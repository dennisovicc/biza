package com.biza.dto;

import java.math.BigDecimal;

import com.biza.domain.enums.TipoCredito;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreditoRequest(
        @NotNull Long clienteId,
        @NotNull TipoCredito tipoCredito,
        @NotNull @DecimalMin("0.01") BigDecimal montante,
        @NotNull @Min(1) Integer prazoMeses
) {}
