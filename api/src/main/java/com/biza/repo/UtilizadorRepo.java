package com.biza.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biza.domain.Utilizador;

public interface UtilizadorRepo extends JpaRepository<Utilizador, Long> {
    Optional<Utilizador> findByUsername(String username);
}
