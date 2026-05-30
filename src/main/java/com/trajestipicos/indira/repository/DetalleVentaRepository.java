package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Repositorio encargado de gestionar los detalles asociados a las ventas.
 * <p>
 * Permite consultar los productos incluidos en una venta y verificar si un
 * vestido ya fue vendido anteriormente.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
public interface DetalleVentaRepository
        extends JpaRepository<DetalleVenta, Long> {
    /**
     * Consulta los detalles pertenecientes a una venta específica.
     *
     * @param idVenta identificador de la venta.
     * @return lista de detalles asociados a la venta.
     */
    List<DetalleVenta> findByVenta_IdVenta(Long idVenta);
    /**
     * Verifica si un vestido ya aparece asociado a una venta.
     * <p>
     * Esta validación permite impedir que un producto vendido sea eliminado o
     * reactivado dentro del catálogo.
     * </p>
     *
     * @param idVestido identificador del vestido.
     * @return {@code true} si el vestido ya fue vendido.
     */
    boolean existsByVestido_IdVestido(Long idVestido);
}