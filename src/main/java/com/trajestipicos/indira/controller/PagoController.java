package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.dto.PagoDTO;
import com.trajestipicos.indira.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
/**
 * Controlador REST encargado de procesar el pago simulado de una compra.
 * <p>
 * Este controlador recibe los datos del formulario de pago y delega en el
 * servicio correspondiente la validación de la información y la finalización
 * de la compra.
 * </p>
 */
@RestController
@RequestMapping("/api/pago")
@Tag(name = "Pago", description = "Simulación de pago y finalización de compra")
public class PagoController {

    private final PagoService pagoService;
    /**
     * Crea el controlador de pago.
     *
     * @param pagoService servicio encargado de validar y procesar el pago simulado.
     * @author Angela Sofía Rupay Aros
     */
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    /**
     * Procesa los datos del formulario de pago simulado.
     * <p>
     * Si la información es válida, se finaliza la compra asociada al carrito del
     * cliente. Este proceso registra la venta, sus detalles y actualiza el estado
     * de los vestidos vendidos.
     * </p>
     *
     * @param dto datos ingresados por el cliente en el formulario de pago.
     * @return respuesta indicando si la transacción fue completada correctamente.
     */
    @Operation(summary = "Procesar pago simulado")
    @PostMapping("/procesar")
    public ApiResponse procesarPago(@RequestBody PagoDTO dto) {
        return pagoService.procesarPago(dto);
    }
}