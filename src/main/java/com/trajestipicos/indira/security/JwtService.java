package com.trajestipicos.indira.security;

import com.trajestipicos.indira.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Servicio encargado de generar, leer y validar tokens JWT.
 * <p>
 * Los tokens generados almacenan información básica del usuario autenticado,
 * como correo, documento, nombre y rol. Esta información permite identificar al
 * usuario en futuras peticiones sin mantener una sesión tradicional permanente.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */

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
    /**
     * Genera un token JWT para un usuario autenticado.
     * <p>
     * El token incluye como sujeto principal el correo del usuario y como claims
     * adicionales el documento, nombre y rol. La duración definida para el token
     * es de 24 horas.
     * </p>
     *
     * @param usuario usuario autenticado para el cual se genera el token.
     * @return token JWT firmado.
     */
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
    /**
     * Obtiene el correo almacenado como sujeto dentro del token.
     *
     * @param token token JWT recibido.
     * @return correo del usuario asociado al token.
     */
    public String obtenerCorreo(String token) {
        return obtenerClaims(token).getSubject();
    }
    /**
     * Obtiene el rol almacenado dentro del token.
     *
     * @param token token JWT recibido.
     * @return rol registrado en los claims del token.
     */
    public String obtenerRol(String token) {
        return obtenerClaims(token).get("rol", String.class);
    }
    /**
     * Verifica si el token aún se encuentra vigente.
     *
     * @param token token JWT recibido.
     * @return {@code true} si el token no ha expirado; {@code false} en caso contrario.
     */
    public boolean tokenValido(String token) {
        return obtenerClaims(token)
                .getExpiration()
                .after(new Date());
    }
    /**
     * Extrae los claims contenidos dentro del token JWT.
     *
     * @param token token JWT recibido.
     * @return claims almacenados en el token.
     */
    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}