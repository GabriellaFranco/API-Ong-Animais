package com.enterprise.ongpet.configuration.security;

import com.enterprise.ongpet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        List<SimpleGrantedAuthority> autoridades = usuario.getAutoridades().stream()
                .map(autoridade -> new SimpleGrantedAuthority(autoridade.getName())).toList();
        return new User(usuario.getEmail(), usuario.getSenha(), autoridades);
    }
}
