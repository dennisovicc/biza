package com.biza.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ClienteUpdateRequest(
    @Size(max = 255) String nome,
    @Size(max = 50) String nuit,
    @Size(max = 50) String bi,
    @Size(max = 255) String endereco,
    @Size(max = 50) String telefone,
    @Email @Size(max = 255) String email,
    @Size(max = 30) String tipoCliente,
    @DecimalMin("0.00") BigDecimal rendaMensal
) {}
