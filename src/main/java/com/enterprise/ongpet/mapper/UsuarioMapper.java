package com.enterprise.ongpet.mapper;

import com.enterprise.ongpet.model.dto.usuario.UsuarioRequestDTO;
import com.enterprise.ongpet.model.dto.usuario.UsuarioResponseDTO;
import com.enterprise.ongpet.model.dto.usuario.UsuarioUpdateDTO;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UsuarioMapper {

    private Usuario toUsuario(UsuarioRequestDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.nome())
                .email(usuarioDTO.email())
                .senha(usuarioDTO.senha())
                .cpf(usuarioDTO.cpf())
                .cep(usuarioDTO.cep())
                .cidade(usuarioDTO.cidade())
                .bairro(usuarioDTO.bairro())
                .rua(usuarioDTO.rua())
                .numEndereco(usuarioDTO.numEndereco())
                .perfil(usuarioDTO.perfil())
                .build();
    }

    private UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .cpf(usuario.getCpf())
                .cep(usuario.getCep())
                .cidade(usuario.getCidade())
                .bairro(usuario.getBairro())
                .rua(usuario.getRua())
                .numEndereco(usuario.getNumEndereco())
                .perfil(usuario.getPerfil())
                .build();
    }

    private Usuario updateFromDTO(UsuarioUpdateDTO updateDTO, Usuario usuarioAtual) {
        return Usuario.builder()
                .email(Objects.requireNonNullElse(updateDTO.email(), usuarioAtual.getEmail()))
                .cep(Objects.requireNonNullElse(updateDTO.cep(), usuarioAtual.getCep()))
                .cidade(Objects.requireNonNullElse(updateDTO.cidade(), usuarioAtual.getCidade()))
                .bairro(Objects.requireNonNullElse(updateDTO.bairro(), usuarioAtual.getBairro()))
                .rua(Objects.requireNonNullElse(updateDTO.rua(), usuarioAtual.getRua()))
                .numEndereco(Objects.requireNonNullElse(updateDTO.numEndereco(), usuarioAtual.getNumEndereco()))
                .build();
    }
}
