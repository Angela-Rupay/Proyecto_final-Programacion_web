package com.trajestipicos.indira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Entidad que representa una venta realizada en el sistema.
 * <p>
 * Almacena la información general de la compra, incluyendo el cliente, la fecha
 * de realización y el valor total.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    /**
     * Cliente que realizó la compra.
     */
    @ManyToOne
    @JoinColumn(name = "documento", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false)
    private BigDecimal total;

    /**
     * Detalles asociados a la venta, es decir, los vestidos comprados.
     */
    @OneToMany(mappedBy = "venta")
    @JsonIgnore
    private List<DetalleVenta> detallesVenta;
}
