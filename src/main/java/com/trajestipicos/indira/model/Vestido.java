package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private Modelo modelo;
}