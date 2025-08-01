package com.enterprise.ongpet.model.dto.animal;

import com.enterprise.ongpet.enums.Genero;
import com.enterprise.ongpet.enums.PorteAnimal;
import lombok.Builder;

import java.util.List;

@Builder
public record AnimalResponseDTO(
        Long id,
        String nome,
        Long idade,
        Genero genero,
        PorteAnimal porte,
        String descricao,
        Boolean disponivel,
        List<String> fotos,
        UsuarioDTO responsavel
) {
    @Builder
    public record UsuarioDTO(
            Long id,
            String nome
    ){}
}
