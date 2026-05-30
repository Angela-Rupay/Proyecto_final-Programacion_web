package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * DTO utilizado para mostrar información resumida de una venta.
 * <p>
 * Se emplea en el historial de compras del cliente y en el historial general
 * de ventas consultado por el administrador.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
@AllArgsConstructor
public class VentaResponseDTO {

    private Long idVenta;

    private Long documentoCliente;
    private String nombreCliente;

    private LocalDateTime fechaCompra;

    private BigDecimal total;
}