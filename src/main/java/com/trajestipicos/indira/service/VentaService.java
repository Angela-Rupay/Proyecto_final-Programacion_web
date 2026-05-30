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
/**
 * Servicio encargado de gestionar el registro y consulta de ventas.
 * <p>
 * Contiene la lógica para transformar los productos del carrito en una venta,
 * generar los detalles correspondientes, desactivar los vestidos vendidos y
 * consultar los historiales de compra del cliente y del administrador.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final VestidoRepository vestidoRepository;
    /**
     * Crea el servicio de ventas con los repositorios necesarios para registrar
     * ventas, detalles, consultar carritos y actualizar vestidos.
     *
     * @param ventaRepository repositorio de ventas.
     * @param detalleVentaRepository repositorio de detalles de venta.
     * @param carritoItemRepository repositorio de ítems del carrito.
     * @param usuarioRepository repositorio de usuarios.
     * @param vestidoRepository repositorio de vestidos.
     */
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
    /**
     * Registra una compra a partir de los productos existentes en el carrito.
     * <p>
     * El proceso valida que el usuario exista, que el carrito tenga productos y que
     * todos los vestidos sigan activos. Luego calcula el total, crea la venta,
     * registra cada detalle de venta, desactiva los vestidos comprados y finalmente
     * vacía el carrito del cliente.
     * </p>
     *
     * @param documento documento del cliente que realiza la compra.
     * @return respuesta indicando si la compra fue registrada correctamente.
     */
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
    /**
     * Consulta el historial de compras de un cliente.
     *
     * @param documento documento del cliente consultado.
     * @return lista de ventas realizadas por el cliente en formato DTO.
     */
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
    /**
     * Consulta el historial general de ventas registradas en el sistema.
     * <p>
     * Esta información es utilizada por el administrador para revisar las compras
     * realizadas por todos los clientes.
     * </p>
     *
     * @return lista general de ventas en formato DTO.
     */
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
    /**
     * Consulta los productos asociados a una venta específica.
     *
     * @param idVenta identificador de la venta consultada.
     * @return lista de detalles de venta con nombre, talla y subtotal del vestido.
     */
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
