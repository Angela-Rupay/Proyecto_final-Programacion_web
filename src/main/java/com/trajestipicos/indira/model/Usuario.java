package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    @Id
    private Long documento;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String correo;

    private String telefono;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    @ToString.Exclude
    private Rol rol;

    @Column(nullable = false)
    private String provider; // LOCAL o GOOGLE

    @Column(unique = true)
    private String googleId;
}