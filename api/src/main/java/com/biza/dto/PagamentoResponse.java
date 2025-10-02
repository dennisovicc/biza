package com.biza.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PagamentoResponse(
        UUID id,
        UUID creditoId,
        BigDecimal valor,
        OffsetDateTime dataPagamento,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
