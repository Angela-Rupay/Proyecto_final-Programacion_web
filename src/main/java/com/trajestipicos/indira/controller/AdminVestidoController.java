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
/**
 * Controlador REST encargado de la administración de vestidos.
 * <p>
 * Expone operaciones para listar, crear, actualizar y eliminar vestidos desde
 * el panel administrativo. Estos endpoints están protegidos por rol ADMIN
 * desde la configuración de seguridad.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@RestController
@RequestMapping("/api/admin/vestidos")
@Tag(name = "Administración de vestidos", description = "Endpoints para crear, editar, listar y eliminar vestidos")
public class AdminVestidoController {

    private final VestidoService vestidoService;
    /**
     * Crea el controlador administrativo de vestidos.
     *
     * @param vestidoService servicio encargado de la lógica de gestión de vestidos.
     */
    public AdminVestidoController(VestidoService vestidoService) {
        this.vestidoService = vestidoService;
    }
    /**
     * Lista todos los vestidos registrados, incluyendo activos, inactivos y vendidos.
     *
     * @return lista completa de vestidos en formato DTO.
     */
    @GetMapping
    public List<VestidoListadoDTO> listarTodos() {
        return vestidoService.listarTodosDTO();
    }
    /**
     * Crea un vestido sin manejo de imágenes.
     * <p>
     * Este endpoint se conserva para operaciones simples o pruebas desde Swagger.
     * En el frontend principal se utiliza la creación con imágenes.
     * </p>
     *
     * @param dto datos básicos del vestido.
     * @return respuesta indicando si el vestido fue creado correctamente.
     */
    @Operation(summary = "Crear un vestido")
    @PostMapping
    public ApiResponse crearVestido(@RequestBody VestidoRegistroDTO dto) {
        return vestidoService.crearVestido(dto);
    }
    /**
     * Crea un vestido junto con sus tres imágenes principales.
     *
     * @param nombre nombre comercial del vestido.
     * @param talla talla disponible del vestido.
     * @param precioBase precio base del vestido.
     * @param idModelo identificador del modelo asociado.
     * @param imagen1 primera imagen del vestido.
     * @param imagen2 segunda imagen del vestido.
     * @param imagen3 tercera imagen del vestido.
     * @return respuesta indicando el resultado de la creación.
     */
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
    /**
     * Actualiza la información básica de un vestido existente.
     *
     * @param idVestido identificador del vestido que será actualizado.
     * @param dto datos actualizados del vestido.
     * @return respuesta indicando si la actualización fue exitosa.
     */
    @Operation(summary = "Actualizar un vestido existente")
    @PutMapping("/{idVestido}")
    public ApiResponse actualizarVestido(
            @PathVariable Long idVestido,
            @RequestBody VestidoActualizarDTO dto
    ) {
        return vestidoService.actualizarVestido(idVestido, dto);
    }
    /**
     * Actualiza un vestido y permite reemplazar una o varias imágenes.
     * <p>
     * Las imágenes son opcionales para permitir que el administrador actualice
     * únicamente los datos textuales sin tener que cargar nuevamente todas las
     * imágenes del producto.
     * </p>
     *
     * @param idVestido identificador del vestido a actualizar.
     * @param nombre nuevo nombre del vestido.
     * @param talla nueva talla del vestido.
     * @param precioBase nuevo precio base.
     * @param idModelo identificador del modelo asociado.
     * @param imagen1 nueva primera imagen, si se desea reemplazar.
     * @param imagen2 nueva segunda imagen, si se desea reemplazar.
     * @param imagen3 nueva tercera imagen, si se desea reemplazar.
     * @return respuesta indicando el resultado de la actualización.
     */
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
    /**
     * Elimina un vestido siempre que no haya sido vendido previamente.
     * <p>
     * Esta validación evita borrar productos que ya hacen parte del historial de
     * ventas, conservando la trazabilidad del sistema.
     * </p>
     *
     * @param idVestido identificador del vestido a eliminar.
     * @return respuesta indicando si la eliminación fue permitida.
     */
    @Operation(summary = "Eliminar un vestido no vendido")
    @DeleteMapping("/{idVestido}")
    public ApiResponse eliminarVestido(@PathVariable Long idVestido) {
        return vestidoService.eliminarVestido(idVestido);
    }
}