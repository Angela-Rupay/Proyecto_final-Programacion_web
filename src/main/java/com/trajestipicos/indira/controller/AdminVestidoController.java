package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.*;
import com.trajestipicos.indira.model.Vestido;
import com.trajestipicos.indira.service.VestidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/vestidos")
@Tag(name = "Administración de vestidos", description = "Endpoints para crear, editar, activar y desactivar vestidos")
public class AdminVestidoController {

    private final VestidoService vestidoService;

    public AdminVestidoController(VestidoService vestidoService) {
        this.vestidoService = vestidoService;
    }

    @Operation(summary = "Listar todos los vestidos, activos e inactivos")
    @GetMapping
    public List<Vestido> listarTodos() {
        return vestidoService.listarTodos();
    }

    @Operation(summary = "Crear un vestido")
    @PostMapping
    public ApiResponse crearVestido(@RequestBody VestidoRegistroDTO dto) {
        return vestidoService.crearVestido(dto);
    }

    @Operation(summary = "Crear vestido con imágenes")
    @PostMapping(
            value = "/con-imagenes",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse crearVestidoConImagenes(
            @RequestParam String nombre,
            @RequestParam String talla,
            @RequestParam BigDecimal precioBase,
            @RequestParam String idModelo,
            @RequestParam MultipartFile imagen1,
            @RequestParam MultipartFile imagen2,
            @RequestParam MultipartFile imagen3
    ) {
        VestidoRegistroDTO dto = new VestidoRegistroDTO();
        dto.setNombre(nombre);
        dto.setTalla(talla);
        dto.setPrecioBase(precioBase);
        dto.setIdModelo(idModelo);

        return vestidoService.crearVestidoConImagenes(
                dto,
                imagen1,
                imagen2,
                imagen3
        );
    }

    @Operation(summary = "Actualizar un vestido existente")
    @PutMapping("/{idVestido}")
    public ApiResponse actualizarVestido(
            @PathVariable Long idVestido,
            @RequestBody VestidoActualizarDTO dto
    ) {
        return vestidoService.actualizarVestido(idVestido, dto);
    }

    @Operation(summary = "Actualizar vestido con imágenes opcionales")
    @PutMapping(
            value = "/{idVestido}/con-imagenes",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse actualizarVestidoConImagenes(
            @PathVariable Long idVestido,
            @RequestParam String nombre,
            @RequestParam String talla,
            @RequestParam BigDecimal precioBase,
            @RequestParam String idModelo,
            @RequestParam(required = false) MultipartFile imagen1,
            @RequestParam(required = false) MultipartFile imagen2,
            @RequestParam(required = false) MultipartFile imagen3
    ) {
        VestidoActualizarDTO dto = new VestidoActualizarDTO();
        dto.setNombre(nombre);
        dto.setTalla(talla);
        dto.setPrecioBase(precioBase);
        dto.setIdModelo(idModelo);

        return vestidoService.actualizarVestidoConImagenes(
                idVestido,
                dto,
                imagen1,
                imagen2,
                imagen3
        );
    }

    @Operation(summary = "Desactivar un vestido del catálogo")
    @PatchMapping("/{idVestido}/desactivar")
    public ApiResponse desactivarVestido(@PathVariable Long idVestido) {
        return vestidoService.desactivarVestido(idVestido);
    }

    @Operation(summary = "Activar un vestido del catálogo")
    @PatchMapping("/{idVestido}/activar")
    public ApiResponse activarVestido(@PathVariable Long idVestido) {
        return vestidoService.activarVestido(idVestido);
    }

    @Operation(summary = "Desactivar un vestido del catálogo")
    @DeleteMapping("/{idVestido}")
    public ApiResponse eliminarLogicoVestido(@PathVariable Long idVestido) {
        return vestidoService.desactivarVestido(idVestido);
    }
}