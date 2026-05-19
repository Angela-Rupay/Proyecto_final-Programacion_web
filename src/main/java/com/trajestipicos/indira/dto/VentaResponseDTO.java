package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VentaResponseDTO {

    private Long idVenta;

    private Long documentoCliente;
    private String nombreCliente;

    private LocalDateTime fechaCompra;

    private BigDecimal total;
}