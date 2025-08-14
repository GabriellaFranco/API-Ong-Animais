package com.enterprise.ongpet.controller;

import com.enterprise.ongpet.enums.StatusAdocao;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoRequestDTO;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoResponseDTO;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoUpdateDTO;
import com.enterprise.ongpet.service.PedidoAdocaoService;
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
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos-adocao")
public class PedidoAdocaoController {

    private final PedidoAdocaoService pedidoAdocaoService;

    @Operation(
            summary = "Retorna todos os pedidos de adoção, em páginas com 10 objetos ordenados por id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum registro a exibir")
            }
    )
    @GetMapping
    public ResponseEntity<Page<PedidoAdocaoResponseDTO>> getAllPedidosAdocao(@PageableDefault(page = 1, size = 10, sort = "id")
                                                                             Pageable pageable) {

        var pedidos = pedidoAdocaoService.getAllPedidosAdocao(pageable);
        return pedidos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedidos);
    }

    @Operation(
            summary = "Retorna um pedido de adoção com o id informado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PedidoAdocaoResponseDTO> getPedidoAdocaoById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoAdocaoService.getPedidoAdocaoById(id));
    }

    @Operation(
            summary = "Cria um novo pedido de adoção",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucesso"),
                    @ApiResponse(responseCode = "400", description = "Informações inválidas")
            }
    )
    @PostMapping
    public ResponseEntity<PedidoAdocaoResponseDTO> createPedidoAdocao(@Valid @RequestBody PedidoAdocaoRequestDTO pedidoDTO) {
        var pedido = pedidoAdocaoService.createPedidoAdocao(pedidoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pedido.id()).toUri();
        return ResponseEntity.created(uri).body(pedido);
    }

    @Operation(
            summary = "Localiza o pedido com o id informado e atualiza seu status. Para chamar este endpoint" +
                    " é necessário possuir a autoridade/role 'VOLUNTARIO'",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucesso"),
                    @ApiResponse(responseCode = "400", description = "Informações inválidas"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão"),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
            }
    )
    @PatchMapping("/analise/{id}")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public ResponseEntity<PedidoAdocaoResponseDTO> updateStatusPedidoAdocao(@PathVariable Long id, @Valid @RequestBody
                                                                            PedidoAdocaoUpdateDTO updateDTO) {

        var pedido = pedidoAdocaoService.updateStatusPedidoAdocao(id, updateDTO);
        return ResponseEntity.ok(pedido);
    }

    @Operation(
            summary = "Exclui o pedido de adoção com o id informado",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
            }
    )    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePedidoAdocao(@PathVariable Long id) {
        pedidoAdocaoService.deletePedidoAdocao(id);
        return ResponseEntity.ok("Pedido excluído com sucesso: " + id);
    }

    @Operation(
            summary = "Retorna todas os pedidos de adoção que combinam com os parâmetros inseridos (um ou todos) ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum registro a exibir")
            }
    )
    @GetMapping("/results")
    public ResponseEntity<Page<PedidoAdocaoResponseDTO>> getPedidosAdocaoByFilters(@RequestParam(required = false) StatusAdocao status,
                                                                                   @RequestParam(required = false) LocalDate dataPedido,
                                                                                   @RequestParam(required = false) String adotante,
                                                                                   @RequestParam(required = false) String voluntarioResponsavel,
                                                                                   @PageableDefault(page = 1, size = 10, sort = "id")
                                                                                   Pageable pageable) {

        var pedidos = pedidoAdocaoService.getPedidosByFilters(status, dataPedido, adotante, voluntarioResponsavel, pageable);
        return pedidos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pedidos);
    }

}
