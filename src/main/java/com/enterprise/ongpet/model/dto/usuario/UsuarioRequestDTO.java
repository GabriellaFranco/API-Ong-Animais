package com.enterprise.ongpet.model.dto.usuario;

import com.enterprise.ongpet.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UsuarioRequestDTO(

        @NotBlank
        @Size(min = 6, max = 80, message = "O nome deve ter entre 6 e 80 caracteres")
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos")
        String cpf,

        @NotBlank
        @Size(min = 6, max = 20, message = "A senha deve conter entre 6 e 20 caracteres")
        String senha,

        @NotBlank
        @Pattern(regexp = "^\\d{8}$", message = "O CEP deve conter exatamente 8 dígitos")
        String cep,

        @NotBlank
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Apenas letras são permitidas neste campo")
        String cidade,

        @NotBlank
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Apenas letras são permitidas neste campo")
        String bairro,

        @NotBlank
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Apenas letras são permitidas neste campo")
        String rua,

        @NotBlank
        @Pattern(regexp = "^[0-9]+$", message = "Apenas números são permitidos neste campo")
        Long numEndereco,

        @NotBlank
        PerfilUsuario perfil
) {
}
