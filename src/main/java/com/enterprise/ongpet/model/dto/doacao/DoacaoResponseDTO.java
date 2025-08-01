package com.enterprise.ongpet.model.dto.doacao;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DoacaoResponseDTO(
        Long id,
        BigDecimal valor,
        UserDTO doador
) {
    @Builder
    public record UserDTO(
            Long id,
            String nome
    ) {}
}
