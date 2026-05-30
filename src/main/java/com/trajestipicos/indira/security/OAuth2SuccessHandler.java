package com.trajestipicos.indira.security;

import com.trajestipicos.indira.model.Usuario;
import com.trajestipicos.indira.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
/**
 * Manejador ejecutado cuando un inicio de sesión con Google OAuth2 finaliza de
 * forma exitosa.
 * <p>
 * Su función es identificar si el correo de Google ya pertenece a un usuario
 * registrado. Si el usuario existe, se genera un token JWT y se redirige a una
 * vista intermedia que guarda la sesión en el navegador. Si no existe, se
 * redirige al formulario de completar perfil.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    /**
     * Crea el manejador de autenticación OAuth2.
     *
     * @param usuarioRepository repositorio utilizado para buscar usuarios por correo.
     * @param jwtService servicio utilizado para generar el token JWT del usuario autenticado.
     */
    public OAuth2SuccessHandler(
            UsuarioRepository usuarioRepository,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }
    /**
     * Procesa la respuesta exitosa de Google después de autenticar al usuario.
     * <p>
     * Se extraen los datos principales del perfil de Google, como correo, nombre
     * y apellido. Si el correo ya existe en el sistema, se genera un JWT y se
     * redirige a {@code /oauth-success}. Si no existe, se envían los datos
     * disponibles a {@code /completar-perfil} para finalizar el registro.
     * </p>
     *
     * @param request petición HTTP original.
     * @param response respuesta HTTP usada para redireccionar al usuario.
     * @param authentication objeto de autenticación entregado por Spring Security.
     * @throws IOException si ocurre un error al redireccionar.
     * @throws ServletException si ocurre un error durante el procesamiento de la autenticación.
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String googleId = oauthUser.getAttribute("sub");
        String correo = oauthUser.getAttribute("email");
        String nombre = oauthUser.getAttribute("given_name");
        String apellido = oauthUser.getAttribute("family_name");

        if (nombre == null || nombre.isBlank()) {
            String name = oauthUser.getAttribute("name");
            nombre = name != null ? name : "";
        }

        if (apellido == null) {
            apellido = "";
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            String token = jwtService.generarToken(usuario);

            String redirectUrl = "/oauth-success"
                    + "?success=true"
                    + "&message=" + encode("Login exitoso")
                    + "&documento=" + usuario.getDocumento()
                    + "&nombre=" + encode(usuario.getNombre())
                    + "&apellido=" + encode(usuario.getApellido())
                    + "&correo=" + encode(usuario.getCorreo())
                    + "&rol=" + encode(usuario.getRol().getTipoRol())
                    + "&direccion=" + encode(usuario.getDireccion())
                    + "&barrio=" + encode(usuario.getBarrio())
                    + "&token=" + encode(token);
            response.sendRedirect(redirectUrl);
            return;
        }

        String redirectUrl = "/completar-perfil"
                + "?email=" + encode(correo)
                + "&googleId=" + encode(googleId)
                + "&nombre=" + encode(nombre)
                + "&apellido=" + encode(apellido);

        response.sendRedirect(redirectUrl);
    }
    /**
     * Codifica valores enviados por URL para evitar problemas con espacios,
     * acentos o caracteres especiales.
     *
     * @param value valor que será enviado como parámetro en la URL.
     * @return valor codificado en UTF-8.
     */
    private String encode(String value) {
        if (value == null) {
            return "";
        }

        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}