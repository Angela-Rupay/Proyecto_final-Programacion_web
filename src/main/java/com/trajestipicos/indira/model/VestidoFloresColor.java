package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "vestido_flores_color",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_vestido", "id_color"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VestidoFloresColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_vestido", nullable = false)
    private Vestido vestido;

    @ManyToOne
    @JoinColumn(name = "id_color", nullable = false)
    private FloresColor color;
}