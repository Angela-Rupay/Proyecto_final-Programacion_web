package com.trajestipicos.indira.service;

import com.trajestipicos.indira.dto.*;
import com.trajestipicos.indira.model.*;
import com.trajestipicos.indira.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con los vestidos.
 * <p>
 * Incluye operaciones de consulta para el catálogo público, administración de
 * productos, manejo de imágenes, actualización de datos y eliminación controlada
 * de vestidos no vendidos.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Service
public class VestidoService {

    private final VestidoRepository vestidoRepository;
    private final ModeloRepository modeloRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    private static final Path CARPETA_IMAGENES = Path.of("uploads/vestidos");
    /**
     * Servicio encargado de gestionar la lógica de negocio relacionada con los vestidos.
     * <p>
     * Incluye operaciones de consulta para el catálogo público, administración de
     * productos, manejo de imágenes, actualización de datos y eliminación controlada
     * de vestidos no vendidos.
     * </p>
     */
    public VestidoService(
            VestidoRepository vestidoRepository,
            ModeloRepository modeloRepository,
            DetalleVentaRepository detalleVentaRepository
    ) {
        this.vestidoRepository = vestidoRepository;
        this.modeloRepository = modeloRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }
    /**
     * Obtiene todos los vestidos registrados en el sistema.
     * <p>
     * Esta consulta se usa principalmente en el panel administrativo, ya que incluye
     * vestidos activos, inactivos y vendidos.
     * </p>
     *
     * @return lista de vestidos convertidos a DTO de listado.
     */
    public List<VestidoListadoDTO> listarTodosDTO() {
        return vestidoRepository.findAll()
                .stream()
                .map(this::convertirAListadoDTO)
                .toList();
    }
    /**
     * Obtiene los vestidos activos disponibles para el catálogo público.
     *
     * @return lista de vestidos activos convertidos a DTO.
     */
    public List<VestidoListadoDTO> listarActivosDTO() {
        return vestidoRepository.findByActivoTrue()
                .stream()
                .map(this::convertirAListadoDTO)
                .toList();
    }
    /**
     * Obtiene los vestidos activos filtrados por modelo.
     *
     * @param idModelo identificador del modelo seleccionado.
     * @return lista de vestidos activos asociados al modelo indicado.
     */
    public List<VestidoListadoDTO> listarActivosPorModeloDTO(String idModelo) {
        return vestidoRepository.findByModelo_IdModeloAndActivoTrue(idModelo)
                .stream()
                .map(this::convertirAListadoDTO)
                .toList();
    }
    /**
     * Obtiene los vestidos activos filtrados por talla.
     *
     * @param talla talla consultada por el usuario.
     * @return lista de vestidos activos que coinciden con la talla indicada.
     */
    public List<VestidoListadoDTO> listarActivosPorTallaDTO(String talla) {
        return vestidoRepository.findByTallaAndActivoTrue(talla)
                .stream()
                .map(this::convertirAListadoDTO)
                .toList();
    }
    /**
     * Obtiene los vestidos activos que coinciden con un modelo y una talla específica.
     *
     * @param idModelo identificador del modelo seleccionado.
     * @param talla talla seleccionada.
     * @return lista de vestidos activos que cumplen ambos filtros.
     */
    public List<VestidoListadoDTO> listarActivosPorModeloYTallaDTO(
            String idModelo,
            String talla
    ) {
        return vestidoRepository.findByModelo_IdModeloAndTallaAndActivoTrue(
                        idModelo,
                        talla
                )
                .stream()
                .map(this::convertirAListadoDTO)
                .toList();
    }
    /**
     * Convierte una entidad Vestido en un DTO de listado.
     * <p>
     * Además de los datos básicos del vestido, se verifica si el producto ya aparece
     * asociado a una venta. Esto permite mostrar en el panel administrativo si el
     * vestido fue vendido y evitar acciones que rompan la trazabilidad.
     * </p>
     *
     * @param vestido entidad de vestido consultada desde la base de datos.
     * @return DTO con información resumida del vestido y su estado de venta.
     */
    private VestidoListadoDTO convertirAListadoDTO(Vestido vestido) {

        boolean vendido = detalleVentaRepository.existsByVestido_IdVestido(
                vestido.getIdVestido()
        );

        return new VestidoListadoDTO(
                vestido.getIdVestido(),
                vestido.getNombre(),
                vestido.getTalla(),
                vestido.getPrecioBase(),
                vestido.getActivo(),
                vestido.getModelo().getIdModelo(),
                vestido.getModelo().getNombreModelo(),
                vendido
        );
    }
    /**
     * Crea un vestido usando únicamente los datos básicos del producto.
     *
     * @param dto datos de registro del vestido.
     * @return respuesta indicando si el vestido fue creado correctamente.
     */
    public ApiResponse crearVestido(VestidoRegistroDTO dto) {
        guardarVestido(dto);
        return new ApiResponse(true, "Vestido creado correctamente");
    }
    /**
     * Guarda un nuevo vestido en la base de datos.
     * <p>
     * El metodo valida que el modelo exista, asigna los datos principales del
     * vestido y lo registra como activo para que pueda aparecer en el catálogo.
     * </p>
     *
     * @param dto datos necesarios para crear el vestido.
     * @return entidad Vestido guardada en la base de datos.
     */
    public Vestido guardarVestido(VestidoRegistroDTO dto) {
        Modelo modelo = modeloRepository.findById(dto.getIdModelo())
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        Vestido vestido = new Vestido();
        vestido.setNombre(dto.getNombre());
        vestido.setTalla(dto.getTalla());
        vestido.setPrecioBase(dto.getPrecioBase());
        vestido.setModelo(modelo);
        vestido.setActivo(true);

        return vestidoRepository.save(vestido);
    }
    /**
     * Crea un vestido y almacena sus tres imágenes principales.
     * <p>
     * Primero se registra el vestido en la base de datos para obtener su
     * identificador. Luego se guardan las imágenes usando una convención de nombres
     * basada en el id del vestido y el número de imagen.
     * </p>
     *
     * @param dto datos básicos del vestido.
     * @param imagen1 primera imagen del vestido.
     * @param imagen2 segunda imagen del vestido.
     * @param imagen3 tercera imagen del vestido.
     * @return respuesta indicando si el proceso fue exitoso.
     */
    public ApiResponse crearVestidoConImagenes(
            VestidoRegistroDTO dto,
            MultipartFile imagen1,
            MultipartFile imagen2,
            MultipartFile imagen3
    ) {
        try {
            Vestido vestido = guardarVestido(dto);

            guardarImagenSiExiste(imagen1, vestido.getIdVestido(), 1);
            guardarImagenSiExiste(imagen2, vestido.getIdVestido(), 2);
            guardarImagenSiExiste(imagen3, vestido.getIdVestido(), 3);

            return new ApiResponse(true, "Vestido creado con imágenes correctamente");

        } catch (Exception e) {
            return new ApiResponse(false, "Error creando vestido con imágenes");
        }
    }
    /**
     * Obtiene el detalle de un vestido específico.
     *
     * @param idVestido identificador del vestido consultado.
     * @return DTO con la información detallada del vestido.
     */
    public VestidoDetalleDTO obtenerDetalleVestido(Long idVestido) {
        Vestido vestido = vestidoRepository.findById(idVestido)
                .orElseThrow(() -> new RuntimeException("Vestido no encontrado"));


        return new VestidoDetalleDTO(
                vestido.getIdVestido(),
                vestido.getNombre(),
                vestido.getTalla(),
                vestido.getPrecioBase(),
                vestido.getModelo().getNombreModelo()
        );
    }
    /**
     * Actualiza los datos básicos de un vestido existente.
     * <p>
     * Se valida la existencia del vestido y del modelo antes de modificar los datos.
     * </p>
     *
     * @param idVestido identificador del vestido a actualizar.
     * @param dto datos nuevos del vestido.
     * @return respuesta indicando si la actualización fue exitosa.
     */
    public ApiResponse actualizarVestido(Long idVestido, VestidoActualizarDTO dto) {
        Vestido vestido = vestidoRepository.findById(idVestido)
                .orElseThrow(() -> new RuntimeException("Vestido no encontrado"));

        Modelo modelo = modeloRepository.findById(dto.getIdModelo())
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        vestido.setNombre(dto.getNombre());
        vestido.setTalla(dto.getTalla());
        vestido.setPrecioBase(dto.getPrecioBase());
        vestido.setModelo(modelo);

        vestidoRepository.save(vestido);

        return new ApiResponse(true, "Vestido actualizado correctamente");
    }
    /**
     * Actualiza un vestido y reemplaza las imágenes enviadas por el administrador.
     * <p>
     * Las imágenes son opcionales, por lo que se pueden modificar solo los datos
     * textuales del vestido o reemplazar una parte de las imágenes existentes.
     * </p>
     *
     * @param idVestido identificador del vestido a actualizar.
     * @param dto datos actualizados del vestido.
     * @param imagen1 nueva primera imagen, si fue enviada.
     * @param imagen2 nueva segunda imagen, si fue enviada.
     * @param imagen3 nueva tercera imagen, si fue enviada.
     * @return respuesta indicando si la actualización fue exitosa.
     */
    public ApiResponse actualizarVestidoConImagenes(
            Long idVestido,
            VestidoActualizarDTO dto,
            MultipartFile imagen1,
            MultipartFile imagen2,
            MultipartFile imagen3
    ) {
        try {
            actualizarVestido(idVestido, dto);

            guardarImagenSiExiste(imagen1, idVestido, 1);
            guardarImagenSiExiste(imagen2, idVestido, 2);
            guardarImagenSiExiste(imagen3, idVestido, 3);

            return new ApiResponse(true, "Vestido actualizado correctamente");

        } catch (Exception e) {
            return new ApiResponse(false, "Error actualizando el vestido");
        }
    }
    /**
     * Guarda una imagen de vestido si el archivo fue enviado.
     * <p>
     * Si la carpeta de destino no existe, se crea automáticamente. Las imágenes se
     * almacenan con el formato {@code idVestido-numeroImagen.jpg}, lo que permite
     * al frontend consultarlas de forma predecible.
     * </p>
     *
     * @param archivo imagen recibida desde el formulario.
     * @param idVestido identificador del vestido asociado.
     * @param numeroImagen número asignado a la imagen dentro del carrusel.
     * @throws IOException si ocurre un error al crear la carpeta o copiar el archivo.
     */
    private void guardarImagenSiExiste(
            MultipartFile archivo,
            Long idVestido,
            int numeroImagen
    ) throws IOException {

        if (archivo == null || archivo.isEmpty()) {
            return;
        }

        if (!Files.exists(CARPETA_IMAGENES)) {
            Files.createDirectories(CARPETA_IMAGENES);
        }

        Path destino = CARPETA_IMAGENES.resolve(idVestido + "-" + numeroImagen + ".jpg");

        Files.copy(
                archivo.getInputStream(),
                destino,
                StandardCopyOption.REPLACE_EXISTING
        );
    }
    /**
     * Elimina una imagen asociada a un vestido si existe en el sistema de archivos.
     *
     * @param idVestido identificador del vestido asociado.
     * @param numeroImagen número de la imagen que se desea eliminar.
     * @throws IOException si ocurre un error durante la eliminación del archivo.
     */
    private void eliminarImagenSiExiste(Long idVestido, int numeroImagen) throws IOException {
        Path imagen = CARPETA_IMAGENES.resolve(idVestido + "-" + numeroImagen + ".jpg");

        if (Files.exists(imagen)) {
            Files.delete(imagen);
        }
    }
    /**
     * Elimina un vestido siempre que no haya sido vendido previamente.
     * <p>
     * Si el vestido ya aparece en un detalle de venta, no se elimina para conservar
     * la trazabilidad del historial de compras y ventas. Si no fue vendido, se
     * eliminan sus imágenes asociadas y posteriormente el registro de la base de datos.
     * </p>
     *
     * @param idVestido identificador del vestido a eliminar.
     * @return respuesta indicando si la eliminación fue realizada o rechazada.
     */
    public ApiResponse eliminarVestido(Long idVestido) {
        Vestido vestido = vestidoRepository.findById(idVestido)
                .orElse(null);

        if (vestido == null) {
            return new ApiResponse(false, "Vestido no encontrado");
        }

        boolean fueVendido = detalleVentaRepository.existsByVestido_IdVestido(idVestido);

        if (fueVendido) {
            return new ApiResponse(
                    false,
                    "Este vestido ya fue vendido y no puede eliminarse del historial"
            );
        }

        try {
            eliminarImagenSiExiste(idVestido, 1);
            eliminarImagenSiExiste(idVestido, 2);
            eliminarImagenSiExiste(idVestido, 3);

            vestidoRepository.delete(vestido);

            return new ApiResponse(true, "Vestido eliminado correctamente");

        } catch (Exception e) {
            return new ApiResponse(false, "Error eliminando el vestido");
        }
    }
}