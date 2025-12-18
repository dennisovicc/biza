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
import com.biza.domain.enums.TipoCredito;
import com.biza.dto.CreditoRequest;
import com.biza.repo.CreditoRepo;

@Service
public class CreditoService {

    private final CreditoRepo repo;

    public CreditoService(CreditoRepo repo) {
        this.repo = repo;
    }

    // -------------------------------------------------------------------------
    // Criar crédito (microcrédito)
    // - tipoCredito: RAPIDO ou LONGO
    // - taxaJurosMensal: 15% se RAPIDO, 30% se LONGO
    // - saldoDevedor inicial = montante
    // -------------------------------------------------------------------------
    public Credito criar(CreditoRequest req) {
        var c = new Credito();
        c.setClienteId(req.clienteId());
        c.setTipoCredito(req.tipoCredito());
        c.setMontante(req.montante());
        c.setPrazoMeses(req.prazoMeses());
        c.setStatus(StatusCredito.SOLICITADO);

        // definir taxa de juro mensal pelo tipo de crédito
        if (req.tipoCredito() == TipoCredito.RAPIDO) {
            c.setTaxaJurosMensal(BigDecimal.valueOf(15.00));
        } else if (req.tipoCredito() == TipoCredito.LONGO) {
            c.setTaxaJurosMensal(BigDecimal.valueOf(30.00));
        } else {
            throw new IllegalArgumentException("Tipo de crédito inválido.");
        }

        // saldo devedor inicial = montante
        c.setSaldoDevedor(req.montante());

        return repo.save(c);
    }

    // -------------------------------------------------------------------------
    // Listar créditos (por cliente ou por status, ou todos)
    // -------------------------------------------------------------------------
    public Page<Credito> listar(Long clienteId, StatusCredito status, Pageable pageable) {
        if (clienteId != null) return repo.findByClienteId(clienteId, pageable);
        if (status != null)     return repo.findByStatus(status, pageable);
        return repo.findAll(pageable);
    }

    // -------------------------------------------------------------------------
    // Obter crédito por ID
    // -------------------------------------------------------------------------
    public Credito obter(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Crédito não encontrado"));
    }

    // -------------------------------------------------------------------------
    // Aprovar crédito
    // -------------------------------------------------------------------------
    public Credito aprovar(UUID id) {
        var c = obter(id);
        if (c.getStatus() != StatusCredito.SOLICITADO)
            throw new IllegalStateException("Só é possível aprovar quando está SOLICITADO.");
        c.setStatus(StatusCredito.APROVADO);
        // opcional: dataInicio aqui ou apenas em liberar()
        c.setDataInicio(OffsetDateTime.now());
        return repo.save(c);
    }

    // -------------------------------------------------------------------------
    // Rejeitar crédito
    // -------------------------------------------------------------------------
    public Credito rejeitar(UUID id) {
        var c = obter(id);
        if (c.getStatus() != StatusCredito.SOLICITADO)
            throw new IllegalStateException("Só é possível rejeitar quando está SOLICITADO.");
        c.setStatus(StatusCredito.REJEITADO);
        return repo.save(c);
    }

    // -------------------------------------------------------------------------
    // Liberar crédito (passa para EM_CURSO)
    // -------------------------------------------------------------------------
    public Credito liberar(UUID id) {
        var c = obter(id);
        if (c.getStatus() != StatusCredito.APROVADO)
            throw new IllegalStateException("Só é possível liberar quando está APROVADO.");
        c.setStatus(StatusCredito.EM_CURSO);
        if (c.getDataInicio() == null) {
            c.setDataInicio(OffsetDateTime.now());
        }
        return repo.save(c);
    }

    // -------------------------------------------------------------------------
    // Liquidar crédito (quitar)
    // -------------------------------------------------------------------------
    public Credito liquidar(UUID id) {
        var c = obter(id);
        if (c.getStatus() != StatusCredito.EM_CURSO)
            throw new IllegalStateException("Só é possível liquidar quando está EM_CURSO.");
        c.setStatus(StatusCredito.LIQUIDADO);
        c.setSaldoDevedor(BigDecimal.ZERO);
        return repo.save(c);
    }

    // -------------------------------------------------------------------------
    // Atualizar saldo devedor com base nos juros mensais
    // Usa o método da entidade (juros simples sobre o montante inicial)
    // -------------------------------------------------------------------------
    public Credito atualizarSaldoDevedor(UUID id) {
        var c = obter(id);
        c.atualizarSaldoDevedor(OffsetDateTime.now());
        return repo.save(c);
    }
}
