package com.biza.dto;

import com.biza.domain.enums.Role;

public record UtilizadorResponse(
        Long id,
        String username,
        String name,
        Role role,
        boolean activo
) {}
