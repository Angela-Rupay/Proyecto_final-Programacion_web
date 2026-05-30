package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.trajestipicos.indira.dto.DetalleVentaResponseDTO;
import com.trajestipicos.indira.dto.VentaResponseDTO;

import java.util.List;
/**
 * Controlador REST encargado de gestionar las operaciones relacionadas con las
 * ventas realizadas en la plataforma.
 * <p>
 * Permite registrar compras desde el carrito, consultar el historial de compras
 * de un cliente, consultar el historial general para el administrador y obtener
 * el detalle de una venta específica.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Registro de compras y gestión de ventas")
public class VentaController {

    private final VentaService ventaService;
    /**
     * Crea el controlador de ventas.
     *
     * @param ventaService servicio encargado de la lógica de compras e historial de ventas.
     */
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }
    /**
     * Registra una compra a partir de los productos existentes en el carrito de
     * un cliente.
     *
     * @param documento documento del cliente que realiza la compra.
     * @return respuesta indicando si la compra fue registrada correctamente.
     */
    @Operation(summary = "Realizar compra desde el carrito")
    @PostMapping("/comprar/{documento}")
    public ApiResponse realizarCompra(@PathVariable Long documento) {
        return ventaService.realizarCompra(documento);
    }
    /**
     * Consulta el historial de compras de un cliente específico.
     *
     * @param documento documento del cliente consultado.
     * @return lista de compras realizadas por el cliente.
     */
    @Operation(summary = "Consultar historial de compras de un cliente")
    @GetMapping("/cliente/{documento}")
    public List<VentaResponseDTO> historialCliente(
            @PathVariable Long documento
    ) {
        return ventaService.historialCliente(documento);
    }
    /**
     * Consulta el historial general de ventas registradas en el sistema.
     * <p>
     * Esta operación está orientada al administrador, ya que permite visualizar
     * las compras realizadas por todos los clientes.
     * </p>
     *
     * @return lista general de ventas registradas.
     */
    @Operation(summary = "Consultar historial general de ventas")
    @GetMapping
    public List<VentaResponseDTO> historialVentas() {
        return ventaService.historialVentas();
    }
    /**
     * Consulta los productos asociados a una venta específica.
     *
     * @param idVenta identificador de la venta consultada.
     * @return lista de detalles asociados a la venta.
     */
    @Operation(summary = "Consultar detalle de una venta")
    @GetMapping("/{idVenta}")
    public List<DetalleVentaResponseDTO> detalleVenta(
            @PathVariable Long idVenta
    ) {
        return ventaService.detalleVenta(idVenta);
    }
}