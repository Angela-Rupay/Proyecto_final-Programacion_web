package com.trajestipicos.indira.service;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.dto.DetalleVentaResponseDTO;
import com.trajestipicos.indira.dto.VentaResponseDTO;
import com.trajestipicos.indira.model.*;
import com.trajestipicos.indira.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final VestidoRepository vestidoRepository;

    public VentaService(
            VentaRepository ventaRepository,
            DetalleVentaRepository detalleVentaRepository,
            CarritoItemRepository carritoItemRepository,
            UsuarioRepository usuarioRepository,
            VestidoRepository vestidoRepository
    ) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.usuarioRepository = usuarioRepository;
        this.vestidoRepository = vestidoRepository;
    }

    public ApiResponse realizarCompra(Long documento) {

        Usuario usuario = usuarioRepository.findById(documento)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<CarritoItem> items = carritoItemRepository.findByUsuario_Documento(documento);

        if (items.isEmpty()) {
            return new ApiResponse(false, "El carrito está vacío");
        }

        for (CarritoItem item : items) {
            if (!item.getVestido().getActivo()) {
                return new ApiResponse(
                        false,
                        "El vestido ya no está disponible: " + item.getVestido().getNombre()
                );
            }
        }

        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem item : items) {
            total = total.add(item.getVestido().getPrecioBase());
        }

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setFechaCompra(LocalDateTime.now());
        venta.setTotal(total);

        Venta ventaGuardada = ventaRepository.save(venta);

        for (CarritoItem item : items) {

            Vestido vestido = item.getVestido();

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(ventaGuardada);
            detalle.setVestido(vestido);
            detalle.setSubtotal(vestido.getPrecioBase());

            detalleVentaRepository.save(detalle);

            vestido.setActivo(false);
            vestidoRepository.save(vestido);
        }

        carritoItemRepository.deleteAll(items);

        return new ApiResponse(true, "Compra realizada correctamente");
    }

    public List<VentaResponseDTO> historialCliente(Long documento) {

        return ventaRepository.findByUsuario_Documento(documento)
                .stream()
                .map(venta -> new VentaResponseDTO(
                        venta.getIdVenta(),
                        venta.getUsuario().getDocumento(),
                        venta.getUsuario().getNombre(),
                        venta.getFechaCompra(),
                        venta.getTotal()
                ))
                .toList();
    }

    public List<VentaResponseDTO> historialVentas() {

        return ventaRepository.findAll()
                .stream()
                .map(venta -> new VentaResponseDTO(
                        venta.getIdVenta(),
                        venta.getUsuario().getDocumento(),
                        venta.getUsuario().getNombre(),
                        venta.getFechaCompra(),
                        venta.getTotal()
                ))
                .toList();
    }

    public List<DetalleVentaResponseDTO> detalleVenta(Long idVenta) {

        return detalleVentaRepository.findByVenta_IdVenta(idVenta)
                .stream()
                .map(detalle -> new DetalleVentaResponseDTO(
                        detalle.getVestido().getNombre(),
                        detalle.getVestido().getTalla(),
                        detalle.getSubtotal()
                ))
                .toList();
    }
}
