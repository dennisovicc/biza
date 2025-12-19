package com.biza.web;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.biza.domain.Utilizador;
import com.biza.dto.CreateUserRequest;
import com.biza.dto.UserResponse;
import com.biza.repo.UtilizadorRepo;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UtilizadorRepo utilizadorRepo;
    private final PasswordEncoder encoder;

    public AdminController(UtilizadorRepo utilizadorRepo, PasswordEncoder encoder) {
        this.utilizadorRepo = utilizadorRepo;
        this.encoder = encoder;
    }

    // ✅ ENDPOINT 1: Listar com paginação (mantém compatibilidade com frontend atual)
    @GetMapping("/utilizadores")
    public ResponseEntity<?> listarUtilizadores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        // Parse do parâmetro de ordenação
        String[] parts = sort.split(",", 2);
        String campo = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, campo));
        
        Page<UserResponse> result = utilizadorRepo.findAll(pageable)
                .map(this::toResponse);
        
        return ResponseEntity.ok(result);
    }

    // ✅ ENDPOINT 2: Listar todos (sem paginação) - útil para selects simples
    @GetMapping("/utilizadores/todos")
    public ResponseEntity<?> listarTodosUtilizadores() {
        return ResponseEntity.ok(
                utilizadorRepo.findAll().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    // ✅ ENDPOINT 3: Criar utilizador (melhorado com validação completa)
    @PostMapping("/utilizadores")
    public ResponseEntity<?> criarUtilizador(@RequestBody @Valid CreateUserRequest req) {
        
        // Validação: username único
        if (utilizadorRepo.findByUsername(req.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Username já existe"));
        }

        // Validação: name obrigatório (como definido na entidade)
        if (req.name() == null || req.name().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "O nome é obrigatório"));
        }

        Utilizador u = new Utilizador();
        u.setName(req.name().trim());
        u.setUsername(req.username().trim());
        u.setPasswordHash(encoder.encode(req.password()));
        u.setRole(req.role());
        u.setActivo(true);

        Utilizador saved = utilizadorRepo.save(u);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    // ✅ ENDPOINT 4: Inativar utilizador
    @PatchMapping("/utilizadores/{id}/inativar")
    public ResponseEntity<Map<String, String>> inativarUtilizador(@PathVariable Long id) {
        return alterarEstadoUtilizador(id, false);
    }

    // ✅ ENDPOINT 5: Ativar utilizador (nova funcionalidade!)
    @PatchMapping("/utilizadores/{id}/ativar")
    public ResponseEntity<Map<String, String>> ativarUtilizador(@PathVariable Long id) {
        return alterarEstadoUtilizador(id, true);
    }

    // ✅ ENDPOINT 6: Buscar por ID
    @GetMapping("/utilizadores/{id}")
    public ResponseEntity<UserResponse> buscarPorId(@PathVariable Long id) {
        return utilizadorRepo.findById(id)
                .map(u -> ResponseEntity.ok(toResponse(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ ENDPOINT 7: Atualizar informações do utilizador (nova funcionalidade!)
    @PutMapping("/utilizadores/{id}")
    public ResponseEntity<?> atualizarUtilizador(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        
        return utilizadorRepo.findById(id)
                .map(u -> {
                    // Atualizar apenas campos permitidos
                    if (updates.containsKey("name")) {
                        u.setName(updates.get("name").toString().trim());
                    }
                    
                    // Não permitir alterar username por questões de unicidade
                    // Não permitir alterar password por este endpoint (criar endpoint específico)
                    
                    if (updates.containsKey("activo") && updates.get("activo") instanceof Boolean) {
                        u.setActivo((Boolean) updates.get("activo"));
                    }
                    
                    if (updates.containsKey("role")) {
                        try {
                            u.setRole(com.biza.domain.enums.Role.valueOf(updates.get("role").toString()));
                        } catch (IllegalArgumentException e) {
                            // Role inválido, ignorar ou retornar erro
                        }
                    }
                    
                    utilizadorRepo.save(u);
                    return ResponseEntity.ok(toResponse(u));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ ENDPOINT 8: Alterar password (segurança)
    @PatchMapping("/utilizadores/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> alterarPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        
        String novaPassword = request.get("password");
        if (novaPassword == null || novaPassword.trim().length() < 6) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Password deve ter pelo menos 6 caracteres"));
        }
        
        return utilizadorRepo.findById(id)
                .map(u -> {
                    u.setPasswordHash(encoder.encode(novaPassword.trim()));
                    utilizadorRepo.save(u);
                    return ResponseEntity.ok(Map.of("message", "Password alterada com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== MÉTODOS AUXILIARES PRIVADOS ==========

    private UserResponse toResponse(Utilizador u) {
        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getName(),
                u.getRole(),
                u.isActivo()
        );
    }

    private ResponseEntity<Map<String, String>> alterarEstadoUtilizador(Long id, boolean activo) {
        return utilizadorRepo.findById(id)
                .map(u -> {
                    u.setActivo(activo);
                    utilizadorRepo.save(u);
                    
                    String mensagem = activo 
                            ? "Utilizador ativado com sucesso" 
                            : "Utilizador inativado com sucesso";
                    
                    return ResponseEntity.ok(Map.of("message", mensagem));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}