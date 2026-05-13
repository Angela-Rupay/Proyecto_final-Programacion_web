package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.dto.LoginDTO;
import com.trajestipicos.indira.dto.LoginResponseDTO;
import com.trajestipicos.indira.dto.RegistroDTO;
import com.trajestipicos.indira.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro y login de usuarios")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Registrar un nuevo usuario")
    @PostMapping("/registro")
    public ApiResponse registrar(@RequestBody RegistroDTO dto) {
        return usuarioService.registrarUsuario(dto);
    }

    @Operation(summary = "Iniciar sesión de usuario")
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO dto) {
        return usuarioService.login(dto);
    }

    @Operation(summary = "Registrar un usuario administrador")
    @PostMapping("/registro-admin")
    public ApiResponse registrarAdmin(@RequestBody RegistroDTO dto) {
        return usuarioService.registrarAdmin(dto);
    }
}