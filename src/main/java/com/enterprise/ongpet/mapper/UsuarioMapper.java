package com.enterprise.ongpet.mapper;

import com.enterprise.ongpet.model.dto.usuario.UsuarioRequestDTO;
import com.enterprise.ongpet.model.dto.usuario.UsuarioResponseDTO;
import com.enterprise.ongpet.model.dto.usuario.UsuarioUpdateDTO;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class UsuarioMapper {

    public Usuario toUsuario(UsuarioRequestDTO usuarioDTO) {
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

    public UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario) {
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

    public void updateFromDTO(UsuarioUpdateDTO updateDTO, Usuario usuarioAtual) {
        Optional.ofNullable(updateDTO.email()).ifPresent(usuarioAtual::setEmail);
        Optional.ofNullable(updateDTO.cep()).ifPresent(usuarioAtual::setCep);
        Optional.ofNullable(updateDTO.cidade()).ifPresent(usuarioAtual::setCidade);
        Optional.ofNullable(updateDTO.bairro()).ifPresent(usuarioAtual::setBairro);
        Optional.ofNullable(updateDTO.rua()).ifPresent(usuarioAtual::setRua);
        Optional.ofNullable(updateDTO.numEndereco()).ifPresent(usuarioAtual::setNumEndereco);
    }
}
