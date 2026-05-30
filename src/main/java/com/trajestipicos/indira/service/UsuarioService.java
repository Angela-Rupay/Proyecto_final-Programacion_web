package com.trajestipicos.indira.service;

import com.trajestipicos.indira.model.Rol;
import com.trajestipicos.indira.model.Usuario;
import com.trajestipicos.indira.repository.RolRepository;
import com.trajestipicos.indira.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.trajestipicos.indira.security.JwtService;
import com.trajestipicos.indira.dto.*;
/**
 * Servicio encargado de gestionar la lógica relacionada con usuarios.
 * <p>
 * Incluye el registro de clientes y administradores, el inicio de sesión
 * mediante correo y contraseña, la generación de tokens JWT y la finalización
 * del perfil de usuarios autenticados con Google.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    /**
     * Crea el servicio de usuarios con las dependencias necesarias para consultar
     * usuarios, roles, cifrar contraseñas y generar tokens JWT.
     *
     * @param usuarioRepository repositorio de usuarios.
     * @param rolRepository repositorio de roles.
     * @param passwordEncoder codificador utilizado para cifrar contraseñas.
     * @param jwtService servicio encargado de generar tokens JWT.
     */
    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    /**
     * Registra un nuevo usuario cliente en el sistema.
     * <p>
     * Antes de guardar el usuario se valida que el documento, correo y teléfono
     * no se encuentren registrados previamente. La contraseña se almacena cifrada
     * mediante BCrypt y al usuario se le asigna automáticamente el rol CLIENTE.
     * </p>
     *
     * @param dto datos enviados desde el formulario de registro.
     * @return respuesta indicando si el registro fue exitoso o si existe algún dato duplicado.
     */
    public ApiResponse registrarUsuario(RegistroDTO dto) {

        if (usuarioRepository.existsByDocumento(dto.getDocumento())) {
            return new ApiResponse(false, "El documento ya está registrado");
        }

        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            return new ApiResponse(false, "El correo ya está registrado");
        }

        if (usuarioRepository.existsByTelefono(dto.getTelefono())) {
            return new ApiResponse(false, "El teléfono ya está registrado");
        }

        Rol rolCliente = rolRepository.findByTipoRol("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setDocumento(dto.getDocumento());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());

        // Contraseña encriptada
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        usuario.setRol(rolCliente);
        usuario.setActivo(true);
        usuario.setProvider("LOCAL");
        usuario.setGoogleId(null);

        usuarioRepository.save(usuario);

        return new ApiResponse(true, "Usuario registrado correctamente");
    }
    /**
     * Valida las credenciales de inicio de sesión de un usuario local.
     * <p>
     * Si el correo existe, el usuario está activo y la contraseña coincide con la
     * versión cifrada almacenada, se genera un token JWT y se retorna la información
     * básica del usuario autenticado.
     * </p>
     *
     * @param dto datos de acceso compuestos por correo y contraseña.
     * @return respuesta con los datos del usuario autenticado, su rol y su token JWT.
     */
    public LoginResponseDTO login(LoginDTO dto) {

        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElse(null);

        if (usuario == null || !usuario.getActivo()) {
            return new LoginResponseDTO(
                    false,
                    "Usuario no encontrado",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            return new LoginResponseDTO(
                    false,
                    "Contraseña incorrecta",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        String token = jwtService.generarToken(usuario);

        return new LoginResponseDTO(
                true,
                "Login exitoso",
                usuario.getDocumento(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getRol().getTipoRol(),
                usuario.getDireccion(),
                usuario.getBarrio(),
                token
        );
    }
    /**
     * Registra un nuevo usuario administrador en el sistema.
     * <p>
     * Este metodo se utiliza para crear cuentas con rol ADMIN. Antes de guardar,
     * valida que documento, correo y teléfono no estén registrados previamente.
     * La contraseña también se almacena cifrada.
     * </p>
     *
     * @param dto datos necesarios para crear el administrador.
     * @return respuesta indicando si el administrador fue registrado correctamente.
     */
    public ApiResponse registrarAdmin(RegistroDTO dto) {

        if (usuarioRepository.existsByDocumento(dto.getDocumento())) {
            return new ApiResponse(false, "El documento ya está registrado");
        }

        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            return new ApiResponse(false, "El correo ya está registrado");
        }

        if (usuarioRepository.existsByTelefono(dto.getTelefono())) {
            return new ApiResponse(false, "El teléfono ya está registrado");
        }

        Rol rolAdmin = rolRepository.findByTipoRol("ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setDocumento(dto.getDocumento());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        usuario.setRol(rolAdmin);
        usuario.setActivo(true);
        usuario.setProvider("LOCAL");
        usuario.setGoogleId(null);

        usuarioRepository.save(usuario);

        return new ApiResponse(true, "Administrador registrado correctamente");
    }
    /**
     * Completa el perfil de un usuario autenticado mediante Google OAuth2.
     * <p>
     * Cuando un usuario inicia sesión con Google por primera vez, se solicitan
     * datos adicionales como documento y teléfono. Después de validar que no existan
     * duplicados, se crea el usuario con rol CLIENTE y se genera un token JWT.
     * </p>
     *
     * @param dto datos complementarios obtenidos del formulario de completar perfil.
     * @return respuesta con la información del usuario creado y su token JWT.
     */

    public LoginResponseDTO completarPerfilGoogle(CompletarPerfilGoogleDTO dto) {

        if (dto.getDocumento() == null) {
            return new LoginResponseDTO(
                    false,
                    "El documento es obligatorio",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (dto.getTelefono() == null || dto.getTelefono().isBlank()) {
            return new LoginResponseDTO(
                    false,
                    "El teléfono es obligatorio",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return new LoginResponseDTO(
                    false,
                    "El correo de Google es obligatorio",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (dto.getGoogleId() == null || dto.getGoogleId().isBlank()) {
            return new LoginResponseDTO(
                    false,
                    "El identificador de Google es obligatorio",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null

            );
        }

        if (usuarioRepository.existsByDocumento(dto.getDocumento())) {
            return new LoginResponseDTO(
                    false,
                    "Ya existe un usuario con ese documento",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (usuarioRepository.existsByCorreo(dto.getEmail())) {
            return new LoginResponseDTO(
                    false,
                    "Ya existe un usuario con ese correo",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null

            );
        }

        Rol rolCliente = rolRepository.findByTipoRol("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setDocumento(dto.getDocumento());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setPassword(null);
        usuario.setRol(rolCliente);
        usuario.setActivo(true);
        usuario.setProvider("GOOGLE");
        usuario.setGoogleId(dto.getGoogleId());

        usuarioRepository.save(usuario);

        String token = jwtService.generarToken(usuario);

        return new LoginResponseDTO(
                true,
                "Perfil completado correctamente",
                usuario.getDocumento(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getRol().getTipoRol(),
                usuario.getDireccion(),
                usuario.getBarrio(),
                token
        );

    }
}