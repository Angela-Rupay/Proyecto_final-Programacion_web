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

@Service
public class VestidoService {

    private final VestidoRepository vestidoRepository;
    private final ModeloRepository modeloRepository;

    private static final Path CARPETA_IMAGENES = Path.of("uploads/vestidos");

    public VestidoService(
            VestidoRepository vestidoRepository,
            ModeloRepository modeloRepository
    ) {
        this.vestidoRepository = vestidoRepository;
        this.modeloRepository = modeloRepository;
    }

    public List<Vestido> listarTodos() {
        return vestidoRepository.findAll();
    }

    public ApiResponse crearVestido(VestidoRegistroDTO dto) {
        guardarVestido(dto);
        return new ApiResponse(true, "Vestido creado correctamente");
    }

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

    public ApiResponse cambiarEstadoVestido(Long idVestido, boolean activo) {
        Vestido vestido = vestidoRepository.findById(idVestido)
                .orElseThrow(() -> new RuntimeException("Vestido no encontrado"));

        vestido.setActivo(activo);
        vestidoRepository.save(vestido);

        return new ApiResponse(
                true,
                activo ? "Vestido activado correctamente" : "Vestido desactivado correctamente"
        );
    }

    public ApiResponse desactivarVestido(Long idVestido) {
        return cambiarEstadoVestido(idVestido, false);
    }

    public ApiResponse activarVestido(Long idVestido) {
        return cambiarEstadoVestido(idVestido, true);
    }


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
}