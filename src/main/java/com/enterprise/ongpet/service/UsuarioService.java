package com.enterprise.ongpet.service;

import com.enterprise.ongpet.enums.PerfilUsuario;
import com.enterprise.ongpet.enums.TipoAutoridade;
import com.enterprise.ongpet.exception.BusinessException;
import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.mapper.UsuarioMapper;
import com.enterprise.ongpet.model.dto.usuario.UsuarioRequestDTO;
import com.enterprise.ongpet.model.dto.usuario.UsuarioResponseDTO;
import com.enterprise.ongpet.model.dto.usuario.UsuarioUpdateDTO;
import com.enterprise.ongpet.model.entity.Autoridade;
import com.enterprise.ongpet.model.entity.Usuario;
import com.enterprise.ongpet.repository.AuthRepository;
import com.enterprise.ongpet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UsuarioResponseDTO> getAllUsuarios(Pageable pageable) {
        var usuarios = usuarioRepository.findAll(pageable);
        return usuarios.map(usuarioMapper::toUsuarioResponseDTO);
    }

    public UsuarioResponseDTO getUsuarioById(Long id) {
        return usuarioRepository.findById(id).map(usuarioMapper::toUsuarioResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    @Transactional
    public UsuarioResponseDTO createUsuario(UsuarioRequestDTO usuarioDTO) {
        validarEmail(usuarioDTO.email(), null);
        var usuarioMapeado = usuarioMapper.toUsuario(usuarioDTO);
        var autoridadePadrao = authRepository.findByName(TipoAutoridade.ROLE_PADRAO.name())
                .orElseThrow(() -> new ResourceNotFoundException("Autoridade não encontrada"));

        usuarioMapeado.setPerfil(PerfilUsuario.PADRAO);
        usuarioMapeado.setAutoridades(List.of(autoridadePadrao));
        usuarioMapeado.setSenha(passwordEncoder.encode(usuarioMapeado.getSenha()));

        var usuarioSalvo = usuarioRepository.save(usuarioMapeado);
        return usuarioMapper.toUsuarioResponseDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioResponseDTO updateUsuario(Long id, UsuarioUpdateDTO updateDTO) {
        validarEmail(updateDTO.email(), id);
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));

        usuarioMapper.updateFromDTO(updateDTO, usuario);
        var usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toUsuarioResponseDTO(usuarioSalvo);
    }

    @Transactional
    public void deleteUsuario(Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
        usuarioRepository.delete(usuario);
    }

    private void validarEmail(String email, Long id) {
        usuarioRepository.findByEmail(email)
                .filter(usuario -> !usuario.getId().equals(id))
                .ifPresent(usuario -> {
                    throw new BusinessException("Email já cadastrado: " + email);
                });
    }

    protected Usuario getUsuarioLogado() {
        var usuario = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(usuario instanceof UserDetails userDetails)) {
            throw new BadCredentialsException("Usuário não autenticado");
        }
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userDetails.getUsername()));
    }

}
