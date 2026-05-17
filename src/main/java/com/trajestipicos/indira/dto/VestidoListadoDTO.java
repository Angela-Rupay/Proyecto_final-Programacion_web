package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class VestidoListadoDTO {

    private Long idVestido;
    private String nombre;
    private String talla;
    private BigDecimal precioBase;
    private Boolean activo;

    private String idModelo;
    private String nombreModelo;

    private Boolean vendido;
}