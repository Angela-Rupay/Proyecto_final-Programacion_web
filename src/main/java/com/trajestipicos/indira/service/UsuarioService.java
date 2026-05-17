package com.trajestipicos.indira.service;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.dto.LoginDTO;
import com.trajestipicos.indira.dto.LoginResponseDTO;
import com.trajestipicos.indira.dto.RegistroDTO;
import com.trajestipicos.indira.model.Rol;
import com.trajestipicos.indira.model.Usuario;
import com.trajestipicos.indira.repository.RolRepository;
import com.trajestipicos.indira.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.trajestipicos.indira.security.JwtService;

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

        usuarioRepository.save(usuario);

        return new ApiResponse(true, "Administrador registrado correctamente");
    }
}