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

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public OAuth2SuccessHandler(
            UsuarioRepository usuarioRepository,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

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

    private String encode(String value) {
        if (value == null) {
            return "";
        }

        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}