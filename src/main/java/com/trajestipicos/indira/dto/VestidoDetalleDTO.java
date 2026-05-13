package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class VestidoDetalleDTO {

    private Long idVestido;

    private String nombre;

    private String talla;

    private BigDecimal precioBase;

    private String modelo;

    private List<String> colores;
}