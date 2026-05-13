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

@Service
public class CarritoService {

    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final VestidoRepository vestidoRepository;

    public CarritoService(
            CarritoItemRepository carritoItemRepository,
            UsuarioRepository usuarioRepository,
            VestidoRepository vestidoRepository
    ) {
        this.carritoItemRepository = carritoItemRepository;
        this.usuarioRepository = usuarioRepository;
        this.vestidoRepository = vestidoRepository;
    }

    public ApiResponse agregarAlCarrito(CarritoItemDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getDocumento())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Vestido vestido = vestidoRepository.findById(dto.getIdVestido())
                .orElseThrow(() -> new RuntimeException("Vestido no encontrado"));

        if (!vestido.getActivo()) {
            return new ApiResponse(false, "El vestido no está disponible");
        }

        CarritoItem item = new CarritoItem();
        item.setUsuario(usuario);
        item.setVestido(vestido);

        carritoItemRepository.save(item);

        return new ApiResponse(true, "Vestido agregado al carrito");
    }

    public ApiResponse eliminarItem(Long idCarritoItem) {
        carritoItemRepository.deleteById(idCarritoItem);
        return new ApiResponse(true, "Vestido eliminado del carrito");
    }
}