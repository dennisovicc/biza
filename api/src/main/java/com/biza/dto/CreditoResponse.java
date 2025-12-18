package com.biza.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.biza.domain.enums.StatusCredito;
import com.biza.domain.enums.TipoCredito;

public record CreditoResponse(
        UUID id,
        Long clienteId,
        TipoCredito tipoCredito,
        BigDecimal montante,
        Integer prazoMeses,
        BigDecimal taxaJurosMensal,
        StatusCredito status,
        OffsetDateTime dataInicio,
        BigDecimal saldoDevedor,
        Integer mesesEmAtraso,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
