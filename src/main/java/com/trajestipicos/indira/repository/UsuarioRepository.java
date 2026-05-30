package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repositorio encargado de gestionar las operaciones de persistencia de usuarios.
 * <p>
 * Proporciona consultas personalizadas para buscar usuarios por correo y validar
 * la existencia de datos únicos como documento, correo y teléfono.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    /**
     * Busca un usuario a partir de su correo electrónico.
     *
     * @param correo correo electrónico del usuario.
     * @return usuario encontrado, si existe.
     */
    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByGoogleId(String googleId);
    /**
     * Verifica si ya existe un usuario registrado con el correo indicado.
     *
     * @param correo correo electrónico a validar.
     * @return {@code true} si el correo ya está registrado.
     */
    boolean existsByCorreo(String correo);
    /**
     * Verifica si ya existe un usuario registrado con el documento indicado.
     *
     * @param documento documento de identidad a validar.
     * @return {@code true} si el documento ya está registrado.
     */
    boolean existsByDocumento(Long documento);
    /**
     * Verifica si ya existe un usuario registrado con el teléfono indicado.
     *
     * @param telefono número de teléfono a validar.
     * @return {@code true} si el teléfono ya está registrado.
     */
    boolean existsByTelefono(String telefono);

}