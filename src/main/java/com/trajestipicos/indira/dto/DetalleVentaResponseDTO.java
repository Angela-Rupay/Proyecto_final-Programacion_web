package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
/**
 * DTO utilizado para mostrar los productos asociados a una venta específica.
 * <p>
 * Contiene datos básicos del vestido comprado, como nombre, talla y subtotal.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
@AllArgsConstructor
public class DetalleVentaResponseDTO {

    private String vestido;

    private String talla;

    private BigDecimal subtotal;
}