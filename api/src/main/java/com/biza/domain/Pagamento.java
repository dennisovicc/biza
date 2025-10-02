package com.biza.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamentos", indexes = {
        @Index(name = "idx_pagamentos_credito", columnList = "creditoId")
})
public class Pagamento {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID creditoId;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private OffsetDateTime dataPagamento = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    public void onUpdate() { this.updatedAt = OffsetDateTime.now(); }

    // getters/setters
    public UUID getId() { return id; }
    public UUID getCreditoId() { return creditoId; }
    public void setCreditoId(UUID creditoId) { this.creditoId = creditoId; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public OffsetDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(OffsetDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
