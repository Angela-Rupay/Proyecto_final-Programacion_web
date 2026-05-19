package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByUsuario_Documento(Long documento);

    Optional<CarritoItem> findByUsuario_DocumentoAndVestido_IdVestido(
            Long documento,
            Long idVestido
    );

    boolean existsByUsuario_DocumentoAndVestido_IdVestido(
            Long documento,
            Long idVestido
    );
}