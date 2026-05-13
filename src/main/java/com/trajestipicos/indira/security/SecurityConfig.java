package com.trajestipicos.indira.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/catalogo",
                                "/detalle",
                                "/login",
                                "/registro",
                                "/carrito",
                                "/historial",
                                "/admin",

                                "/css/**",
                                "/javascript/**",
                                "/images/**",

                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs",
                                "/api-docs/**",

                                "/api/auth/**",
                                "/api/vestidos",
                                "/api/vestidos/**",
                                "/api/admin/vestidos",
                                "/api/admin/vestidos/**",
                                "/api/carrito",
                                "/api/carrito/**",
                                "/api/ventas",
                                "/api/ventas/**",
                                "/pago",
                                "/ver-productos",
                                "/crear-producto",
                                "/historial-ventas",
                                "/historial-ventas",
                                "/api/pago",
                                "/api/pago/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}