package com.trajestipicos.indira.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VestidoActualizarDTO {

    private String nombre;

    private String talla;

    private BigDecimal precioBase;

    private String idModelo;
}