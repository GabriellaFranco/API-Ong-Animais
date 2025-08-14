package com.enterprise.ongpet.service;

import com.enterprise.ongpet.enums.PerfilUsuario;
import com.enterprise.ongpet.enums.StatusAdocao;
import com.enterprise.ongpet.exception.BusinessException;
import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.mapper.PedidoAdocaoMapper;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoRequestDTO;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoResponseDTO;
import com.enterprise.ongpet.model.dto.pedidoadocao.PedidoAdocaoUpdateDTO;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.PedidoAdocao;
import com.enterprise.ongpet.model.entity.Usuario;
import com.enterprise.ongpet.repository.AnimalRepository;
import com.enterprise.ongpet.repository.PedidoAdocaoRepository;
import com.enterprise.ongpet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PedidoAdocaoService {

    private final PedidoAdocaoRepository pedidoAdocaoRepository;
    private final AnimalRepository animalRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoAdocaoMapper pedidoAdocaoMapper;
    private final UsuarioService usuarioService;

    public Page<PedidoAdocaoResponseDTO> getAllPedidosAdocao(Pageable pageable) {
        var pedidos = pedidoAdocaoRepository.findAll(pageable);
        return pedidos.map(pedidoAdocaoMapper::toPedidoAdocaoResponseDTO);
    }

    public Page<PedidoAdocaoResponseDTO> getPedidosByFilters(StatusAdocao statusAdocao, LocalDate dataPedido,
                                                             String adotante, String voluntarioResponsavel, Pageable pageable) {

        var pedidos = pedidoAdocaoRepository.findByFilters(statusAdocao, dataPedido, adotante, voluntarioResponsavel, pageable);
        return pedidos.map(pedidoAdocaoMapper::toPedidoAdocaoResponseDTO);
    }

    public PedidoAdocaoResponseDTO getPedidoAdocaoById(Long id) {
        return pedidoAdocaoRepository.findById(id).map(pedidoAdocaoMapper::toPedidoAdocaoResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de Adoção não encontrado: " + id));
    }

    @Transactional
    public PedidoAdocaoResponseDTO createPedidoAdocao(PedidoAdocaoRequestDTO pedidoAdocaoDTO) {
        var adotante = usuarioService.getUsuarioLogado();
        validarLimiteDePedidosPendentesPorUsuario(adotante);
        var animal = validarDisponibilidadeAnimal(pedidoAdocaoDTO.idAnimal());
        validarDuplicidadeDePedidoParaAnimal(adotante, animal);
        var voluntarios = validarDisponibilidadeVoluntarios(PerfilUsuario.VOLUNTARIO);
        var voluntarioResponsavel = distribuicaoPedidosParaVoluntariosComMenorCarga(voluntarios);

        var pedidoMapeado = pedidoAdocaoMapper.toPedidoAdocao(pedidoAdocaoDTO, animal, adotante, voluntarioResponsavel);

        pedidoMapeado.setDataPedido(LocalDate.now());
        pedidoMapeado.setStatus(StatusAdocao.SOLICITADA);

        var pedidoSalvo = pedidoAdocaoRepository.save(pedidoMapeado);
        return pedidoAdocaoMapper.toPedidoAdocaoResponseDTO(pedidoSalvo);
    }

    @Transactional
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public PedidoAdocaoResponseDTO updateStatusPedidoAdocao(Long id, PedidoAdocaoUpdateDTO pedidoAdocaoDTO) {
        var pedidoAdocao = pedidoAdocaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de Adoção não encontrado: " + id));
        pedidoAdocaoMapper.updateFromDTO(pedidoAdocaoDTO, pedidoAdocao);
        var pedidoAdocaoSalvo = pedidoAdocaoRepository.save(pedidoAdocao);
        return pedidoAdocaoMapper.toPedidoAdocaoResponseDTO(pedidoAdocaoSalvo);
    }

    @Transactional
    public void deletePedidoAdocao(Long id) {
        var pedidoAdocao = validarSePedidoAdocaoEstaConcluido(id);
        pedidoAdocaoRepository.delete(pedidoAdocao);
    }

    private Animal validarDisponibilidadeAnimal(Long id) {
        return animalRepository.findById(id)
                .filter(Animal::getDisponivel)
                .orElseThrow(() -> new BusinessException("Animal não disponível para adoção."));
    }

    private void validarDuplicidadeDePedidoParaAnimal(Usuario adotante, Animal animal) {
        if (pedidoAdocaoRepository.existsByAdotanteAndAnimalAndStatus(adotante, animal, StatusAdocao.SOLICITADA)) {
            throw new BusinessException("Já existe um pedido de adoção pendente para este animal");
        }
    }

    private void validarLimiteDePedidosPendentesPorUsuario(Usuario usuario) {
        var pedidosPendentes = pedidoAdocaoRepository.countByAdotanteAndStatus(usuario, StatusAdocao.SOLICITADA);
        if (pedidosPendentes >= 3) {
            throw new BusinessException("Você já possui 3 pedidos de adoção pendentes. Aguarde avaliação antes de criar novos pedidos");
        }
    }

    private List<Usuario> validarDisponibilidadeVoluntarios(PerfilUsuario perfil) {
        var voluntarios = usuarioRepository.findByPerfil(perfil);
        if (voluntarios.isEmpty()) {
            throw new BusinessException("Não há voluntários disponíveis no momento para analisar o pedido de adoção.");
        }
        return voluntarios;
    }

    private Usuario distribuicaoPedidosParaVoluntariosComMenorCarga(List<Usuario> voluntarios) {
        return voluntarios.stream()
                .min(Comparator.comparingLong(v -> pedidoAdocaoRepository.countByVoluntarioResponsavelAndStatus(v, StatusAdocao.SOLICITADA)))
                .orElseThrow(() -> new BusinessException("Não há voluntários disponíveis no momento."));
    }

    private PedidoAdocao validarSePedidoAdocaoEstaConcluido(Long id) {
        var pedidoAdocao =  pedidoAdocaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de Adoção não encontrado: " + id));
        if (!pedidoAdocao.getStatus().equals(StatusAdocao.SOLICITADA)) {
            throw new BusinessException("Não é possível excluir um pedido de adoção já avaliado por um voluntário");
        }

        return pedidoAdocao;
    }
}
