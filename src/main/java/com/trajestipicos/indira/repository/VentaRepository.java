package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByUsuario_Documento(Long documento);
}