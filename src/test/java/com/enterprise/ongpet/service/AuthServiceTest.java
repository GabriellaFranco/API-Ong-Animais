package com.enterprise.ongpet.service;

import com.enterprise.ongpet.enums.PerfilUsuario;
import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.model.dto.auth.AtualizarPerfilEAutoridadesDTO;
import com.enterprise.ongpet.model.dto.auth.UpdateSenhaDTO;
import com.enterprise.ongpet.model.entity.Autoridade;
import com.enterprise.ongpet.model.entity.Usuario;
import com.enterprise.ongpet.repository.AuthRepository;
import com.enterprise.ongpet.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Usuario usuarioLogado;
    private Usuario usuarioBanco;

    @BeforeEach
    void setUp() {
        usuarioLogado = new Usuario();
        usuarioLogado.setId(1L);

        usuarioBanco = new Usuario();
        usuarioBanco.setId(2L);
    }

    @Test
    void updateSenha_quandoSenhaNovaValida_deveAtualizarESalvar() {
        var updateSenhaDTO = new UpdateSenhaDTO("senhaAtual123", "novaSenha123");
        when(usuarioService.getUsuarioLogado()).thenReturn(usuarioLogado);
        when(passwordEncoder.encode("novaSenha123")).thenReturn("senhaCriptografada");

        authService.updateSenha(updateSenhaDTO);

        assertEquals("senhaCriptografada", usuarioLogado.getSenha());
        verify(usuarioRepository).save(usuarioLogado);
    }

    @Test
    void updateSenha_quandoSenhaNovaNula_naoDeveAlterarSenha() {
        UpdateSenhaDTO dto = new UpdateSenhaDTO("senhaAtual123", null);
        when(usuarioService.getUsuarioLogado()).thenReturn(usuarioLogado);

        authService.updateSenha(dto);

        assertEquals("senhaAtual123", usuarioLogado.getSenha());
        verify(usuarioRepository).save(usuarioLogado);
    }

    @Test
    void atualizarPerfilEAutoridade_quandoUsuarioExiste_deveAtualizarPerfilEAutoridades() {
        var dto = new AtualizarPerfilEAutoridadesDTO(
                PerfilUsuario.VOLUNTARIO,
                List.of("ROLE_PADRAO", "ROLE_VOLUNTARIO")
        );

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuarioBanco));
        when(authRepository.findByNameIn(dto.novasAutoridades())).thenReturn(List.of(
                new Autoridade("ROLE_ADMIN"),
                new Autoridade("ROLE_USER")
        ));

        authService.atualizarPerfilEAutoridade(2L, dto);

        assertEquals(PerfilUsuario.VOLUNTARIO, usuarioBanco.getPerfil());
        assertEquals(2, usuarioBanco.getAutoridades().size());
        verify(usuarioRepository).save(usuarioBanco);
    }

    @Test
    void atualizarPerfilEAutoridade_quandoUsuarioNaoExistir_deveLancarExcecao() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        var dto = new AtualizarPerfilEAutoridadesDTO(PerfilUsuario.ADMIN, List.of());
        assertThrows(ResourceNotFoundException.class, () -> authService.atualizarPerfilEAutoridade(999L, dto));
    }


}
