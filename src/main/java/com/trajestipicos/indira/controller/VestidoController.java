package com.trajestipicos.indira.controller;

import com.trajestipicos.indira.dto.VestidoDetalleDTO;
import com.trajestipicos.indira.dto.VestidoListadoDTO;
import com.trajestipicos.indira.service.VestidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controlador REST encargado de exponer las operaciones públicas del catálogo
 * de vestidos.
 * <p>
 * Permite consultar vestidos activos, aplicar filtros por modelo y talla, y
 * obtener el detalle de un vestido específico. Estos endpoints son públicos
 * porque el catálogo puede visualizarse sin iniciar sesión.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@RestController
@RequestMapping("/api/vestidos")
@Tag(name = "Vestidos", description = "Gestión del catálogo de vestidos")
public class VestidoController {

    private final VestidoService vestidoService;
    /**
     * Crea el controlador del catálogo usando el servicio de vestidos.
     *
     * @param vestidoService servicio encargado de la lógica de consulta de vestidos.
     */
    public VestidoController(VestidoService vestidoService) {
        this.vestidoService = vestidoService;
    }
    /**
     * Obtiene todos los vestidos activos disponibles para el catálogo público.
     *
     * @return lista de vestidos activos en formato DTO.
     */
    @Operation(summary = "Obtener todos los vestidos activos")
    @GetMapping
    public List<VestidoListadoDTO> listarVestidos() {
        return vestidoService.listarActivosDTO();
    }
    /**
     * Obtiene los vestidos activos asociados a un modelo específico.
     *
     * @param idModelo identificador del modelo de vestido.
     * @return lista de vestidos activos pertenecientes al modelo indicado.
     */
    @Operation(summary = "Obtener vestidos por modelo")
    @GetMapping("/modelo/{idModelo}")
    public List<VestidoListadoDTO> listarPorModelo(@PathVariable String idModelo) {
        return vestidoService.listarActivosPorModeloDTO(idModelo);
    }
    /**
     * Obtiene los vestidos activos filtrados por talla.
     *
     * @param talla talla solicitada por el usuario.
     * @return lista de vestidos activos que coinciden con la talla indicada.
     */
    @Operation(summary = "Obtener vestidos activos por talla")
    @GetMapping("/talla/{talla}")
    public List<VestidoListadoDTO> listarPorTalla(@PathVariable String talla) {
        return vestidoService.listarActivosPorTallaDTO(talla);
    }
    /**
     * Obtiene los vestidos activos que coinciden simultáneamente con modelo y talla.
     *
     * @param idModelo identificador del modelo de vestido.
     * @param talla talla solicitada por el usuario.
     * @return lista de vestidos activos que cumplen ambos filtros.
     */
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
    /**
     * Obtiene la información detallada de un vestido específico.
     *
     * @param idVestido identificador del vestido consultado.
     * @return detalle del vestido solicitado.
     */
    @Operation(summary = "Obtener detalle completo de un vestido")
    @GetMapping("/{idVestido}")
    public VestidoDetalleDTO detalleVestido(@PathVariable Long idVestido) {
        return vestidoService.obtenerDetalleVestido(idVestido);
    }
}