package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
/**
 * DTO utilizado para enviar al frontend la información detallada de un vestido.
 * <p>
 * Se usa principalmente en la vista de detalle del catálogo, donde el usuario
 * consulta nombre, talla, precio y modelo del producto seleccionado.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
@AllArgsConstructor
public class VestidoDetalleDTO {

    private Long idVestido;

    private String nombre;

    private String talla;

    private BigDecimal precioBase;

    private String modelo;
}