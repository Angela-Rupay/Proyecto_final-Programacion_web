package com.trajestipicos.indira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "modelo")
    @JsonIgnore
    private List<Vestido> vestidos;
}