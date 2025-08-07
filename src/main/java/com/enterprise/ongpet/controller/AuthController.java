package com.enterprise.ongpet.controller;

import com.enterprise.ongpet.configuration.jwt.JWTUtil;
import com.enterprise.ongpet.model.dto.auth.AtualizarPerfilEAutoridadesDTO;
import com.enterprise.ongpet.model.dto.auth.LoginRequestDTO;
import com.enterprise.ongpet.model.dto.auth.LoginResponseDTO;
import com.enterprise.ongpet.model.dto.auth.UpdateSenhaDTO;
import com.enterprise.ongpet.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @PatchMapping("/alterar-senha")
    public ResponseEntity<String> updateSenha(@Valid @RequestBody UpdateSenhaDTO updateSenhaDTO) {
        //authService.updateSenha(updateSenhaDTO);
        return ResponseEntity.ok("Senha alterada com sucesso");
    }

    @PatchMapping("/usuarios/{id}/perfil-autoridades")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updatePerfilEAutoridades(@PathVariable Long id, @Valid @RequestBody AtualizarPerfilEAutoridadesDTO dto) {
        authService.atualizarPerfilEAutoridade(id, dto);
        return ResponseEntity.ok("Perfil e autoridades de usuário atualizadas com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password()
                )
        );

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(authentication.getName(), authorities);

        return ResponseEntity.ok(new LoginResponseDTO("Login successful", token));
    }
}
