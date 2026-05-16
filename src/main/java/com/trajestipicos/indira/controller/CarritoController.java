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

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Gestión del carrito de compras del cliente")
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

    @Operation(summary = "Agregar un producto al carrito")
    @PostMapping("/agregar")
    public ApiResponse agregarAlCarrito(@RequestBody CarritoItemDTO dto) {
        return carritoService.agregarAlCarrito(dto);
    }

    @Operation(summary = "Consultar carrito de un usuario")
    @GetMapping("/{documento}")
    public List<CarritoItemDTO> obtenerCarrito(@PathVariable String documento) {
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

    @Operation(summary = "Eliminar un producto del carrito")
    @DeleteMapping("/{idCarritoItem}")
    public ApiResponse eliminarItem(@PathVariable Long idCarritoItem) {
        return carritoService.eliminarItem(idCarritoItem);
    }
}