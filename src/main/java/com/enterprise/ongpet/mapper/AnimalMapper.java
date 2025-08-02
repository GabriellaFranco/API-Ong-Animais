package com.enterprise.ongpet.mapper;

import com.enterprise.ongpet.model.dto.animal.AnimalRequestDTO;
import com.enterprise.ongpet.model.dto.animal.AnimalResponseDTO;
import com.enterprise.ongpet.model.dto.animal.AnimalUpdateDTO;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

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

    public void updateFromDTO(AnimalUpdateDTO updateDTO, Animal animalAtual) {
        Optional.ofNullable(updateDTO.descricao()).ifPresent(animalAtual::setDescricao);
        Optional.ofNullable(updateDTO.fotos()).ifPresent(animalAtual::setFotos);
        Optional.ofNullable(updateDTO.disponivel()).ifPresent(animalAtual::setDisponivel);
    }
}
