package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repositorio encargado de gestionar las operaciones de persistencia de roles.
 * <p>
 * Permite consultar roles por su nombre para asignarlos durante el registro
 * de clientes y administradores.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
public interface RolRepository extends JpaRepository<Rol, Long> {
    /**
     * Busca un rol a partir de su tipo.
     *
     * @param tipoRol nombre del rol, por ejemplo ADMIN o CLIENTE.
     * @return rol encontrado, si existe.
     */
    Optional<Rol> findByTipoRol(String tipoRol);
}