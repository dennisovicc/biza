package com.biza.dto;

import com.biza.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank String username,
        @NotBlank String password,
        @NotNull Role role
) {}
