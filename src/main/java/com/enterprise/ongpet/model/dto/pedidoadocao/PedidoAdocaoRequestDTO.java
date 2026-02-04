package com.enterprise.ongpet.model.dto.pedidoadocao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.lang.Nullable;

@Builder
public record PedidoAdocaoRequestDTO(

        @Nullable
        String observacoes,

        @NotNull
        Long idAnimal,

        @NotNull
        Long idAdotante
) {
}
