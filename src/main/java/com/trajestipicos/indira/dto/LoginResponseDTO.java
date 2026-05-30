package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * DTO utilizado para devolver la información de un usuario autenticado.
 * <p>
 * Incluye datos básicos del usuario, su rol dentro del sistema, dirección,
 * barrio y el token JWT necesario para consumir APIs protegidas.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private boolean success;
    private String message;

    private Long documento;
    private String nombre;
    private String apellido;
    private String correo;
    private String rol;
    private String direccion;
    private String barrio;

    private String token;
}