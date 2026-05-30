package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
/**
 * Entidad que representa el detalle de una venta.
 * <p>
 * Cada registro asocia una venta con un vestido comprado y conserva el subtotal
 * correspondiente al producto vendido.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Entity
@Table(name = "detalle_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    /**
     * Venta a la que pertenece este detalle.
     */
    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    /**
     * Vestido comprado dentro de la venta.
     */
    @ManyToOne
    @JoinColumn(name = "id_vestido", nullable = false)
    private Vestido vestido;

    @Column(nullable = false)
    private BigDecimal subtotal;
}