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

                        // VISTAS PÚBLICAS
                        .requestMatchers(
                                "/",
                                "/catalogo",
                                "/detalle",
                                "/login",
                                "/registro",
                                "/admin",
                                "/ver-productos",
                                "/crear-producto",
                                "/historial-ventas",
                                "/carrito",
                                "/historial",
                                "/pago",
                                "/sin-permisos"
                        ).permitAll()

                        // ARCHIVOS ESTÁTICOS
                        .requestMatchers(
                                "/css/**",
                                "/javascript/**",
                                "/images/**"
                        ).permitAll()

                        // SWAGGER
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs",
                                "/api-docs/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // AUTH
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // CATÁLOGO PÚBLICO
                        .requestMatchers(
                                "/api/vestidos",
                                "/api/vestidos/**"
                        ).permitAll()

                        // APIs SOLO ADMIN
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")

                        // APIs SOLO CLIENTE
                        .requestMatchers(
                                "/api/carrito/**",
                                "/api/pago/**"
                        ).hasRole("CLIENTE")

                        // VENTAS: cliente compra, admin consulta historial general
                        .requestMatchers(
                                "/api/ventas/comprar/**",
                                "/api/ventas/cliente/**"
                        ).hasRole("CLIENTE")

                        .requestMatchers(
                                "/api/ventas",
                                "/api/ventas/**"
                        ).hasAnyRole("ADMIN", "CLIENTE")

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