package com.enterprise.ongpet.model.dto.usuario;

import com.enterprise.ongpet.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UsuarioUpdateDTO(

        @Email
        String email,

        @Pattern(regexp = "^\\d{8}$", message = "O CEP deve conter exatamente 8 dígitos")
        String cep,

        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Apenas letras são permitidas neste campo")
        String cidade,

        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Apenas letras são permitidas neste campo")
        String bairro,

        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Apenas letras são permitidas neste campo")
        String rua,

        @Pattern(regexp = "^[0-9]+$", message = "Apenas números sõ permitidos neste campo")
        Long numEndereco,

        PerfilUsuario perfil

        ) {
}
