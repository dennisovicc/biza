package com.biza.web;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biza.domain.Cliente;
import com.biza.repo.ClienteRepo;
import com.biza.util.PageResponse;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepo repo;

    public ClienteController(ClienteRepo repo) {
        this.repo = repo;
    }

    // 👉 LISTAGEM PAGINADA
    @GetMapping
    public PageResponse<Cliente> list(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "nome,asc") String sort) {

        // separa "campo" e "ordem"
        String[] parts = sort.split(",", 2);
        String campo = parts[0];
        Sort.Direction direcao = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<Cliente> pagina = repo.findAll(PageRequest.of(page, size, Sort.by(direcao, campo)));

        return PageResponse.of(pagina); // devolve no formato bonitinho
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable UUID id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> create(@RequestBody Cliente c) {
        if (c.getNome() == null || c.getNome().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.save(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable UUID id, @RequestBody Cliente req) {
        return repo.findById(id)
                .map(c -> {
                    if (req.getNome() != null) c.setNome(req.getNome());
                    if (req.getNuit() != null) c.setNuit(req.getNuit());
                    if (req.getBi() != null) c.setBi(req.getBi());
                    if (req.getEndereco() != null) c.setEndereco(req.getEndereco());
                    if (req.getTelefone() != null) c.setTelefone(req.getTelefone());
                    if (req.getEmail() != null) c.setEmail(req.getEmail());
                    if (req.getTipoCliente() != null) c.setTipoCliente(req.getTipoCliente());
                    if (req.getRendaMensal() != null) c.setRendaMensal(req.getRendaMensal());
                    c.setUpdatedAt(OffsetDateTime.now());
                    return ResponseEntity.ok(repo.save(c));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
