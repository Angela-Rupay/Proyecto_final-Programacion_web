package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeloRepository extends JpaRepository<Modelo, String> {
}