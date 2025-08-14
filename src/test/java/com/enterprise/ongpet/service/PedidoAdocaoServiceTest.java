package com.enterprise.ongpet.service;

import com.enterprise.ongpet.enums.PerfilUsuario;
import com.enterprise.ongpet.enums.StatusAdocao;
import com.enterprise.ongpet.exception.BusinessException;
import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.mapper.PedidoAdocaoMapper;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoRequestDTO;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoResponseDTO;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.PedidoAdocao;
import com.enterprise.ongpet.model.entity.Usuario;
import com.enterprise.ongpet.repository.AnimalRepository;
import com.enterprise.ongpet.repository.PedidoAdocaoRepository;
import com.enterprise.ongpet.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PedidoAdocaoServiceTest {

    @Mock
    private PedidoAdocaoRepository pedidoAdocaoRepository;

    @Mock
    private PedidoAdocaoMapper pedidoAdocaoMapper;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PedidoAdocaoService pedidoAdocaoService;

    private PedidoAdocao pedidoAdocao;
    private PedidoAdocaoRequestDTO pedidoAdocaoRequestDTO;
    private PedidoAdocaoResponseDTO pedidoAdocaoResponseDTO;

    @BeforeEach
    void setup() {
        pedidoAdocao = PedidoAdocao.builder()
                .id(1L)
                .observacoes("Algumas observações estão inseridas aqui")
                .status(StatusAdocao.SOLICITADA)
                .adotante(new Usuario())
                .voluntarioResponsavel(new Usuario())
                .build();

        pedidoAdocaoRequestDTO = PedidoAdocaoRequestDTO.builder()
                .idAdotante(1L)
                .idAnimal(1L)
                .observacoes("Algumas observações estão inseridas aqui")
                .build();

        pedidoAdocaoResponseDTO = PedidoAdocaoResponseDTO.builder()
                .id(1L)
                .observacoes("Algumas observações estão inseridas aqui")
                .status(StatusAdocao.SOLICITADA)
                .dataPedido(LocalDate.now())
                .adotante(PedidoAdocaoResponseDTO.UsuarioDTO.builder()
                        .id(1L)
                        .nome("Gabriella")
                        .perfil(PerfilUsuario.PADRAO)
                        .build())
                .voluntarioResponsavel(PedidoAdocaoResponseDTO.UsuarioDTO.builder()
                        .id(2L)
                        .nome("Liana")
                        .perfil(PerfilUsuario.VOLUNTARIO)
                        .build())
                .build();
    }

    @Test
    void getAllPedidosAdocao_quandoMetodoForChamado_deveRetornarPaginaDePedidosAdocao() {
        var pageable = PageRequest.of(0, 10);
        var pedidosAdocao = List.of(pedidoAdocao);
        var paginaPedidosAdocao = new PageImpl<>(pedidosAdocao, pageable, pedidosAdocao.size());

        when(pedidoAdocaoRepository.findAll(pageable)).thenReturn(paginaPedidosAdocao);
        when(pedidoAdocaoMapper.toPedidoAdocaoResponseDTO(pedidoAdocao)).thenReturn(pedidoAdocaoResponseDTO);

        var resultado = pedidoAdocaoService.getAllPedidosAdocao(pageable);

        assertThat(resultado)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getPedidoAdocaoById_quandoPedidoExistir_deveRetornarOPedido() {
        when(pedidoAdocaoRepository.findById(1L)).thenReturn(Optional.of(pedidoAdocao));
        when(pedidoAdocaoMapper.toPedidoAdocaoResponseDTO(pedidoAdocao)).thenReturn(pedidoAdocaoResponseDTO);
        var pedidoAdocao = pedidoAdocaoService.getPedidoAdocaoById(1L);
        assertThat(pedidoAdocao)
                .isNotNull()
                .extracting(PedidoAdocaoResponseDTO::status, PedidoAdocaoResponseDTO::id)
                .containsExactly(StatusAdocao.SOLICITADA, 1L);
    }

    @Test
    void getPedidoAdocaoById_quandoPedidoNaoExistir_DeveLancarExcecao() {
        when(pedidoAdocaoRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pedidoAdocaoService.getPedidoAdocaoById(5L));
    }

    @Test
    void createPedidoAdocao_quandoMetodoForChamado_deveCriarESalvarPedido() {
        var usuarioLogado = usuarioService.getUsuarioLogado();
        var animal = new Animal();
        animal.setId(1L);
        animal.setDisponivel(true);
        var voluntarioResponsavel = new Usuario();
        voluntarioResponsavel.setId(2L);

        when(usuarioService.getUsuarioLogado()).thenReturn(usuarioLogado);
        when(animalRepository.findById(pedidoAdocaoRequestDTO.idAnimal())).thenReturn(Optional.of(animal));
        when(usuarioRepository.findByPerfil(PerfilUsuario.VOLUNTARIO)).thenReturn(List.of(voluntarioResponsavel));
        when(pedidoAdocaoMapper.toPedidoAdocao(pedidoAdocaoRequestDTO, animal ,usuarioLogado, voluntarioResponsavel)).thenReturn(pedidoAdocao);
        when(pedidoAdocaoRepository.save(pedidoAdocao)).thenReturn(pedidoAdocao);
        when(pedidoAdocaoMapper.toPedidoAdocaoResponseDTO(pedidoAdocao)).thenReturn(pedidoAdocaoResponseDTO);

        var resultado = pedidoAdocaoService.createPedidoAdocao(pedidoAdocaoRequestDTO);

        assertThat(resultado)
                .isNotNull()
                .extracting(PedidoAdocaoResponseDTO::id, PedidoAdocaoResponseDTO::status)
                .containsExactly(1L, StatusAdocao.SOLICITADA);

        verify(pedidoAdocaoRepository).save(pedidoAdocao);
    }

    @Test
    void createPedidoAdocao_quandoAnimalNaoEstiverDisponivel_deveLancarExcecao() {
        var animal = new Animal();
        animal.setDisponivel(false);
        pedidoAdocao.setAnimal(animal);

        var usuarioLogado = new Usuario();
        usuarioLogado.setId(1L);

        when(usuarioService.getUsuarioLogado()).thenReturn(usuarioLogado);
        when(animalRepository.findById(pedidoAdocaoRequestDTO.idAnimal())).thenReturn(Optional.of(animal));
        assertThrows(BusinessException.class, () -> pedidoAdocaoService.createPedidoAdocao(pedidoAdocaoRequestDTO));
    }

    @Test
    void createPedidoAdocao_quandoUsuarioJaTiverTresPedidosAguardandoAvaliacao_DeveLancarExcecao() {
        var usuarioLogado = new Usuario();
        usuarioLogado.setId(1L);

        when(usuarioService.getUsuarioLogado()).thenReturn(usuarioLogado);
        when(pedidoAdocaoRepository.countByAdotanteAndStatus(usuarioLogado, StatusAdocao.SOLICITADA)).thenReturn(3L);
        assertThrows(BusinessException.class, () -> pedidoAdocaoService.createPedidoAdocao(pedidoAdocaoRequestDTO));
    }

    @Test
    void createPedidoAdocao_quandoPedidoDuplicadoParaOMesmoAnimal_DeveLancarExcecao() {
        var usuarioLogado = new Usuario();
        usuarioLogado.setId(1L);
        var animal = new Animal();
        animal.setId(1L);
        animal.setDisponivel(true);

        when(usuarioService.getUsuarioLogado()).thenReturn(usuarioLogado);
        when(animalRepository.findById(pedidoAdocaoRequestDTO.idAnimal())).thenReturn(Optional.of(animal));
        when(pedidoAdocaoRepository.existsByAdotanteAndAnimalAndStatus(usuarioLogado, animal, StatusAdocao.SOLICITADA)).thenReturn(true);
        assertThrows(BusinessException.class, () -> pedidoAdocaoService.createPedidoAdocao(pedidoAdocaoRequestDTO));
    }

    @Test
    void deletePedidoAdocao_quandoPedidoExistir_deveDeletarComSucesso() {
        when(pedidoAdocaoRepository.findById(pedidoAdocao.getId())).thenReturn(Optional.of(pedidoAdocao));
        pedidoAdocaoService.deletePedidoAdocao(pedidoAdocao.getId());
        verify(pedidoAdocaoRepository).findById(pedidoAdocao.getId());
        verify(pedidoAdocaoRepository).delete(pedidoAdocao);
    }

    @Test
    void deletePedidoAdocao_quandoPedidoNaoExistir_deveLancarExcecao() {
        when(pedidoAdocaoRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pedidoAdocaoService.deletePedidoAdocao(5L));
    }

    @Test
    void deletePedidoAdocao_quandoTentarDeletarPedidoJaAvaliado_DeveLancarExcecao() {
        when(pedidoAdocaoRepository.findById(pedidoAdocao.getId())).thenReturn(Optional.of(pedidoAdocao));
        pedidoAdocao.setStatus(StatusAdocao.APROVADA);
        assertThrows(BusinessException.class, () -> pedidoAdocaoService.deletePedidoAdocao(pedidoAdocao.getId()));
    }
}
