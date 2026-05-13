package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DetalleVentaResponseDTO {

    private String vestido;

    private String talla;

    private BigDecimal subtotal;
}