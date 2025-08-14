package com.enterprise.ongpet.mapper;

import com.enterprise.ongpet.model.dto.doacao.DoacaoRequestDTO;
import com.enterprise.ongpet.model.dto.doacao.DoacaoResponseDTO;
import com.enterprise.ongpet.model.entity.Doacao;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class DoacaoMapper {

    public Doacao toDoacao(DoacaoRequestDTO doacaoDTO, Usuario doador) {
        return Doacao.builder()
                .valor(doacaoDTO.valor())
                .doador(doador)
                .build();
    }

    public DoacaoResponseDTO toDoacaoResponseDTO(Doacao doacao) {
        return DoacaoResponseDTO.builder()
                .id(doacao.getId())
                .valor(doacao.getValor())
                .dataDoacao(doacao.getData())
                .doador(DoacaoResponseDTO.UsuarioDTO.builder()
                        .id(doacao.getDoador().getId())
                        .nome(doacao.getDoador().getNome())
                        .build())
                .build();
    }
}
