package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "rol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Column(nullable = false, unique = true)
    private String tipoRol;

    @OneToMany(mappedBy = "rol")
    @ToString.Exclude
    private List<Usuario> usuarios;
}