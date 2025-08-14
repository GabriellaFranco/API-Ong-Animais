package com.enterprise.ongpet.service;

import com.enterprise.ongpet.exception.BusinessException;
import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.mapper.DoacaoMapper;
import com.enterprise.ongpet.model.dto.doacao.DoacaoRequestDTO;
import com.enterprise.ongpet.model.dto.doacao.DoacaoResponseDTO;
import com.enterprise.ongpet.model.entity.Doacao;
import com.enterprise.ongpet.model.entity.Usuario;
import com.enterprise.ongpet.repository.DoacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;
    private final DoacaoMapper doacaoMapper;
    private final UsuarioService usuarioService;
    private static final BigDecimal VALOR_MINIMO_DOACAO = new BigDecimal("1.00");

    public Page<DoacaoResponseDTO> getAllDoacoes(Pageable pageable) {
        var doacoes = doacaoRepository.findAll(pageable);
        return doacoes.map(doacaoMapper::toDoacaoResponseDTO);
    }

    public Page<DoacaoResponseDTO> getDoacoesByFilters(String doador, LocalDate data, BigDecimal valor, Pageable pageable) {
        var doacoes = doacaoRepository.findByFilters(doador, data, valor, pageable);
        return doacoes.map(doacaoMapper::toDoacaoResponseDTO);
    }

    public DoacaoResponseDTO getDoacaoById(Long id) {
        return doacaoRepository.findById(id).map(doacaoMapper::toDoacaoResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Doação não encontrada: " + id));
    }

    @Transactional
    public DoacaoResponseDTO createDoacao(DoacaoRequestDTO doacaoDTO) {
        var usuarioLogado = usuarioService.getUsuarioLogado();
        validarValorMinimo(doacaoDTO);
        validarIntervaloMinimoEntreDoacoes(usuarioLogado);
        var doacaoMapeada = doacaoMapper.toDoacao(doacaoDTO, usuarioLogado);
        var doacaoSalva = doacaoRepository.save(doacaoMapeada);

        return doacaoMapper.toDoacaoResponseDTO(doacaoSalva);
    }

    @Transactional
    public void deleteDoacao(Long id) {
        var doacao = doacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doação não encontrada: " + id));
        validarPermissaoParaExcluir(doacao);
        doacaoRepository.delete(doacao);
    }

    private void validarPermissaoParaExcluir(Doacao doacao) {
        if (!doacao.getDoador().equals(usuarioService.getUsuarioLogado())) {
            throw new BusinessException("Você não tem permissão para excluir esta doação");
        }
    }

    private void validarIntervaloMinimoEntreDoacoes(Usuario usuario) {
        var agora = LocalDateTime.now();
        var inicio = agora.minusMinutes(1);
        boolean existe = doacaoRepository.existsByDoadorAndDataBetween(usuario, inicio, agora);

        if (existe) {
            throw new BusinessException("Uma doação já foi registrada recentemente, aguarde 1 minuto e tente novamente");
        }
    }

    private void validarValorMinimo(DoacaoRequestDTO doacao) {
        if (doacao.valor() == null || doacao.valor().compareTo(VALOR_MINIMO_DOACAO) < 0) {
            throw new BusinessException("O valor mínimo da doação é de R$1,00");
        }
    }
}
