package com.biza.repo;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.biza.domain.Pagamento;

public interface PagamentoRepo extends JpaRepository<Pagamento, UUID> {
    Page<Pagamento> findByCreditoId(UUID creditoId, Pageable pageable);
}
