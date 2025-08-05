package com.enterprise.ongpet.service;

import com.enterprise.ongpet.exception.BusinessException;
import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.mapper.DoacaoMapper;
import com.enterprise.ongpet.model.dto.doacao.DoacaoRequestDTO;
import com.enterprise.ongpet.model.dto.doacao.DoacaoResponseDTO;
import com.enterprise.ongpet.model.entity.Doacao;
import com.enterprise.ongpet.model.entity.Usuario;
import com.enterprise.ongpet.repository.DoacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoacaoServiceTest {

    @Mock
    private DoacaoRepository doacaoRepository;

    @Mock
    private DoacaoMapper doacaoMapper;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private DoacaoService doacaoService;

    private Doacao doacao;
    private DoacaoRequestDTO doacaoRequestDTO;
    private DoacaoResponseDTO doacaoResponseDTO;

    @BeforeEach
    void setup() {

        doacao = Doacao.builder()
                .id(1L)
                .valor(new BigDecimal(100))
                .data(LocalDate.now())
                .doador(new Usuario())
                .build();

        doacaoRequestDTO = DoacaoRequestDTO.builder()
                .valor(new BigDecimal(100))
                .build();

        doacaoResponseDTO = DoacaoResponseDTO.builder()
                .id(1L)
                .valor(new BigDecimal(100))
                .doador(DoacaoResponseDTO.UsuarioDTO.builder()
                        .id(1L)
                        .nome("Gabriella")
                        .build())
                .build();
    }

    @Test
    void getAllDoacoes_quandoMetodoEChamado_deveRetornarPaginaDeDoacoes() {
        var pageable = PageRequest.of(0, 10);
        List<Doacao> listaDoacoes = List.of(doacao);
        Page<Doacao> paginaDoacao = new PageImpl<>(listaDoacoes, pageable, listaDoacoes.size());

        when(doacaoRepository.findAll(pageable)).thenReturn(paginaDoacao);
        when(doacaoMapper.toDoacaoResponseDTO(doacao)).thenReturn(doacaoResponseDTO);

        var doacoes = doacaoService.getAllDoacoes(pageable);

        assertThat(doacoes)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    void getDoacaoById_quandoDoacaoExistir_deveRetornarDoacao() {
        when(doacaoRepository.findById(1L)).thenReturn(Optional.of(doacao));
        when(doacaoMapper.toDoacaoResponseDTO(doacao)).thenReturn(doacaoResponseDTO);

        var doacao = doacaoService.getDoacaoById(1L);

        assertThat(doacao)
                .isNotNull()
                .extracting(DoacaoResponseDTO::valor, DoacaoResponseDTO::id)
                .containsExactly(BigDecimal.valueOf(100), 1L);
    }

    @Test
    void getDoacaoById_quandoDoacaoNaoExistir_deveLancarExcecao() {
        when(doacaoRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> doacaoService.getDoacaoById(5L));
    }

    @Test
    void createDoacao_quandoMetodoEChamado_DeveCriarESalvarDoacao() {
        var usuarioLogado = usuarioService.getUsuarioLogado();
        when(doacaoMapper.toDoacao(doacaoRequestDTO, usuarioLogado)).thenReturn(doacao);
        when(doacaoRepository.save(doacao)).thenReturn(doacao);
        when(doacaoMapper.toDoacaoResponseDTO(doacao)).thenReturn(doacaoResponseDTO);

        var doacaoSalva = doacaoService.createDoacao(doacaoRequestDTO);

        assertThat(doacaoSalva)
                .isNotNull()
                .extracting(DoacaoResponseDTO::valor, DoacaoResponseDTO::id)
                .containsExactly(BigDecimal.valueOf(100), 1L);
    }

    @Test
    void createDoacao_quandoTentarCriarDoacaoDeMesmoValorComMenosDe1MinutoDeIntervalo_deveLancarExcecao() {
        var usuarioLogado = usuarioService.getUsuarioLogado();
        when(usuarioService.getUsuarioLogado()).thenReturn(usuarioLogado);
        when(doacaoRepository.existsByUsuarioAndDataCriacaoBetween(
                Mockito.eq(usuarioLogado),
                Mockito.<LocalDateTime>any(),
                Mockito.<LocalDateTime>any()
        )).thenReturn(true);

        assertThrows(BusinessException.class, () -> doacaoService.createDoacao(doacaoRequestDTO));
    }

    @Test
    void createDoacao_quandoValorForInferiorAUmReal_deveLancarExcecao() {
        DoacaoRequestDTO doacaoIncorreta = new DoacaoRequestDTO(new BigDecimal("0.50"));
        assertThrows(BusinessException.class, () -> doacaoService.createDoacao(doacaoIncorreta));
    }
}
