package com.trajestipicos.indira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
/**
 * Entidad que representa los modelos o categorías de vestidos disponibles.
 * <p>
 * Cada modelo agrupa vestidos con características similares, como Tradicional,
 * Fantasía, Pintado o Profesional.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Entity
@Table(name = "modelo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modelo {

    @Id
    private String idModelo;

    @Column(nullable = false, unique = true)
    private String nombreModelo;

    /**
     * Vestidos asociados al modelo.
     */
    @OneToMany(mappedBy = "modelo")
    @JsonIgnore
    private List<Vestido> vestidos;
}