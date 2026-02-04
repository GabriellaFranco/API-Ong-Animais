package com.enterprise.ongpet.model.dto.doacao;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DoacaoRequestDTO(

        @NotNull
        @Positive(message = "A doação deve ser um valor positivo")
        @DecimalMin(value = "1.00", message = "A doação mínima é de R$1,00")
        BigDecimal valor
) {
}
