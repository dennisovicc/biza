package com.biza.web;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biza.domain.Cliente;
import com.biza.domain.enums.StatusRegistro;
import com.biza.dto.ClienteRequest;
import com.biza.dto.ClienteResponse;
import com.biza.dto.ClienteUpdateRequest;
import com.biza.repo.ClienteRepo;
import com.biza.util.PageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepo repo;

    public ClienteController(ClienteRepo repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasAnyRole('OFICIAL_CREDITO','ADMIN')")
    @GetMapping
    public PageResponse<ClienteResponse> list(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "nome,asc") String sort) {
        String[] parts = sort.split(",", 2);
        String campo = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Page<Cliente> p = repo.findAll(PageRequest.of(page, size, Sort.by(dir, campo)));
        return PageResponse.of(p.map(this::toResponse));
    }

    @PreAuthorize("hasAnyRole('OFICIAL_CREDITO','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(c -> ResponseEntity.ok(toResponse(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('OFICIAL_CREDITO')")
    @PostMapping
    public ResponseEntity<ClienteResponse> create(@RequestBody @Valid ClienteRequest req) {
        Cliente c = new Cliente();
        applyCreate(req, c);
        Cliente saved = repo.save(c);
        return ResponseEntity.ok(toResponse(saved));
    }

    @PreAuthorize("hasRole('OFICIAL_CREDITO')")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> put(@PathVariable Long id,
                                               @RequestBody @Valid ClienteRequest req) {
        return repo.findById(id)
                .map(c -> {
                    applyCreate(req, c);
                    c.setUpdatedAt(OffsetDateTime.now());
                    return ResponseEntity.ok(toResponse(repo.save(c)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('OFICIAL_CREDITO')")
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponse> patch(@PathVariable Long id,
                                                 @RequestBody @Valid ClienteUpdateRequest req) {
        return repo.findById(id)
                .map(c -> {
                    applyPatch(req, c);
                    c.setUpdatedAt(OffsetDateTime.now());
                    return ResponseEntity.ok(toResponse(repo.save(c)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok().body(Map.of("message", "Cliente removido com sucesso"));
    }

        
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        return repo.findById(id)
            .map(c -> {
                c.setStatus(StatusRegistro.INACTIVO); // ou c.setActivo(false) conforme o teu modelo
                c.setUpdatedAt(OffsetDateTime.now());
                repo.save(c);
                return ResponseEntity.ok().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // -------- helpers --------
    private void applyCreate(ClienteRequest r, Cliente c) {
        c.setNome(r.nome());
        c.setNuit(r.nuit());
        c.setBi(r.bi());
        c.setEndereco(r.endereco());
        c.setTelefone(r.telefone());
        c.setEmail(r.email());
        c.setTipoCliente(r.tipoCliente());
        c.setRendaMensal(r.rendaMensal());
    }

    private void applyPatch(ClienteUpdateRequest r, Cliente c) {
        if (r.nome() != null) c.setNome(r.nome());
        if (r.nuit() != null) c.setNuit(r.nuit());
        if (r.bi() != null) c.setBi(r.bi());
        if (r.endereco() != null) c.setEndereco(r.endereco());
        if (r.telefone() != null) c.setTelefone(r.telefone());
        if (r.email() != null) c.setEmail(r.email());
        if (r.tipoCliente() != null) c.setTipoCliente(r.tipoCliente());
        if (r.rendaMensal() != null) c.setRendaMensal(r.rendaMensal());
    }

    private ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNome(),
                c.getNuit(),
                c.getBi(),
                c.getEndereco(),
                c.getTelefone(),
                c.getEmail(),
                c.getTipoCliente(),
                c.getRendaMensal(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
