package com.trajestipicos.indira.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/catalogo",
                                "/detalle",
                                "/login",
                                "/registro",

                                "/css/**",
                                "/javascript/**",
                                "/images/**",

                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs",
                                "/api-docs/**",

                                "/api/auth/**",
                                "/api/vestidos",
                                "/api/vestidos/**"
                        ).permitAll()

                        .requestMatchers(
                                "/admin",
                                "/ver-productos",
                                "/crear-producto",
                                "/historial-ventas",
                                "/api/admin/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/carrito",
                                "/historial",
                                "/pago",
                                "/api/carrito/**",
                                "/api/ventas/**",
                                "/api/pago/**"
                        ).hasRole("CLIENTE")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}