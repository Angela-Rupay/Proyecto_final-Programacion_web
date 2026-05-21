package com.trajestipicos.indira.service;

import com.trajestipicos.indira.model.Rol;
import com.trajestipicos.indira.model.Usuario;
import com.trajestipicos.indira.repository.RolRepository;
import com.trajestipicos.indira.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.trajestipicos.indira.security.JwtService;
import com.trajestipicos.indira.dto.*;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    public ApiResponse registrarUsuario(RegistroDTO dto) {

        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            return new ApiResponse(false, "El correo ya está registrado");
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
                token
        );
    }

    public ApiResponse registrarAdmin(RegistroDTO dto) {

        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            return new ApiResponse(false, "El correo ya está registrado");
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

        usuarioRepository.save(usuario);

        return new ApiResponse(true, "Administrador registrado correctamente");
    }

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
                token
        );
    }
}