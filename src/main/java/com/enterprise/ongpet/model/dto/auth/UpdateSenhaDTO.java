package com.enterprise.ongpet.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateSenhaDTO(

        @NotBlank
        String senhaAtual,

        @NotBlank
        @Size(min = 6, max = 20, message = "A senha deve conter entre 6 e 20 caracteres")
        String novaSenha
) {
}
