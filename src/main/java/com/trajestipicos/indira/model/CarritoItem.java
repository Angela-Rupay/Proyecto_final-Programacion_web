package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;
/**
 * Entidad que representa un vestido agregado al carrito de compras de un cliente.
 * <p>
 * Relaciona un usuario con un vestido seleccionado antes de que se registre la
 * venta definitiva.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Entity
@Table(name = "carrito_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarritoItem;

    /**
     * Cliente propietario del ítem agregado al carrito.
     */
    @ManyToOne
    @JoinColumn(name = "documento", nullable = false)
    private Usuario usuario;

    /**
     * Vestido seleccionado por el cliente.
     */
    @ManyToOne
    @JoinColumn(name = "id_vestido", nullable = false)
    private Vestido vestido;
}