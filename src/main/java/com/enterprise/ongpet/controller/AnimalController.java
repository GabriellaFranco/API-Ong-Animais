package com.enterprise.ongpet.controller;

import com.enterprise.ongpet.enums.Especie;
import com.enterprise.ongpet.enums.Genero;
import com.enterprise.ongpet.enums.PorteAnimal;
import com.enterprise.ongpet.model.dto.animal.AnimalRequestDTO;
import com.enterprise.ongpet.model.dto.animal.AnimalResponseDTO;
import com.enterprise.ongpet.model.dto.animal.AnimalUpdateDTO;
import com.enterprise.ongpet.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService animalService;


    @Operation(
            summary = "Retorna todos os animais, em páginas com 10 objetos ordenados por id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum registro a exibir")
            }
    )
    @GetMapping
    public ResponseEntity<Page<AnimalResponseDTO>> getAllAnimais(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        var animais = animalService.getAllAnimais(pageable);
        return animais.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(animais);
    }

    @Operation(
            summary = "Retorna um animal com o id informado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> getAnimalById(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.getAnimalById(id));
    }

    @Operation(
            summary = "Cria um novo animal",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucesso"),
                    @ApiResponse(responseCode = "400", description = "Informações inválidas")
            }
    )
    @PostMapping
    public ResponseEntity<AnimalResponseDTO> createAnimal(@Valid @RequestBody AnimalRequestDTO animalDTO) {
        var animal = animalService.createAnimal(animalDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(animal.id()).toUri();
        return ResponseEntity.created(uri).body(animal);
    }

    @Operation(
            summary = "Localiza o animal com o id informado e atualiza as informações necessárias. Para chamar este endpoint" +
                    "é necessário possuir a autoridade/role 'VOLUNTARIO'",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucesso"),
                    @ApiResponse(responseCode = "400", description = "Informações inválidas"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasHole('VOLUNTARIO')")
    public ResponseEntity<AnimalResponseDTO> updateAnimal (@PathVariable Long id, @Valid @RequestBody AnimalUpdateDTO animalDTO) {
        return ResponseEntity.ok(animalService.updateAnimal(id, animalDTO));
    }

    @Operation(
            summary = "Exclui o animal com o id informado",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.ok("Animal excluído com sucesso: " + id);
    }

    @Operation(
            summary = "Retorna todos os animais que combinam com os parâmetros inseridos (um ou todos) ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum registro a exibir")
            }
    )
    @GetMapping("/results")
    public ResponseEntity<Page<AnimalResponseDTO>> getAnimalsByFilter(@RequestPart(required = false) String nome,
                                                                      @RequestPart(required = false) Especie especie,
                                                                      @RequestPart(required = false) Genero genero,
                                                                      @RequestPart(required = false) PorteAnimal porte,
                                                                      @RequestPart(required = false) Boolean disponivel,
                                                                      @PageableDefault(page = 0, size = 10)
                                                                      Pageable pageable) {

        var animais = animalService.getAnimalsByFilter(nome, especie, porte, genero, disponivel,pageable);
        return animais.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(animais);
    }
}
