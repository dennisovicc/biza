package com.biza.repo;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.biza.domain.Credito;
import com.biza.domain.enums.StatusCredito;

public interface CreditoRepo extends JpaRepository<Credito, UUID> {
    Page<Credito> findByClienteId(UUID clienteId, Pageable pageable);
    Page<Credito> findByStatus(StatusCredito status, Pageable pageable);
}
