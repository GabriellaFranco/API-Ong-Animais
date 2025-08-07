package com.enterprise.ongpet.configuration.jwt;

import com.enterprise.ongpet.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public String generateToken(String usuario, List<String> authorities) {
        List<String> rolesComPrefixo = authorities.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setIssuer("ong-pet")
                .setSubject(usuario)
                .claim("authorities", rolesComPrefixo)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 60))
                .signWith(secretKey)
                .compact();
    }

}

