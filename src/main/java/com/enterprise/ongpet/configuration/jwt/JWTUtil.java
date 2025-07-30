package com.enterprise.ongpet.configuration.jwt;

import com.enterprise.ongpet.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Getter
public class JWTUtil {

    @Value("${" + ApplicationConstants.JWT_SECRET_KEY + ":" + ApplicationConstants.JWT_SECRET_DEFAULT_VALUE + "}")
    private String secret;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String usuario, String authorities) {
        return Jwts.builder()
                .setIssuer("ong-pet")
                .setSubject(usuario)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 60))
                .signWith(secretKey)
                .compact();
    }
}
