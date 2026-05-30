package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Vestido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Repositorio encargado de gestionar las consultas y operaciones de persistencia
 * de vestidos.
 * <p>
 * Incluye métodos personalizados para listar únicamente vestidos activos y
 * aplicar filtros por modelo, talla o ambos criterios.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
public interface VestidoRepository extends JpaRepository<Vestido, Long> {
    /**
     * Consulta todos los vestidos activos disponibles para el catálogo.
     *
     * @return lista de vestidos activos.
     */
    List<Vestido> findByActivoTrue();
    /**
     * Consulta los vestidos activos pertenecientes a un modelo específico.
     *
     * @param idModelo identificador del modelo.
     * @return lista de vestidos activos del modelo indicado.
     */
    List<Vestido> findByModelo_IdModeloAndActivoTrue(String idModelo);
    /**
     * Consulta los vestidos activos disponibles en una talla específica.
     *
     * @param talla talla del vestido.
     * @return lista de vestidos activos de la talla indicada.
     */
    List<Vestido> findByTallaAndActivoTrue(String talla);
    /**
     * Consulta los vestidos activos que coinciden con modelo y talla.
     *
     * @param idModelo identificador del modelo.
     * @param talla talla del vestido.
     * @return lista de vestidos activos que cumplen ambos filtros.
     */
    List<Vestido> findByModelo_IdModeloAndTallaAndActivoTrue(
            String idModelo,
            String talla
    );

    boolean existsByIdVestidoAndActivoTrue(Long idVestido);
}