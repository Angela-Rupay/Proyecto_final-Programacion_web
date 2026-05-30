package com.trajestipicos.indira.security;

import com.trajestipicos.indira.model.Usuario;
import com.trajestipicos.indira.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro encargado de validar el token JWT enviado en las peticiones HTTP.
 * <p>
 * Este filtro revisa el encabezado {@code Authorization}, extrae el token si
 * existe, valida su contenido y registra la autenticación correspondiente en el
 * contexto de seguridad de Spring. De esta manera, las APIs protegidas pueden
 * reconocer el usuario autenticado y su rol.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea el filtro JWT con los servicios necesarios para validar tokens y
     * consultar usuarios registrados en la base de datos.
     *
     * @param jwtService servicio encargado de generar y validar tokens JWT.
     * @param usuarioRepository repositorio para consultar usuarios por correo.
     */

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioRepository usuarioRepository
    ) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Procesa cada petición HTTP para determinar si contiene un token JWT válido.
     * <p>
     * Si la petición no contiene un encabezado {@code Authorization} con formato
     * {@code Bearer}, el filtro permite que la solicitud continúe sin autenticar.
     * Si el token existe y es válido, se consulta el usuario asociado, se obtiene
     * su rol y se registra una autenticación con la autoridad correspondiente.
     * </p>
     *
     * @param request petición HTTP recibida.
     * @param response respuesta HTTP generada por el servidor.
     * @param filterChain cadena de filtros de Spring Security.
     * @throws ServletException si ocurre un error en el procesamiento del filtro.
     * @throws IOException si ocurre un error de entrada o salida.
     */

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String correo = jwtService.obtenerCorreo(token);

            if (correo != null) {
                Usuario usuario = usuarioRepository.findByCorreo(correo)
                        .orElse(null);

                if (usuario != null && usuario.getActivo() && jwtService.tokenValido(token)) {

                    String rol = usuario.getRol().getTipoRol();

                    rol = rol.trim().toUpperCase();

                    if (rol.startsWith("ROLE_")) {
                        rol = rol.substring(5);
                    }


                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    usuario.getCorreo(),
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}