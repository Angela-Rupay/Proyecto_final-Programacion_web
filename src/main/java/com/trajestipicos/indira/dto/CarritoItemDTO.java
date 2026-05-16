package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItemDTO {

    private String documento;
    private Long idVestido;

    private Long idCarritoItem;
    private String nombreVestido;
    private String modelo;
    private String talla;
    private BigDecimal precioBase;
}