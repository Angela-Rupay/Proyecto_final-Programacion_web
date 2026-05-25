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
    private final OAuth2SuccessHandler oauth2SuccessHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            OAuth2SuccessHandler oauth2SuccessHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oauth2SuccessHandler = oauth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
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
                                "/sin-permisos",
                                "/error",
                                "/completar-perfil",
                                // OAuth Google
                                "/oauth2/**",
                                "/login/oauth2/**"
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

                /*
                 * OAuth2 con Google.
                 * Por ahora, solo redirigimos al catálogo cuando Google responda bien.
                 * Más adelante cambiaremos esto por un SuccessHandler personalizado
                 * para decidir si va a catálogo o a completar-perfil.
                 */
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oauth2SuccessHandler)
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":false,\"message\":\"No autenticado\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":false,\"message\":\"No tienes permisos\"}");
                        })
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}