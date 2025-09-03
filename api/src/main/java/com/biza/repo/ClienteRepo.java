package com.biza.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biza.domain.Cliente;

public interface ClienteRepo extends JpaRepository<Cliente, UUID> {}
