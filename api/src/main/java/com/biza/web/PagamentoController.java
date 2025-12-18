package com.biza.web;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biza.domain.Pagamento;
import com.biza.dto.PagamentoRequest;
import com.biza.dto.PagamentoResponse;
import com.biza.service.PagamentoService;
import com.biza.util.PageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pagamentos")
@CrossOrigin(origins = "*")
public class PagamentoController {

    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('GESTOR_CREDITO')")
    @PostMapping
    public PagamentoResponse registrar(@RequestBody @Valid PagamentoRequest req) {
        return toResponse(service.registrar(req));
    }

    @PreAuthorize("hasAnyRole('GESTOR_CREDITO','ADMIN')")
    @GetMapping("/{id}")
    public PagamentoResponse obter(@PathVariable UUID id) {
        return toResponse(service.obter(id));
    }

    @PreAuthorize("hasAnyRole('GESTOR_CREDITO','ADMIN')")
    @GetMapping
    public PageResponse<PagamentoResponse> listar(@RequestParam(required = false) String creditoId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "dataPagamento,desc") String sort) {

        String[] parts = sort.split(",", 2);
        String campo = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        var pageable = PageRequest.of(page, size, Sort.by(dir, campo));

        UUID creditoUUID = toUuid(creditoId);
        var pagina = service.listar(creditoUUID, pageable);
        return PageResponse.of(pagina.map(this::toResponse));
    }

    private UUID toUuid(String s) {
        if (s == null || s.isBlank()) return null;
        try { return UUID.fromString(s); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("creditoId inválido"); }
    }

    private PagamentoResponse toResponse(Pagamento p) {
        return new PagamentoResponse(
                p.getId(),
                p.getCreditoId(),
                p.getValor(),
                p.getDataPagamento(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
