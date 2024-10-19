package com.saude360.backendsaude360.dtos;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record LoginTokenResponseDto(
        String cpf,
        String token,
        List<GrantedAuthority> roles
) {
}
