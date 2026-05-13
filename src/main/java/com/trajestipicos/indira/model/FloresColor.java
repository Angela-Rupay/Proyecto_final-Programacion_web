package com.trajestipicos.indira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "flores_color")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloresColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColor;

    @Column(nullable = false, unique = true)
    private String nombreColor;

    @OneToMany(mappedBy = "color")
    @JsonIgnore
    private List<VestidoFloresColor> vestidoFloresColores;
}