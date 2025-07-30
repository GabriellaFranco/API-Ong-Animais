package com.enterprise.ongpet.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UsernamePasswordAuthProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var usuario = authentication.getName();
        var senha = authentication.getCredentials().toString();
        var userDetails = userDetailsService.loadUserByUsername(usuario);

        if (passwordEncoder.matches(senha, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(usuario, senha, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Usuário/senha inválido(s)");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
