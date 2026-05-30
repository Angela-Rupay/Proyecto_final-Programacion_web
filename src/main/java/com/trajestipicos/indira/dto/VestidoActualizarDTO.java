package com.trajestipicos.indira.dto;

import lombok.Data;

import java.math.BigDecimal;
/**
 * DTO utilizado para recibir los datos editables de un vestido existente.
 * <p>
 * Permite actualizar información básica del producto desde el panel
 * administrativo.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
public class VestidoActualizarDTO {

    private String nombre;

    private String talla;

    private BigDecimal precioBase;

    private String idModelo;
}