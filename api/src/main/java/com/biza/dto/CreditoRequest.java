package com.biza.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreditoRequest(
        @NotNull UUID clienteId,
        @NotNull @DecimalMin("0.01") BigDecimal montante,
        @NotNull @DecimalMin("0.01") BigDecimal taxaJurosAnual,
        @NotNull @Min(1) Integer prazoMeses
) {}
