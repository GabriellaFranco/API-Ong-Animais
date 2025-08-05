package com.enterprise.ongpet.model.dto.doacao;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record DoacaoResponseDTO(
        Long id,
        BigDecimal valor,
        LocalDate dataDoacao,
        UsuarioDTO doador
) {
    @Builder
    public record UsuarioDTO(
            Long id,
            String nome
    ) {}
}
