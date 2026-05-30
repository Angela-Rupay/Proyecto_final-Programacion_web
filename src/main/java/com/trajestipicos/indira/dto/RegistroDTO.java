package com.trajestipicos.indira.dto;

import lombok.Data;
/**
 * DTO utilizado para recibir los datos necesarios para registrar un usuario.
 * <p>
 * Se emplea tanto para el registro de clientes como para la creación de
 * administradores desde los endpoints correspondientes.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
public class RegistroDTO {

    private Long documento;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String password;
}