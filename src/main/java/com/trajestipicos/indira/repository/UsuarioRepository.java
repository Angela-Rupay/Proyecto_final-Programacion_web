package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByGoogleId(String googleId);

    boolean existsByCorreo(String correo);

    boolean existsByDocumento(Long documento);

    boolean existsByTelefono(String telefono);

}