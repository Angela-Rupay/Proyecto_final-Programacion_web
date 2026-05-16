package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.model.Vestido;
import com.trajestipicos.indira.repository.VestidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.trajestipicos.indira.dto.VestidoDetalleDTO;
import com.trajestipicos.indira.service.VestidoService;

import java.util.List;

@RestController
@RequestMapping("/api/vestidos")
@Tag(name = "Vestidos", description = "Gestión del catálogo de vestidos")
public class VestidoController {

    private final VestidoRepository vestidoRepository;
    private final VestidoService vestidoService;

    public VestidoController(
            VestidoRepository vestidoRepository,
            VestidoService vestidoService
    ) {
        this.vestidoRepository = vestidoRepository;
        this.vestidoService = vestidoService;
    }

    @Operation(summary = "Obtener todos los vestidos activos")
    @GetMapping
    public List<Vestido> listarVestidos() {
        return vestidoRepository.findByActivoTrue();
    }

    @Operation(summary = "Obtener vestidos por modelo")
    @GetMapping("/modelo/{idModelo}")
    public List<Vestido> listarPorModelo(@PathVariable String idModelo) {
        return vestidoRepository.findByModelo_IdModeloAndActivoTrue(idModelo);
    }

    @Operation(summary = "Obtener vestidos activos por talla")
    @GetMapping("/talla/{talla}")
    public List<Vestido> listarPorTalla(@PathVariable String talla) {
        return vestidoRepository.findByTallaAndActivoTrue(talla);
    }

    @Operation(summary = "Obtener vestidos activos por modelo y talla")
    @GetMapping("/modelo/{idModelo}/talla/{talla}")
    public List<Vestido> listarPorModeloYTalla(
            @PathVariable String idModelo,
            @PathVariable String talla
    ) {
        return vestidoRepository.findByModelo_IdModeloAndTallaAndActivoTrue(
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