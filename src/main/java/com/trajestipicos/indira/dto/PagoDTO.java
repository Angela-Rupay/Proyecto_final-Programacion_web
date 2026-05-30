package com.trajestipicos.indira.dto;

import lombok.Data;
/**
 * DTO utilizado para recibir la información del formulario de pago simulado.
 * <p>
 * Incluye datos de tarjeta, información del titular y datos de entrega
 * necesarios para finalizar la compra.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
public class PagoDTO {

    private Long documento;
    private String titular;
    private String numeroTarjeta;
    private String fechaVencimiento;
    private String cvv;
    private String direccion;
    private String barrio;
}