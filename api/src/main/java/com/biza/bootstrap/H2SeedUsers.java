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
        // cria só se ainda não existirem com nomes específicos
        criarSeNaoExiste("admin", "admin123", "Albano Silva", Role.ADMIN);
        criarSeNaoExiste("oficial", "oficial123", "Francisco Biza", Role.OFICIAL_CREDITO);
        criarSeNaoExiste("gestor", "gestor123", "John Snow", Role.GESTOR_CREDITO);
        
        System.out.println("Seed de utilizadores H2 concluído com sucesso!");
    }

    private void criarSeNaoExiste(String username, String rawPassword, String nome, Role role) {
        if (repo.findByUsername(username).isPresent()) {
            System.out.println("Utilizador '" + username + "' já existe, a ignorar...");
            return;
        }

        Utilizador u = new Utilizador();
        u.setUsername(username);
        u.setName(nome);  // Nome do utilizador (obrigatório)
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRole(role);
        u.setActivo(true);
        // createdAt é automaticamente definido na entidade Utilizador

        repo.save(u);
        System.out.println("✓ Utilizador criado: " + username + " (" + nome + ") como " + role);
    }
}