package com.biza.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequest(
        @NotNull UUID creditoId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal valor,
        // opcional: se não mandar, vamos usar agora()
        OffsetDateTime dataPagamento
) {}
