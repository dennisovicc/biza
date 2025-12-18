package com.biza.dto;

public record LoginResponse(
        String token,
        AuthUserResponse user
) {}
