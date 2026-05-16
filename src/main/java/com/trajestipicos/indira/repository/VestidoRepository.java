package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Vestido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VestidoRepository extends JpaRepository<Vestido, Long> {

    List<Vestido> findByActivoTrue();

    List<Vestido> findByModelo_IdModeloAndActivoTrue(String idModelo);

    List<Vestido> findByTallaAndActivoTrue(String talla);

    List<Vestido> findByModelo_IdModeloAndTallaAndActivoTrue(
            String idModelo,
            String talla
    );

    boolean existsByIdVestidoAndActivoTrue(Long idVestido);
}