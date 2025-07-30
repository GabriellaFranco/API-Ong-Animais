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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JWTTokenValidator extends OncePerRequestFilter {

    private static final Key SIGNIN_KEY = Keys.hmacShaKeyFor(
            ApplicationConstants.JWT_SECRET_DEFAULT_VALUE.getBytes(StandardCharsets.UTF_8)
    );

    private static final JwtParser JWT_PARSER = Jwts
            .parserBuilder()
            .setSigningKey(SIGNIN_KEY)
            .build();

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
            Claims claims = JWT_PARSER.parseClaimsJws(token).getBody();
            String username = claims.getSubject();

            List<SimpleGrantedAuthority> authorities = Collections.emptyList();

            Object authClaim = claims.get("authorities");
            if (authClaim instanceof List<?>) {
                authorities = ((List<?>) authClaim).stream()
                        .map(role -> new SimpleGrantedAuthority(role.toString()))
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
            log.error("JWT token inválido: {}", e.getMessage());
        }


        filterChain.doFilter(request, response);
    }

}
