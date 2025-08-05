package com.enterprise.ongpet.service;

import com.enterprise.ongpet.exception.BusinessException;
import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.mapper.AnimalMapper;
import com.enterprise.ongpet.model.dto.animal.AnimalRequestDTO;
import com.enterprise.ongpet.model.dto.animal.AnimalResponseDTO;
import com.enterprise.ongpet.model.dto.animal.AnimalUpdateDTO;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.Usuario;
import com.enterprise.ongpet.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final UsuarioService usuarioService;

    public Page<AnimalResponseDTO> getAllAnimais(Pageable pageable) {
        var animais = animalRepository.findAll(pageable);
        return animais.map(animalMapper::toAnimalResponseDTO);
    }

    public AnimalResponseDTO getAnimalById(Long id) {
        return animalRepository.findById(id).map(animalMapper::toAnimalResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado: " + id));
    }

    @Transactional
    public AnimalResponseDTO createAnimal(AnimalRequestDTO animalDTO) {
        var usuarioLogado = usuarioService.getUsuarioLogado();
        validarRegistroUnico(animalDTO, usuarioLogado);

        var animalMapeado = animalMapper.toAnimal(animalDTO, usuarioLogado);
        var animalSalvo = animalRepository.save(animalMapeado);

        return animalMapper.toAnimalResponseDTO(animalSalvo);
    }

    @Transactional
    public AnimalResponseDTO updateAnimal(Long id, AnimalUpdateDTO updateDTO) {
        var animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado: " + id));

        validarAnimalNaoAdotado(animal);
        animalMapper.updateFromDTO(updateDTO, animal);
        var animalSalvo = animalRepository.save(animal);

        return animalMapper.toAnimalResponseDTO(animalSalvo);
    }

    @Transactional
    public void deleteAnimal(Long id) {
        var animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado: " + id));
        validarAnimalNaoAdotado(animal);
        animalRepository.delete(animal);
    }

    private void validarRegistroUnico(AnimalRequestDTO animalDTO, Usuario usuario) {
        var animalExiste = animalRepository.existsByNomeAndEspecieAndUsuario(animalDTO.nome(), animalDTO.especie(), usuario);
        if (animalExiste) {
            throw new BusinessException("Já existe um animal com esse nome e espécie para este usuário.");
        }
    }

    private void validarAnimalNaoAdotado(Animal animal) {
        if (!animal.getDisponivel()) {
            throw new BusinessException("Não é possível editar um animal que já foi adotado.");
        }
    }

}
