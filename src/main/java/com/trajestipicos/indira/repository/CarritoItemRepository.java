package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
/**
 * Repositorio encargado de gestionar los productos agregados al carrito.
 * <p>
 * Permite consultar el carrito de un usuario y validar si un vestido ya fue
 * agregado previamente por el mismo cliente.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    /**
     * Consulta todos los ítems del carrito asociados a un usuario.
     *
     * @param documento documento del usuario cliente.
     * @return lista de productos agregados al carrito.
     */
    List<CarritoItem> findByUsuario_Documento(Long documento);

    Optional<CarritoItem> findByUsuario_DocumentoAndVestido_IdVestido(
            Long documento,
            Long idVestido
    );
    /**
     * Verifica si un vestido ya se encuentra en el carrito de un usuario.
     *
     * @param documento documento del usuario cliente.
     * @param idVestido identificador del vestido.
     * @return {@code true} si el vestido ya está agregado al carrito.
     */
    boolean existsByUsuario_DocumentoAndVestido_IdVestido(
            Long documento,
            Long idVestido
    );
}