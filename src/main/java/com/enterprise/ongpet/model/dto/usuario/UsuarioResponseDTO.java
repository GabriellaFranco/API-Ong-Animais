package com.enterprise.ongpet.model.dto.usuario;

import com.enterprise.ongpet.enums.PerfilUsuario;
import lombok.Builder;

@Builder
public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        String cep,
        String cidade,
        String bairro,
        String rua,
        Long numEndereco,
        PerfilUsuario perfil
) {
}
