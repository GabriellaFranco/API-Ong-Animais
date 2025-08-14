package com.enterprise.ongpet.service;

import com.enterprise.ongpet.enums.Especie;
import com.enterprise.ongpet.enums.Genero;
import com.enterprise.ongpet.enums.PorteAnimal;
import com.enterprise.ongpet.exception.BusinessException;
import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.mapper.AnimalMapper;
import com.enterprise.ongpet.model.dto.animal.AnimalRequestDTO;
import com.enterprise.ongpet.model.dto.animal.AnimalResponseDTO;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.Usuario;
import com.enterprise.ongpet.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @Mock
    private AnimalMapper animalMapper;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AnimalService animalService;

    private Animal animal;
    private AnimalRequestDTO animalRequestDTO;
    private AnimalResponseDTO animalResponseDTO;

    @BeforeEach()
    void setup() {

        animal = Animal.builder()
                .id(1L)
                .nome("Bolinha")
                .especie(Especie.CANINO)
                .idade(1L)
                .porte(PorteAnimal.GRANDE)
                .genero(Genero.MASCULINO)
                .descricao("Grandão porém extremamente dócil")
                .disponivel(true)
                .responsavel(new Usuario())
                .fotos(Collections.emptyList())
                .build();

        animalRequestDTO = AnimalRequestDTO.builder()
                .nome("Bolinha")
                .especie(Especie.CANINO)
                .idade(1L)
                .porte(PorteAnimal.GRANDE)
                .genero(Genero.MASCULINO)
                .descricao("Grandão porém extremamente dócil")
                .fotos(Collections.emptyList())
                .build();

        animalResponseDTO = AnimalResponseDTO.builder()
                .id(1L)
                .nome("Bolinha")
                .especie(Especie.CANINO)
                .idade(1L)
                .porte(PorteAnimal.GRANDE)
                .genero(Genero.MASCULINO)
                .descricao("Grandão porém extremamente dócil")
                .disponivel(true)
                .responsavel(AnimalResponseDTO.UsuarioDTO.builder()
                        .id(1L)
                        .nome("Gabriella")
                        .build())
                .fotos(Collections.emptyList())
                .build();
    }

    @Test
    void getAllAnimais_quandoMetodoEChamado_deveRetornarPaginaDeAnimais() {
        var pageable = PageRequest.of(0, 10);
        List<Animal> listaAnimais = List.of(animal);
        Page<Animal> paginaAnimais = new PageImpl<>(listaAnimais, pageable, listaAnimais.size());

        when(animalRepository.findAll(pageable)).thenReturn(paginaAnimais);
        when(animalMapper.toAnimalResponseDTO(animal)).thenReturn(animalResponseDTO);

        var animais = animalService.getAllAnimais(pageable);

        assertThat(animais)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getAnimalById_quandoAnimalExistir_deveRetornarAnimal() {
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.of(animal));
        when(animalMapper.toAnimalResponseDTO(animal)).thenReturn(animalResponseDTO);

        var animal = animalService.getAnimalById(1L);

        assertThat(animal)
                .isNotNull()
                .extracting(AnimalResponseDTO::nome, AnimalResponseDTO::idade)
                .containsExactly("Bolinha", 1L);
    }

    @Test
    void getAnimalById_quandoAnimalNaoExistir_DeveLancarExcecao() {
        when(animalRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> animalService.getAnimalById(5L));
    }

    @Test
    void createAnimal_quandoMetodoEChamado_deveCriarESalvarAnimal() {
        var usuarioLogado = usuarioService.getUsuarioLogado();

        when(animalMapper.toAnimal(animalRequestDTO, usuarioLogado)).thenReturn(animal);
        when(animalRepository.save(animal)).thenReturn(animal);
        when(animalMapper.toAnimalResponseDTO(animal)).thenReturn(animalResponseDTO);

        var animalCriado = animalService.createAnimal(animalRequestDTO);

        assertThat(animalCriado)
                .isNotNull()
                .extracting(AnimalResponseDTO::nome, AnimalResponseDTO::idade)
                .containsExactly("Bolinha", 1L);
    }

    @Test
    void createAnimal_quandoTentarCriarRegistroRepetido_deveLancarExcecao() {
        var usuarioLogado = usuarioService.getUsuarioLogado();

        when(animalRepository.existsByNomeAndEspecieAndResponsavel(
                animalRequestDTO.nome(), animalRequestDTO.especie(), usuarioLogado)).thenReturn(true);

        assertThrows(BusinessException.class, () -> animalService.createAnimal(animalRequestDTO));
        verify(animalRepository, never()).save(any());
    }

    @Test
    void deleteAnimal_quandoAnimalExistir_deveDeletarComSucesso() {
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.of(animal));
        animalService.deleteAnimal(animal.getId());

        verify(animalRepository).findById(animal.getId());
        verify(animalRepository).delete(animal);
    }

    @Test
    void deleteAnimal_quandoAnimalNaoExistir_DeveLancarExcecao() {
        when(animalRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> animalService.deleteAnimal(5L));
    }

    @Test
    void deleteAnimal_quandoAnimalTiverStatusAdotado_DeveLancarExcecao() {
        animal.setDisponivel(false);
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.of(animal));
        assertThrows(BusinessException.class, () -> animalService.deleteAnimal(animal.getId()));
    }
}

