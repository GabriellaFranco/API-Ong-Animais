package com.enterprise.ongpet.model.dto.animal;

import com.enterprise.ongpet.enums.Especie;
import com.enterprise.ongpet.enums.Genero;
import com.enterprise.ongpet.enums.PorteAnimal;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@Builder
public record AnimalRequestDTO(

        @NotBlank
        @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
        String nome,

        @NotNull
        Especie especie,

        @NotNull
        @Positive(message = "A idade deve ser um número positivo")
        Long idade,

        @NotNull
        Genero genero,

        @NotNull
        PorteAnimal porte,

        @NotBlank
        @Size(min = 10, max = 200, message = "A descrição deve ter entre 10 e 200 caracteres")
        String descricao,

        @NotEmpty
        List<String> fotos
) {
}
