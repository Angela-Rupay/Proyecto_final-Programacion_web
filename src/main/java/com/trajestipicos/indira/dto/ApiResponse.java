package com.trajestipicos.indira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * DTO utilizado para enviar respuestas generales desde el backend hacia el frontend.
 * <p>
 * Permite indicar si una operación fue exitosa y mostrar un mensaje asociado
 * al resultado del proceso.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Data
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
}