package com.enterprise.ongpet.model.dto.pedidoadocao;

import com.enterprise.ongpet.enums.StatusAdocao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PedidoAdocaoUpdateDTO(

        @NotBlank
        @Size(min = 10, max = 200, message = "As observações devem conter entre 10 e 200 caracteres")
        String observacoes,

        @NotBlank
        StatusAdocao statusAdocao
) {
}
