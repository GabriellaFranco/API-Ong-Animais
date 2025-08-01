package com.enterprise.ongpet.model.dto.auth;

public record LoginResponseDTO(
        String status,
        String jwtToken
) {
}
