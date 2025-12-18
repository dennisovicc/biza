package com.biza.bootstrap;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.biza.domain.Utilizador;
import com.biza.domain.enums.Role;
import com.biza.repo.UtilizadorRepo;

import jakarta.annotation.PostConstruct;

@Component
@Profile("h2")
public class H2SeedUsers {

    private final UtilizadorRepo repo;
    private final PasswordEncoder encoder;

    public H2SeedUsers(UtilizadorRepo repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @PostConstruct
    public void seed() {

        // cria só se ainda não existirem
        criarSeNaoExiste("admin", "admin123", Role.ADMIN);
        criarSeNaoExiste("oficial", "oficial123", Role.OFICIAL_CREDITO);
        criarSeNaoExiste("gestor", "gestor123", Role.GESTOR_CREDITO);
    }

    private void criarSeNaoExiste(String username, String rawPassword, Role role) {
        if (repo.findByUsername(username).isPresent()) return;

        Utilizador u = new Utilizador();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRole(role);
        u.setActivo(true);

        repo.save(u);
    }
}
