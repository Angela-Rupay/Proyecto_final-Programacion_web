package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Vestido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VestidoRepository extends JpaRepository<Vestido, Long> {

    List<Vestido> findByActivoTrue();

    List<Vestido> findByModelo_IdModeloAndActivoTrue(String idModelo);

    boolean existsByIdVestidoAndActivoTrue(Long idVestido);
}