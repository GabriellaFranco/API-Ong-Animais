package com.enterprise.ongpet.model.dto.pedidoadocao;

import com.enterprise.ongpet.enums.PerfilUsuario;
import com.enterprise.ongpet.enums.StatusAdocao;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PedidoAdocaoResponseDTO(
        Long id,
        LocalDate dataPedido,
        StatusAdocao status,
        String observacoes,
        AnimalDTO animal,
        UsuarioDTO adotante,
        UsuarioDTO voluntarioResponsavel

) {
    @Builder
    public record AnimalDTO(
            Long id,
            String nome
    ){}

    @Builder
    public record UsuarioDTO(
            Long id,
            String nome,
            PerfilUsuario perfil
    ) {}
}
