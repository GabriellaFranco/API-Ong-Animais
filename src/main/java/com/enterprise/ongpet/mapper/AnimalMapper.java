package com.enterprise.ongpet.mapper;

import com.enterprise.ongpet.model.dto.animal.AnimalRequestDTO;
import com.enterprise.ongpet.model.dto.animal.AnimalResponseDTO;
import com.enterprise.ongpet.model.dto.animal.AnimalUpdateDTO;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AnimalMapper {

    public Animal toAnimal(AnimalRequestDTO animalDTO, Usuario responsavel) {
        return Animal.builder()
                .nome(animalDTO.nome())
                .idade(animalDTO.idade())
                .porte(animalDTO.porte())
                .genero(animalDTO.genero())
                .descricao(animalDTO.descricao())
                .fotos(animalDTO.fotos())
                .responsavel(responsavel)
                .build();
    }

    public AnimalResponseDTO toAnimalResponseDTO(Animal animal) {
        return AnimalResponseDTO.builder()
                .id(animal.getId())
                .nome(animal.getNome())
                .idade(animal.getIdade())
                .genero(animal.getGenero())
                .porte(animal.getPorte())
                .descricao(animal.getDescricao())
                .disponivel(animal.getDisponivel())
                .fotos(animal.getFotos())
                .responsavel(AnimalResponseDTO.UsuarioDTO.builder()
                        .id(animal.getResponsavel().getId())
                        .nome(animal.getResponsavel().getNome())
                        .build())
                .build();
    }

    public Animal updateFromDTO(AnimalUpdateDTO updateDTO, Animal animalAtual) {
        return Animal.builder()
                .descricao(Objects.requireNonNullElse(updateDTO.descricao(), animalAtual.getDescricao()))
                .fotos(Objects.requireNonNullElse(updateDTO.fotos(), animalAtual.getFotos()))
                .disponivel(Objects.requireNonNullElse(updateDTO.disponivel(), animalAtual.getDisponivel()))
                .build();
    }
}
