package com.enterprise.ongpet.model.dto.pedidoadocao;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.lang.Nullable;

@Builder
public record PedidoAdocaoRequestDTO(

        @Nullable
        String observacoes,

        @NotBlank
        Long idAnimal,

        @NotBlank
        Long idAdotante
) {
}
