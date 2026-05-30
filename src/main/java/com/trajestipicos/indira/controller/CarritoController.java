package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.dto.CarritoItemDTO;
import com.trajestipicos.indira.model.CarritoItem;
import com.trajestipicos.indira.repository.CarritoItemRepository;
import com.trajestipicos.indira.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


import java.util.List;
/**
 * Controlador REST encargado de gestionar el carrito de compras del cliente.
 * <p>
 * Permite agregar vestidos al carrito, consultar los productos seleccionados y
 * eliminar elementos antes de finalizar la compra. Sus endpoints están
 * protegidos para usuarios con rol CLIENTE.
 *  @author Angela Sofía Rupay Aros
 * </p>
 */
@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Gestión del carrito de compras del cliente")

/**
 * Crea el controlador del carrito.
 *
 * @param carritoService servicio encargado de la lógica del carrito.
 * @param carritoItemRepository repositorio usado para consultar los ítems del carrito.
 */
public class CarritoController {

    private final CarritoService carritoService;
    private final CarritoItemRepository carritoItemRepository;

    public CarritoController(
            CarritoService carritoService,
            CarritoItemRepository carritoItemRepository
    ) {
        this.carritoService = carritoService;
        this.carritoItemRepository = carritoItemRepository;
    }
    /**
     * Agrega un vestido al carrito del cliente.
     *
     * @param dto datos del cliente y del vestido que se desea agregar.
     * @return respuesta indicando si el producto fue agregado correctamente.
     */
    @Operation(summary = "Agregar un producto al carrito")
    @PostMapping("/agregar")
    public ApiResponse agregarAlCarrito(@RequestBody CarritoItemDTO dto) {
        return carritoService.agregarAlCarrito(dto);
    }
    /**
     * Consulta los productos que un cliente tiene actualmente en su carrito.
     * <p>
     * La información se transforma a DTO para evitar exponer directamente las
     * entidades JPA al frontend.
     * </p>
     *
     * @param documento documento del usuario cliente.
     * @return lista de productos agregados al carrito.
     */
    @Operation(summary = "Consultar carrito de un usuario")
    @GetMapping("/{documento}")
    public List<CarritoItemDTO> obtenerCarrito(@PathVariable Long documento) {
        return carritoItemRepository.findByUsuario_Documento(documento)
                .stream()
                .map(item -> new CarritoItemDTO(
                        item.getUsuario().getDocumento(),
                        item.getVestido().getIdVestido(),
                        item.getIdCarritoItem(),
                        item.getVestido().getNombre(),
                        item.getVestido().getModelo().getNombreModelo(),
                        item.getVestido().getTalla(),
                        item.getVestido().getPrecioBase()
                ))
                .toList();
    }

    /**
     * Elimina un producto específico del carrito.
     *
     * @param idCarritoItem identificador del ítem dentro del carrito.
     * @return respuesta indicando si el producto fue eliminado correctamente.
     */
    @Operation(summary = "Eliminar un producto del carrito")
    @DeleteMapping("/{idCarritoItem}")
    public ApiResponse eliminarItem(@PathVariable Long idCarritoItem) {
        return carritoService.eliminarItem(idCarritoItem);
    }
}