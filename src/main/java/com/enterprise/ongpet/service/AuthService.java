package com.enterprise.ongpet.service;

import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.model.dto.auth.AtualizarPerfilEAutoridadesDTO;
import com.enterprise.ongpet.model.dto.auth.UpdateSenhaDTO;
import com.enterprise.ongpet.repository.AuthRepository;
import com.enterprise.ongpet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthRepository authRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateSenha(UpdateSenhaDTO dto) {
        var usuario = usuarioService.getUsuarioLogado();
        Optional.ofNullable(dto.novaSenha())
                .filter(senha -> !senha.isBlank())
                .ifPresent(senha -> usuario.setSenha(passwordEncoder.encode(senha)));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void atualizarPerfilEAutoridade(Long idUsuario, AtualizarPerfilEAutoridadesDTO dto) {
        var usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + idUsuario));

        Optional.ofNullable(dto.novoPerfil())
                .ifPresent(usuario::setPerfil);

        Optional.ofNullable(dto.novasAutoridades())
                .filter(autoridades -> !autoridades.isEmpty())
                .ifPresent(autoridades -> {
                    var roles = authRepository.findByNameIn(autoridades);
                    usuario.setAutoridades(roles);
                });

        usuarioRepository.save(usuario);
    }

}
