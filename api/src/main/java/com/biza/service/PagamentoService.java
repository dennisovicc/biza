package com.biza.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biza.domain.Credito;
import com.biza.domain.Pagamento;
import com.biza.domain.enums.StatusCredito;
import com.biza.dto.PagamentoRequest;
import com.biza.repo.CreditoRepo;
import com.biza.repo.PagamentoRepo;

@Service
public class PagamentoService {

    private final PagamentoRepo pagamentoRepo;
    private final CreditoRepo creditoRepo;

    public PagamentoService(PagamentoRepo pagamentoRepo, CreditoRepo creditoRepo) {
        this.pagamentoRepo = pagamentoRepo;
        this.creditoRepo = creditoRepo;
    }

    public Page<Pagamento> listar(UUID creditoId, Pageable pageable) {
        if (creditoId != null) return pagamentoRepo.findByCreditoId(creditoId, pageable);
        return pagamentoRepo.findAll(pageable);
    }

    public Pagamento obter(UUID id) {
        return pagamentoRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Pagamento não encontrado"));
    }

    @Transactional
    public Pagamento registrar(PagamentoRequest req) {
        // 1) validar crédito
        Credito credito = creditoRepo.findById(req.creditoId())
                .orElseThrow(() -> new NoSuchElementException("Crédito não encontrado"));

        if (credito.getStatus() != StatusCredito.EM_CURSO) {
            throw new IllegalStateException("Não é possível registrar pagamento: crédito não está EM_CURSO.");
        }

        // 2) validar valor
        BigDecimal valor = req.valor().setScale(2);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do pagamento deve ser maior que zero.");
        }
        if (valor.compareTo(credito.getSaldoDevedor()) > 0) {
            throw new IllegalStateException("Valor do pagamento excede o saldo devedor.");
        }

        // 3) registrar pagamento
        Pagamento p = new Pagamento();
        p.setCreditoId(credito.getId());
        p.setValor(valor);
        p.setDataPagamento(req.dataPagamento() != null ? req.dataPagamento() : OffsetDateTime.now());
        Pagamento salvo = pagamentoRepo.save(p);

        // 4) atualizar saldo e status do crédito
        BigDecimal novoSaldo = credito.getSaldoDevedor().subtract(valor).setScale(2);
        credito.setSaldoDevedor(novoSaldo);
        if (novoSaldo.compareTo(BigDecimal.ZERO) == 0) {
            credito.setStatus(StatusCredito.LIQUIDADO);
        }
        creditoRepo.save(credito);

        return salvo;
    }
}
