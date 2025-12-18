package com.biza.dto;

import com.biza.domain.enums.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UtilizadorCreateRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 3, max = 100) String name,
        @NotBlank @Size(min = 4, max = 100) String password,
        @NotNull Role role
) {}
