package com.trajestipicos.indira.service;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.dto.PagoDTO;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

    private final VentaService ventaService;

    public PagoService(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    public ApiResponse procesarPago(PagoDTO dto) {

        if (dto.getDocumento() == null || dto.getDocumento().isBlank()) {
            return new ApiResponse(false, "No se encontró el usuario");
        }

        if (dto.getTitular() == null || dto.getTitular().isBlank()) {
            return new ApiResponse(false, "Ingresa el nombre del titular");
        }

        if (dto.getNumeroTarjeta() == null || !dto.getNumeroTarjeta().matches("\\d{16}")) {
            return new ApiResponse(false, "El número de tarjeta debe tener 16 dígitos");
        }

        if (dto.getFechaVencimiento() == null || !dto.getFechaVencimiento().matches("\\d{2}/\\d{2}")) {
            return new ApiResponse(false, "La fecha debe tener formato MM/AA");
        }

        if (dto.getCvv() == null || !dto.getCvv().matches("\\d{3}")) {
            return new ApiResponse(false, "El CVV debe tener 3 dígitos");
        }

        if (dto.getDireccion() == null || dto.getDireccion().isBlank()) {
            return new ApiResponse(false, "Ingresa la dirección de entrega");
        }

        return ventaService.realizarCompra(dto.getDocumento());
    }
}