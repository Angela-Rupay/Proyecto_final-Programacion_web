package com.trajestipicos.indira.dto;

import lombok.Data;

@Data
public class RegistroDTO {

    private String documento;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String password;
}