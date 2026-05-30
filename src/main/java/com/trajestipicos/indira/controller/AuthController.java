package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.*;
import com.trajestipicos.indira.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar los procesos de autenticación
 * y registro de usuarios dentro del sistema.
 * <p>
 * Expone endpoints públicos para registrar clientes, iniciar sesión,
 * completar perfiles asociados a Google OAuth y crear usuarios
 * administradores cuando sea necesario durante la configuración del sistema.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro y login de usuarios")
public class AuthController {

    private final UsuarioService usuarioService;

    /**
     * Crea el controlador de autenticación usando el servicio de usuarios.
     *
     * @param usuarioService servicio encargado de la lógica de registro,
     *                       login y creación de usuarios.
     */
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Registra un nuevo usuario cliente en la plataforma.
     *
     * @param dto datos ingresados por el usuario en el formulario de registro.
     * @return respuesta indicando si el registro fue exitoso o si ocurrió
     * algún error de validación.
     */
    @Operation(summary = "Registrar un nuevo usuario")
    @PostMapping("/registro")
    public ApiResponse registrar(@RequestBody RegistroDTO dto) {
        return usuarioService.registrarUsuario(dto);
    }
    /**
     * Realiza el inicio de sesión de un usuario registrado.
     * <p>
     * Si las credenciales son correctas, se devuelve la información básica
     * del usuario junto con un token JWT para consumir las APIs protegidas.
     * </p>
     *
     * @param dto datos de acceso compuestos por correo y contraseña.
     * @return respuesta con los datos del usuario autenticado y su token JWT.
     */
    @Operation(summary = "Iniciar sesión de usuario")
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO dto) {
        return usuarioService.login(dto);
    }
    /**
     * Registra un usuario con rol administrador.
     * <p>
     * Este endpoint se utiliza para crear cuentas administrativas dentro del
     * sistema. Su uso debe limitarse a la configuración o mantenimiento del
     * proyecto.
     * </p>
     *
     * @param dto datos necesarios para crear la cuenta administradora.
     * @return respuesta indicando si el administrador fue creado correctamente.
     */
    @Operation(summary = "Registrar un usuario administrador")
    @PostMapping("/registro-admin")
    public ApiResponse registrarAdmin(@RequestBody RegistroDTO dto) {
        return usuarioService.registrarAdmin(dto);
    }
    /**
     * Completa el registro de un usuario que inició sesión mediante Google.
     * <p>
     * Cuando el correo autenticado con Google no existe en la base de datos,
     * el usuario debe completar datos adicionales como documento y teléfono.
     * </p>
     *
     * @param dto datos complementarios del perfil autenticado con Google.
     * @return respuesta con la información del usuario y el token JWT generado.
     */
    @Operation(summary = "Completar perfil de usuario autenticado con Google")
    @PostMapping("/google/completar-perfil")
    public LoginResponseDTO completarPerfilGoogle(@RequestBody CompletarPerfilGoogleDTO dto) {
        return usuarioService.completarPerfilGoogle(dto);
    }
}