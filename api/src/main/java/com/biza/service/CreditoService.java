package com.biza.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.biza.domain.Credito;
import com.biza.domain.enums.StatusCredito;
import com.biza.dto.CreditoRequest;
import com.biza.repo.CreditoRepo;

@Service
public class CreditoService {

    private final CreditoRepo repo;

    public CreditoService(CreditoRepo repo) {
        this.repo = repo;
    }

    public Credito criar(CreditoRequest req) {
        var c = new Credito();
        c.setClienteId(req.clienteId());
        c.setMontante(req.montante());
        c.setTaxaJurosAnual(req.taxaJurosAnual());
        c.setPrazoMeses(req.prazoMeses());
        c.setStatus(StatusCredito.SOLICITADO);
        c.setSaldoDevedor(req.montante());
        return repo.save(c);
    }

    public Page<Credito> listar(UUID clienteId, StatusCredito status, Pageable pageable) {
        if (clienteId != null) return repo.findByClienteId(clienteId, pageable);
        if (status != null)     return repo.findByStatus(status, pageable);
        return repo.findAll(pageable);
    }

    public Credito obter(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Crédito não encontrado"));
    }

    public Credito aprovar(UUID id) {
        var c = obter(id);
        if (c.getStatus() != StatusCredito.SOLICITADO)
            throw new IllegalStateException("Só é possível aprovar quando está SOLICITADO.");
        c.setStatus(StatusCredito.APROVADO);
        c.setDataInicio(OffsetDateTime.now());
        return repo.save(c);
    }

    public Credito rejeitar(UUID id) {
        var c = obter(id);
        if (c.getStatus() != StatusCredito.SOLICITADO)
            throw new IllegalStateException("Só é possível rejeitar quando está SOLICITADO.");
        c.setStatus(StatusCredito.REJEITADO);
        return repo.save(c);
    }

    public Credito liberar(UUID id) {
        var c = obter(id);
        if (c.getStatus() != StatusCredito.APROVADO)
            throw new IllegalStateException("Só é possível liberar quando está APROVADO.");
        c.setStatus(StatusCredito.EM_CURSO);
        if (c.getDataInicio() == null) c.setDataInicio(OffsetDateTime.now());
        return repo.save(c);
    }

    public Credito liquidar(UUID id) {
        var c = obter(id);
        if (c.getStatus() != StatusCredito.EM_CURSO)
            throw new IllegalStateException("Só é possível liquidar quando está EM_CURSO.");
        c.setStatus(StatusCredito.LIQUIDADO);
        c.setSaldoDevedor(BigDecimal.ZERO);
        return repo.save(c);
    }
}
