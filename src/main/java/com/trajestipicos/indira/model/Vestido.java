package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
/**
 * Entidad que representa un vestido disponible dentro del catálogo.
 * <p>
 * Cada vestido es tratado como una pieza única. Por esta razón, cuando un
 * vestido es comprado se desactiva para evitar que pueda volver a venderse.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Entity
@Table(name = "vestido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vestido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVestido;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String talla;

    @Column(nullable = false)
    private BigDecimal precioBase;

    /**
     * Indica si el vestido se encuentra disponible para mostrarse en el catálogo.
     * <p>
     * Cuando el vestido es vendido, este campo pasa a falso para impedir nuevas
     * compras del mismo producto.
     * </p>
     */
    @Column(nullable = false)
    private Boolean activo = true;

    /**
     * Modelo o categoría a la que pertenece el vestido.
     */
    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private Modelo modelo;
}