package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.dto.PagoDTO;
import com.trajestipicos.indira.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pago")
@Tag(name = "Pago", description = "Simulación de pago y finalización de compra")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @Operation(summary = "Procesar pago simulado")
    @PostMapping("/procesar")
    public ApiResponse procesarPago(@RequestBody PagoDTO dto) {
        return pagoService.procesarPago(dto);
    }
}