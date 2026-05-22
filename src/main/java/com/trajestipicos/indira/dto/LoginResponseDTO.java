package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

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