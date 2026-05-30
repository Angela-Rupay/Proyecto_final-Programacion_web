package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
/**
 * DTO utilizado para mostrar vestidos en listados del catálogo y del panel administrativo.
 * <p>
 * Incluye información resumida del vestido, el modelo asociado, su estado de
 * disponibilidad y si ya fue vendido.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
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