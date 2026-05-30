package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Repositorio encargado de gestionar las ventas registradas en el sistema.
 * <p>
 * Permite consultar ventas generales y el historial de compras asociado a un
 * cliente específico.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
public interface VentaRepository extends JpaRepository<Venta, Long> {
    /**
     * Consulta las ventas realizadas por un usuario específico.
     *
     * @param documento documento del cliente.
     * @return lista de ventas asociadas al usuario.
     */
    List<Venta> findByUsuario_Documento(Long documento);
}