package com.trajestipicos.indira.dto;

import lombok.Data;
/**
 * DTO utilizado para recibir las credenciales de inicio de sesión.
 * <p>
 * Contiene el correo electrónico y la contraseña ingresados por el usuario
 * en el formulario de login.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
public class LoginDTO {

    private String correo;
    private String password;
}