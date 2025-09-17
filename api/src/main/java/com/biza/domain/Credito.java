package com.biza.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.biza.domain.enums.StatusCredito;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "creditos")
public class Credito {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID clienteId;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal montante;          // valor do empréstimo

    @Column(nullable = false, precision = 5,  scale = 2)
    private BigDecimal taxaJurosAnual;    // % a.a

    @Column(nullable = false)
    private Integer prazoMeses;           // prazo total

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCredito status = StatusCredito.SOLICITADO;

    @Column
    private OffsetDateTime dataInicio;    // quando começa a contar (após aprovação/liberação)

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoDevedor;      // inicia == montante

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    public void onUpdate() { this.updatedAt = OffsetDateTime.now(); }

    // getters/setters
    public UUID getId() { return id; }
    public UUID getClienteId() { return clienteId; }
    public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }
    public BigDecimal getMontante() { return montante; }
    public void setMontante(BigDecimal montante) { this.montante = montante; }
    public BigDecimal getTaxaJurosAnual() { return taxaJurosAnual; }
    public void setTaxaJurosAnual(BigDecimal taxaJurosAnual) { this.taxaJurosAnual = taxaJurosAnual; }
    public Integer getPrazoMeses() { return prazoMeses; }
    public void setPrazoMeses(Integer prazoMeses) { this.prazoMeses = prazoMeses; }
    public StatusCredito getStatus() { return status; }
    public void setStatus(StatusCredito status) { this.status = status; }
    public OffsetDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(OffsetDateTime dataInicio) { this.dataInicio = dataInicio; }
    public BigDecimal getSaldoDevedor() { return saldoDevedor; }
    public void setSaldoDevedor(BigDecimal saldoDevedor) { this.saldoDevedor = saldoDevedor; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
