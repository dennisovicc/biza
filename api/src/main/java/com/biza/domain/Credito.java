package com.biza.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.biza.domain.enums.StatusCredito;
import com.biza.domain.enums.TipoCredito;

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
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_credito", nullable = false, length = 20)
    private TipoCredito tipoCredito;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal montante;

    // prazo em meses (mesmo para microcrédito "rápido")
    @Column(name = "prazo_meses", nullable = false)
    private Integer prazoMeses;

    // 15.00 ou 30.00 (% ao mês)
    @Column(name = "taxa_juros_mensal", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxaJurosMensal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCredito status = StatusCredito.SOLICITADO;

    @Column(name = "data_inicio")
    private OffsetDateTime dataInicio;

    @Column(name = "saldo_devedor", nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoDevedor;

    // novos: meses em atraso (0 se ainda dentro do prazo)
    @Column(name = "meses_em_atraso")
    private Integer mesesEmAtraso = 0;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    // ---------------------------------------------------------------------
    // Atualizar saldo devedor com base em juros simples mensais
    //
    // Regras:
    // - Juros incidem SEMPRE sobre o valor inicial (montante)
    // - RAPIDO  -> 15% ao mês
    // - LONGO   -> 30% ao mês
    // - mesesDecorridos = meses entre data_inicio e agora
    // - jurosTotais = montante * (taxaMensal/100) * mesesDecorridos
    // - mesesEmAtraso = max(0, mesesDecorridos - prazoMeses)
    // ---------------------------------------------------------------------
    public void atualizarSaldoDevedor(OffsetDateTime agora) {
        if (dataInicio == null || taxaJurosMensal == null) {
            return;
        }

        long mesesDecorridos = ChronoUnit.MONTHS.between(
                dataInicio.toLocalDate(),
                agora.toLocalDate()
        );

        if (mesesDecorridos < 0) {
            mesesDecorridos = 0;
        }

        // calcular meses em atraso em relação ao prazo
        int emAtraso = 0;
        if (prazoMeses != null && prazoMeses > 0 && mesesDecorridos > prazoMeses) {
            emAtraso = (int) (mesesDecorridos - prazoMeses);
        }
        this.mesesEmAtraso = emAtraso;

        if (mesesDecorridos == 0) {
            // ainda no primeiro mês, sem juros acumulados
            this.saldoDevedor = montante.setScale(2, RoundingMode.HALF_UP);
            return;
        }

        BigDecimal principal = montante.setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxa = taxaJurosMensal
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP); // 15 -> 0.1500

        BigDecimal jurosTotais = principal
                .multiply(taxa)
                .multiply(BigDecimal.valueOf(mesesDecorridos));

        BigDecimal novoSaldo = principal
                .add(jurosTotais)
                .setScale(2, RoundingMode.HALF_UP);

        this.saldoDevedor = novoSaldo;
    }

    // ---- Getters e Setters ----

    public UUID getId() { return id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public TipoCredito getTipoCredito() { return tipoCredito; }
    public void setTipoCredito(TipoCredito tipoCredito) { this.tipoCredito = tipoCredito; }

    public BigDecimal getMontante() { return montante; }
    public void setMontante(BigDecimal montante) { this.montante = montante; }

    public Integer getPrazoMeses() { return prazoMeses; }
    public void setPrazoMeses(Integer prazoMeses) { this.prazoMeses = prazoMeses; }

    public BigDecimal getTaxaJurosMensal() { return taxaJurosMensal; }
    public void setTaxaJurosMensal(BigDecimal taxaJurosMensal) { this.taxaJurosMensal = taxaJurosMensal; }

    public StatusCredito getStatus() { return status; }
    public void setStatus(StatusCredito status) { this.status = status; }

    public OffsetDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(OffsetDateTime dataInicio) { this.dataInicio = dataInicio; }

    public BigDecimal getSaldoDevedor() { return saldoDevedor; }
    public void setSaldoDevedor(BigDecimal saldoDevedor) { this.saldoDevedor = saldoDevedor; }

    public Integer getMesesEmAtraso() { return mesesEmAtraso; }
    public void setMesesEmAtraso(Integer mesesEmAtraso) { this.mesesEmAtraso = mesesEmAtraso; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
