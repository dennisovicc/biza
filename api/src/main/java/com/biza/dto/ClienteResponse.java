package com.biza.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ClienteResponse(
    Long id,
    String nome,
    String nuit,
    String bi,
    String endereco,
    String telefone,
    String email,
    String tipoCliente,
    BigDecimal rendaMensal,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
