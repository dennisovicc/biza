			package com.biza.exception;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_ERROR_URI = "https://biza.dev/errors/";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setType(URI.create(BASE_ERROR_URI + "validation"));
        pd.setTitle("Dados inválidos");
        pd.setDetail("Um ou mais campos são inválidos.");

        List<Map<String,String>> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> Map.of("field", fe.getField(), "message",
                    Optional.ofNullable(fe.getDefaultMessage()).orElse("inválido")))
            .toList();
        pd.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(java.util.NoSuchElementException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setType(URI.create(BASE_ERROR_URI + "not-found"));
        pd.setTitle("Recurso não encontrado");
        pd.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> handleBusiness(IllegalStateException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setType(URI.create(BASE_ERROR_URI + "business"));
        pd.setTitle("Regra de negócio violada");
        pd.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    // 409 - Duplicado via validação programática (ex.: existsByNuit)
    @ExceptionHandler(com.biza.exception.DomainExceptions.Duplicate.class)
    public ResponseEntity<ProblemDetail> handleDuplicate(DomainExceptions.Duplicate ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setType(URI.create(BASE_ERROR_URI + "duplicate"));
        pd.setTitle("Dados duplicados");
        pd.setDetail(ex.getMessage());
        if (ex.getField() != null) {
            pd.setProperty("conflicts", List.of(Map.of("field", ex.getField(), "value", ex.getValue())));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    // 409 - Duplicado por constraint do banco (ex.: unique index / chave única)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrity(DataIntegrityViolationException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setType(URI.create(BASE_ERROR_URI + "duplicate"));
        pd.setTitle("Dados duplicados");
        pd.setDetail("Violação de unicidade ou integridade de dados.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    // 400 - Violações de Bean Validation fora do binding de método
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setType(URI.create(BASE_ERROR_URI + "validation"));
        pd.setTitle("Dados inválidos");
        List<Map<String,String>> errors = ex.getConstraintViolations().stream()
            .map(cv -> Map.of("field", pathOf(cv), "message", cv.getMessage()))
            .collect(Collectors.toList());
        pd.setProperty("errors", errors);
        pd.setDetail("Um ou mais campos são inválidos.");
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setType(URI.create(BASE_ERROR_URI + "internal"));
        pd.setTitle("Erro interno");
        pd.setDetail("Ocorreu um erro inesperado. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

    private String pathOf(ConstraintViolation<?> cv) {
        String p = cv.getPropertyPath() == null ? "" : cv.getPropertyPath().toString();
        int idx = p.lastIndexOf('.');
        return idx >= 0 ? p.substring(idx + 1) : p;
    }
}
