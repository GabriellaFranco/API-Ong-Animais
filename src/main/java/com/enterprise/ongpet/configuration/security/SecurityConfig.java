package com.enterprise.ongpet.configuration.security;

import com.enterprise.ongpet.configuration.jwt.JWTTokenValidator;
import com.enterprise.ongpet.exception.CustomBasicAuthEntryPoint;
import com.enterprise.ongpet.filter.PerformanceLoggingFilter;
import com.enterprise.ongpet.filter.RequestLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final JWTTokenValidator jwtTokenValidator;
    private final RequestLoggingFilter requestLoggingFilter;
    private final PerformanceLoggingFilter performanceLoggingFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenValidator, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(requestLoggingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(performanceLoggingFilter, UsernamePasswordAuthenticationFilter.class);

        http.formLogin(Customizer.withDefaults());
        http.httpBasic(sbc -> sbc.authenticationEntryPoint(new CustomBasicAuthEntryPoint()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
