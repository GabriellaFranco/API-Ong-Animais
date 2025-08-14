package com.enterprise.ongpet.model.dto.doacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DoacaoRequestDTO(

        @NotBlank
        @Positive
        @Pattern(regexp = "^[0-9]+$", message = "Apenas números sõ permitidos neste campo")
        BigDecimal valor
) {
}
