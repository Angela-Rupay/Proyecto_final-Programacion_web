package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repositorio encargado de gestionar los modelos de vestidos.
 * <p>
 * Los modelos permiten clasificar los vestidos dentro del catálogo, por ejemplo
 * como Tradicional, Fantasía, Pintado o Profesional.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
public interface ModeloRepository extends JpaRepository<Modelo, String> {
}