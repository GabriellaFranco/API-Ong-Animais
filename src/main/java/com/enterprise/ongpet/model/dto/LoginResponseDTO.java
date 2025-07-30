package com.enterprise.ongpet.model.dto;

public record LoginResponseDTO(
        String status,
        String jwtToken
) {
}
