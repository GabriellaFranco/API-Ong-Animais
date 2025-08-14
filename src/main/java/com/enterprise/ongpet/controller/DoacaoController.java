package com.enterprise.ongpet.controller;

import com.enterprise.ongpet.model.dto.doacao.DoacaoRequestDTO;
import com.enterprise.ongpet.model.dto.doacao.DoacaoResponseDTO;
import com.enterprise.ongpet.service.DoacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doacoes")
public class DoacaoController {

    private final DoacaoService doacaoService;

    @Operation(
            summary = "Retorna todas as doações, em páginas com 10 objetos ordenados por id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum registro a exibir")
            }
    )
    @GetMapping
    public ResponseEntity<Page<DoacaoResponseDTO>> getAllDoacoes(@PageableDefault(page = 1, size = 10, sort = "id") Pageable pageable) {
        var doacoes = doacaoService.getAllDoacoes(pageable);
        return doacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(doacoes);
    }

    @Operation(
            summary = "Retorna uma doação com o id informado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "404", description = "Doação não encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DoacaoResponseDTO> getDoacaoById(@PathVariable Long id) {
        return ResponseEntity.ok(doacaoService.getDoacaoById(id));
    }

    @Operation(
            summary = "Cria uma nova doação",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucesso"),
                    @ApiResponse(responseCode = "400", description = "Informações inválidas")
            }
    )
    @PostMapping
    public ResponseEntity<DoacaoResponseDTO> createDoacao(@Valid @RequestBody DoacaoRequestDTO doacaoDTO) {
        var doacao = doacaoService.createDoacao(doacaoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(doacao.id()).toUri();
        return ResponseEntity.created(uri).body(doacao);
    }

    @Operation(
            summary = "Exclui a doação com o id informado",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucesso"),
                    @ApiResponse(responseCode = "404", description = "Doação não encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoacao(@PathVariable Long id) {
        doacaoService.deleteDoacao(id);
        return ResponseEntity.ok("Doação excluída com sucesso: " + id);
    }

    @Operation(
            summary = "Retorna todas as doações que combinam com os parâmetros inseridos (um ou todos) ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum registro a exibir")
            }
    )
    @GetMapping("/results")
    public ResponseEntity<Page<DoacaoResponseDTO>> getDoacoesByFilters(@RequestParam(required = false) String doador,
                                                                       @RequestParam(required = false) LocalDate data,
                                                                       @RequestParam(required = false) BigDecimal valor,
                                                                       @PageableDefault(page = 1, size = 10, sort = "id")
                                                                           Pageable pageable) {

        var doacoes = doacaoService.getDoacoesByFilters(doador, data, valor, pageable);
        return doacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(doacoes);
    }
}
