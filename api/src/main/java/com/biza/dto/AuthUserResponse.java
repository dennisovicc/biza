package com.biza.dto;

import com.biza.domain.enums.Role;

public record AuthUserResponse(
        Long id,
        String username,
        String name,
        Role role
) {}
