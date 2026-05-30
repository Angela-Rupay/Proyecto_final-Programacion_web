package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
/**
 * DTO utilizado para transportar la información de un producto dentro del carrito.
 * <p>
 * Puede emplearse tanto para agregar un vestido al carrito como para mostrar
 * al cliente los productos seleccionados antes de realizar la compra.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItemDTO {

    private Long documento;
    private Long idVestido;

    private Long idCarritoItem;
    private String nombreVestido;
    private String modelo;
    private String talla;
    private BigDecimal precioBase;
}