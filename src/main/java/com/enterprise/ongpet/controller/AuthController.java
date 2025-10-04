package com.enterprise.ongpet.controller;

import com.enterprise.ongpet.configuration.jwt.JWTUtil;
import com.enterprise.ongpet.model.dto.auth.AtualizarPerfilEAutoridadesDTO;
import com.enterprise.ongpet.model.dto.auth.LoginRequestDTO;
import com.enterprise.ongpet.model.dto.auth.LoginResponseDTO;
import com.enterprise.ongpet.model.dto.auth.UpdateSenhaDTO;
import com.enterprise.ongpet.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Altera a senha do usuário autenticado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou senha atual incorreta"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
            }
    )
    @PatchMapping("/alterar-senha")
    public ResponseEntity<String> updateSenha(@Valid @RequestBody UpdateSenhaDTO updateSenhaDTO) {
        authService.updateSenha(updateSenhaDTO);
        return ResponseEntity.ok("Senha alterada com sucesso");
    }

    @Operation(
            summary = "Atualiza o perfil e as autoridades de um usuário. Para chamar este endpoint é necessário possuir a" +
                    "permissão 'ADMIN'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuário autenticado sem permissão de administrador"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
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
