package com.biza.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.biza.domain.enums.StatusCredito;

public record CreditoResponse(
        UUID id,
        UUID clienteId,
        BigDecimal montante,
        BigDecimal taxaJurosAnual,
        Integer prazoMeses,
        StatusCredito status,
        OffsetDateTime dataInicio,
        BigDecimal saldoDevedor,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
