package com.enterprise.ongpet.mapper;

import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoRequestDTO;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoResponseDTO;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoUpdateDTO;
import com.enterprise.ongpet.model.entity.PedidoAdocao;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PedidoAdocaoMapper {

    public PedidoAdocao toPedidoAdocao(PedidoAdocaoRequestDTO pedidoAdocaoDTO, Usuario adotante, Usuario voluntario) {
        return PedidoAdocao.builder()
                .observacoes(pedidoAdocaoDTO.observacoes())
                .adotante(adotante)
                .voluntarioResponsavel(voluntario)
                .build();
    }

    public PedidoAdocaoResponseDTO toPedidoAdocaoResponseDTO(PedidoAdocao pedidoAdocao) {
        return PedidoAdocaoResponseDTO.builder()
                .id(pedidoAdocao.getId())
                .dataPedido(pedidoAdocao.getDataPedido())
                .status(pedidoAdocao.getStatus())
                .observacoes(pedidoAdocao.getObservacoes())
                .adotante(PedidoAdocaoResponseDTO.UsuarioDTO.builder()
                        .id(pedidoAdocao.getAdotante().getId())
                        .nome(pedidoAdocao.getAdotante().getNome())
                        .perfil(pedidoAdocao.getAdotante().getPerfil())
                        .build())
                .voluntarioResponsavel(PedidoAdocaoResponseDTO.UsuarioDTO.builder()
                        .id(pedidoAdocao.getVoluntarioResponsavel().getId())
                        .nome(pedidoAdocao.getVoluntarioResponsavel().getNome())
                        .perfil(pedidoAdocao.getVoluntarioResponsavel().getPerfil())
                        .build())
                .build();
    }


    public PedidoAdocao updateFromDTO(PedidoAdocaoUpdateDTO pedidoAdocaoDTO, PedidoAdocao pedidoAdocaoAtual) {
        return PedidoAdocao.builder()
                .status(Objects.requireNonNull(pedidoAdocaoDTO.statusAdocao()))
                .observacoes(Objects.requireNonNull(pedidoAdocaoDTO.observacoes()))
                .build();
    }
}
