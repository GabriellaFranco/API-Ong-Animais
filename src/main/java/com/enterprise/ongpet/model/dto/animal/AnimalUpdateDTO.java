package com.enterprise.ongpet.model.dto.animal;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record AnimalUpdateDTO(

        @Size(min = 10, max = 200, message = "A descrição deve ter entre 10 e 200 caracteres")
        String descricao,

        Boolean disponivel,

        List<String> fotos
) {
}
