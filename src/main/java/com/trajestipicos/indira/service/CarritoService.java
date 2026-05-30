package com.trajestipicos.indira.service;

import com.trajestipicos.indira.dto.ApiResponse;
import com.trajestipicos.indira.dto.CarritoItemDTO;
import com.trajestipicos.indira.model.CarritoItem;
import com.trajestipicos.indira.model.Usuario;
import com.trajestipicos.indira.model.Vestido;
import com.trajestipicos.indira.repository.CarritoItemRepository;
import com.trajestipicos.indira.repository.UsuarioRepository;
import com.trajestipicos.indira.repository.VestidoRepository;
import org.springframework.stereotype.Service;
/**
 * Servicio encargado de gestionar la lógica del carrito de compras.
 * <p>
 * Permite agregar vestidos disponibles al carrito del cliente y eliminar
 * productos antes de finalizar la compra.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Service
public class CarritoService {

    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final VestidoRepository vestidoRepository;
    /**
     * Crea el servicio de carrito con los repositorios necesarios para consultar
     * usuarios, vestidos y elementos agregados al carrito.
     *
     * @param carritoItemRepository repositorio de ítems del carrito.
     * @param usuarioRepository repositorio de usuarios.
     * @param vestidoRepository repositorio de vestidos.
     */
    public CarritoService(
            CarritoItemRepository carritoItemRepository,
            UsuarioRepository usuarioRepository,
            VestidoRepository vestidoRepository
    ) {
        this.carritoItemRepository = carritoItemRepository;
        this.usuarioRepository = usuarioRepository;
        this.vestidoRepository = vestidoRepository;
    }
    /**
     * Agrega un vestido al carrito de un cliente.
     * <p>
     * Antes de guardar el ítem se valida que el usuario exista, que el vestido
     * exista, que el producto esté activo y que no se encuentre previamente en el
     * carrito del mismo cliente.
     * </p>
     *
     * @param dto datos del usuario y del vestido que se desea agregar.
     * @return respuesta indicando si el vestido fue agregado correctamente.
     */
    public ApiResponse agregarAlCarrito(CarritoItemDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getDocumento())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Vestido vestido = vestidoRepository.findById(dto.getIdVestido())
                .orElseThrow(() -> new RuntimeException("Vestido no encontrado"));

        if (!vestido.getActivo()) {
            return new ApiResponse(false, "El vestido no está disponible");
        }

        boolean yaExisteEnCarrito =
                carritoItemRepository.existsByUsuario_DocumentoAndVestido_IdVestido(
                        dto.getDocumento(),
                        dto.getIdVestido()
                );

        if (yaExisteEnCarrito) {
            return new ApiResponse(
                    false,
                    "Este vestido ya está en tu carrito"
            );
        }

        CarritoItem item = new CarritoItem();
        item.setUsuario(usuario);
        item.setVestido(vestido);

        carritoItemRepository.save(item);

        return new ApiResponse(true, "Vestido agregado al carrito");
    }
    /**
     * Elimina un ítem específico del carrito.
     *
     * @param idCarritoItem identificador del ítem dentro del carrito.
     * @return respuesta indicando que el vestido fue eliminado del carrito.
     */
    public ApiResponse eliminarItem(Long idCarritoItem) {
        carritoItemRepository.deleteById(idCarritoItem);
        return new ApiResponse(true, "Vestido eliminado del carrito");
    }
}