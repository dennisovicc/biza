package com.biza.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biza.domain.Cliente;
import com.biza.repo.ClienteRepo;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

  private final ClienteRepo repo;

  public ClienteController(ClienteRepo repo) {
    this.repo = repo;
  }

  @GetMapping({"", "/"})
  public List<Cliente> list() {
    return repo.findAll();
  }
  @GetMapping("/{id}")
  public ResponseEntity<Cliente> getById(@PathVariable UUID id) {
    return repo.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping({"", "/"})
  public ResponseEntity<Cliente> create(@RequestBody Cliente c) {
    if (c.getNome() == null || c.getNome().isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(repo.save(c));
  }
}
