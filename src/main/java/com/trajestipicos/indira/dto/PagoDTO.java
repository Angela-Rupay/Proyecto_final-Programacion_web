package com.trajestipicos.indira.dto;

import lombok.Data;

@Data
public class PagoDTO {

    private Long documento;
    private String titular;
    private String numeroTarjeta;
    private String fechaVencimiento;
    private String cvv;
    private String direccion;
}