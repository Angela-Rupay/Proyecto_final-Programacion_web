package com.trajestipicos.indira.security;

import com.trajestipicos.indira.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "INDIRA_TRAJES_TIPICOS_SECRET_KEY_SUPER_SEGURA_2026";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generarToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getCorreo())
                .claim("documento", usuario.getDocumento())
                .claim("nombre", usuario.getNombre())
                .claim("rol", usuario.getRol().getTipoRol())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public String obtenerCorreo(String token) {
        return obtenerClaims(token).getSubject();
    }

    public String obtenerRol(String token) {
        return obtenerClaims(token).get("rol", String.class);
    }

    public boolean tokenValido(String token) {
        return obtenerClaims(token)
                .getExpiration()
                .after(new Date());
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}