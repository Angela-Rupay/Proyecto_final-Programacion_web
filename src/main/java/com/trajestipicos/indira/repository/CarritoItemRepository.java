package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByUsuario_Documento(String documento);
}