package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaRepository
        extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByVenta_IdVenta(Long idVenta);
}