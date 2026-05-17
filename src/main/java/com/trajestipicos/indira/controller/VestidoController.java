package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.VestidoDetalleDTO;
import com.trajestipicos.indira.dto.VestidoListadoDTO;
import com.trajestipicos.indira.service.VestidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vestidos")
@Tag(name = "Vestidos", description = "Gestión del catálogo de vestidos")
public class VestidoController {

    private final VestidoService vestidoService;

    public VestidoController(VestidoService vestidoService) {
        this.vestidoService = vestidoService;
    }

    @Operation(summary = "Obtener todos los vestidos activos")
    @GetMapping
    public List<VestidoListadoDTO> listarVestidos() {
        return vestidoService.listarActivosDTO();
    }

    @Operation(summary = "Obtener vestidos por modelo")
    @GetMapping("/modelo/{idModelo}")
    public List<VestidoListadoDTO> listarPorModelo(@PathVariable String idModelo) {
        return vestidoService.listarActivosPorModeloDTO(idModelo);
    }

    @Operation(summary = "Obtener vestidos activos por talla")
    @GetMapping("/talla/{talla}")
    public List<VestidoListadoDTO> listarPorTalla(@PathVariable String talla) {
        return vestidoService.listarActivosPorTallaDTO(talla);
    }

    @Operation(summary = "Obtener vestidos activos por modelo y talla")
    @GetMapping("/modelo/{idModelo}/talla/{talla}")
    public List<VestidoListadoDTO> listarPorModeloYTalla(
            @PathVariable String idModelo,
            @PathVariable String talla
    ) {
        return vestidoService.listarActivosPorModeloYTallaDTO(
                idModelo,
                talla
        );
    }

    @Operation(summary = "Obtener detalle completo de un vestido")
    @GetMapping("/{idVestido}")
    public VestidoDetalleDTO detalleVestido(@PathVariable Long idVestido) {
        return vestidoService.obtenerDetalleVestido(idVestido);
    }
}