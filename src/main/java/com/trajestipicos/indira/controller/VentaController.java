package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.trajestipicos.indira.dto.DetalleVentaResponseDTO;
import com.trajestipicos.indira.dto.VentaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Registro de compras y gestión de ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Operation(summary = "Realizar compra desde el carrito")
    @PostMapping("/comprar/{documento}")
    public ApiResponse realizarCompra(@PathVariable Long documento) {
        return ventaService.realizarCompra(documento);
    }

    @Operation(summary = "Consultar historial de compras de un cliente")
    @GetMapping("/cliente/{documento}")
    public List<VentaResponseDTO> historialCliente(
            @PathVariable Long documento
    ) {
        return ventaService.historialCliente(documento);
    }

    @Operation(summary = "Consultar historial general de ventas")
    @GetMapping
    public List<VentaResponseDTO> historialVentas() {
        return ventaService.historialVentas();
    }

    @Operation(summary = "Consultar detalle de una venta")
    @GetMapping("/{idVenta}")
    public List<DetalleVentaResponseDTO> detalleVenta(
            @PathVariable Long idVenta
    ) {
        return ventaService.detalleVenta(idVenta);
    }
}