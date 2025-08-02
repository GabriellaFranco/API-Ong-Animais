package com.enterprise.ongpet.service;

import com.enterprise.ongpet.enums.PerfilUsuario;
import com.enterprise.ongpet.exception.BusinessException;
import com.enterprise.ongpet.exception.ResourceNotFoundException;
import com.enterprise.ongpet.mapper.UsuarioMapper;
import com.enterprise.ongpet.model.dto.usuario.UsuarioRequestDTO;
import com.enterprise.ongpet.model.dto.usuario.UsuarioResponseDTO;
import com.enterprise.ongpet.model.dto.usuario.UsuarioUpdateDTO;
import com.enterprise.ongpet.model.entity.Usuario;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO usuarioRequestDTO;
    private UsuarioResponseDTO usuarioResponseDTO;

    @BeforeEach
    void setup() {
        usuario = Usuario.builder()
                .id(1L)
                .nome("Gabriella")
                .email("dev@teste.com")
                .cpf("11122233344")
                .cep("88888888")
                .cidade("Timbó")
                .rua("Rua Marechal Rondon")
                .bairro("Flores")
                .numEndereco(1456L)
                .perfil(PerfilUsuario.PADRAO)
                .senha("dev")
                .build();

        usuarioRequestDTO = UsuarioRequestDTO.builder()
                .nome("Liana")
                .email("dev@teste.com")
                .cpf("11122233344")
                .cep("88888888")
                .cidade("Timbó")
                .rua("Rua Marechal Rondon")
                .bairro("Flores")
                .numEndereco(1456L)
                .senha("dev")
                .build();

        usuarioResponseDTO = UsuarioResponseDTO.builder()
                .id(1L)
                .nome("Gabriella")
                .email("dev@teste.com")
                .cpf("11122233344")
                .cep("88888888")
                .cidade("Timbó")
                .rua("Rua Marechal Rondon")
                .bairro("Flores")
                .numEndereco(1456L)
                .perfil(PerfilUsuario.PADRAO)
                .build();

    }

    @Test
    void getAllUsuarios_quandoChamado_deveRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        var usuarios = usuarioService.getAllUsuarios();
        assertThat(usuarios)
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void getUsuarioById_quandoUsuarioExistir_DeveRetornarUsuario() {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toUsuarioResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        var usuario = usuarioService.getUsuarioById(1L);

        assertThat(usuario)
                .isNotNull()
                .extracting(UsuarioResponseDTO::nome, UsuarioResponseDTO::email)
                .containsExactly("Gabriella", "dev@teste.com");
    }

    @Test
    void getUsuarioById_quandoUsuarioNaoExistir_deveLancarExcecao() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.getUsuarioById(2L));
    }

    @Test
    void createUsuario_quandoChamarMetodo_deveCriarESalvarUsuario() {
        when(usuarioMapper.toUsuario(usuarioRequestDTO)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toUsuarioResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        var usuario = usuarioService.createUsuario(usuarioRequestDTO);

        assertThat(usuario)
                .isNotNull()
                .extracting(UsuarioResponseDTO::nome, UsuarioResponseDTO::cep)
                .containsExactly("Gabriella", "88888888");
    }

    @Test
    void createUsuario_quandoTentarCriarUsuarioComEmailJaCadastrado_DeveLancarExcecao() {
        when(usuarioRepository.findByEmail(usuarioRequestDTO.email())).thenReturn(Optional.ofNullable(usuario));
        assertThrows(BusinessException.class, () -> usuarioService.createUsuario(usuarioRequestDTO));
    }

    @Test
    void deleteUsuario_QuandoUsuarioExistir_DeveSerDeletado() {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        usuarioService.deleteUsuario(usuario.getId());
        verify(usuarioRepository).findById(usuario.getId());
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void deleteUsuario_QuandoUsuarioNaoExistir_DeveLancarExcecao() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.deleteUsuario(5L));
    }
}
