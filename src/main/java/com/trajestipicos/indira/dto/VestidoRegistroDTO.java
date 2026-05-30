package com.trajestipicos.indira.dto;

import lombok.Data;

import java.math.BigDecimal;
/**
 * DTO utilizado para recibir los datos necesarios al crear un nuevo vestido.
 * <p>
 * Contiene la información básica del producto, como nombre, talla, precio base
 * y modelo asociado.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
public class VestidoRegistroDTO {

    private String nombre;

    private String talla;

    private BigDecimal precioBase;

    private String idModelo;
}