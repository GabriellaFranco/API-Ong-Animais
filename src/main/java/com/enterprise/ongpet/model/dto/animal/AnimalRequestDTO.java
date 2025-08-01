package com.enterprise.ongpet.model.dto.animal;

import com.enterprise.ongpet.enums.Genero;
import com.enterprise.ongpet.enums.PorteAnimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record AnimalRequestDTO(

        @NotBlank
        @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
        String nome,

        @NotBlank
        @Positive(message = "A idade deve ser um número positivo")
        @Pattern(regexp = "^[0-9]+$", message = "Apenas números sõ permitidos neste campo")
        Long idade,

        @NotBlank
        Genero genero,

        @NotBlank
        PorteAnimal porte,

        @NotBlank
        @Size(min = 10, max = 200, message = "A descrição deve ter entre 10 e 200 caracteres")
        String descricao,

        @NotBlank
        List<String> fotos
) {
}
