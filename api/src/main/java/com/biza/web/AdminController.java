package com.biza.web;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biza.dto.UtilizadorCreateRequest;
import com.biza.dto.UtilizadorResponse;
import com.biza.repo.UtilizadorRepo;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UtilizadorRepo repo;
    private final PasswordEncoder encoder;

    public AdminController(UtilizadorRepo repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @GetMapping("/utilizadores")
    public ResponseEntity<?> listar(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id,asc") String sort) {

        String[] parts = sort.split(",", 2);
        String campo = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        var pageable = PageRequest.of(page, size, Sort.by(dir, campo));

        var p = repo.findAll(pageable).map(u ->
                new UtilizadorResponse(u.getId(), u.getUsername(), u.getName(), u.getRole(), u.isActivo())
        );

        return ResponseEntity.ok(p);
    }

    @PostMapping("/utilizadores")
    public ResponseEntity<?> criar(@RequestBody @Valid UtilizadorCreateRequest req) {

        if (repo.findByUsername(req.username()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username já existe"));
        }

        var u = new com.biza.domain.Utilizador();
        u.setUsername(req.username().trim());
        u.setPasswordHash(encoder.encode(req.password()));
        u.setRole(req.role());
        u.setActivo(true);

        var saved = repo.save(u);

        return ResponseEntity.ok(new UtilizadorResponse(
                saved.getId(), saved.getUsername(), null, saved.getRole(), saved.isActivo()
        ));
    }

    @PatchMapping("/utilizadores/{id}/inativar")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        var u = repo.findById(id).orElseThrow();
        u.setActivo(false);
        repo.save(u);
        return ResponseEntity.ok(Map.of("message", "Utilizador inativado"));
    }

    @PatchMapping("/utilizadores/{id}/ativar")
    public ResponseEntity<?> ativar(@PathVariable Long id) {
        var u = repo.findById(id).orElseThrow();
        u.setActivo(true);
        repo.save(u);
        return ResponseEntity.ok(Map.of("message", "Utilizador ativado"));
    }
}
