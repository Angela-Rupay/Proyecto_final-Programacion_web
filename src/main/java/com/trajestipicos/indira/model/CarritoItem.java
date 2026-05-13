package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carrito_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarritoItem;

    @ManyToOne
    @JoinColumn(name = "documento", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_vestido", nullable = false)
    private Vestido vestido;
}