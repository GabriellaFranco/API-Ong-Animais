package com.enterprise.ongpet.configuration.jwt;

import com.enterprise.ongpet.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTTokenValidator extends OncePerRequestFilter {

    private final JwtParser parser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        try {
            Claims claims = parser.parseClaimsJws(token).getBody();
            String username = claims.getSubject();

            List<SimpleGrantedAuthority> authorities = Collections.emptyList();

            Object authClaim = claims.get("authorities");
            if (authClaim instanceof List<?>) {
                authorities = ((List<?>) authClaim).stream()
                        .map(role -> {
                            String roleName = role.toString();
                            if (!roleName.startsWith("ROLE_")) {
                                roleName = "ROLE_" + roleName;
                            }
                            return new SimpleGrantedAuthority(roleName);
                        })
                        .collect(Collectors.toList());
            }

            if (username != null) {
                var authentication = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.error("JWT token inválido: " + e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}
