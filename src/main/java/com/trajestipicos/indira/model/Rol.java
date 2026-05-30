package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
/**
 * Entidad que representa los roles disponibles en la aplicación.
 * <p>
 * Los roles permiten diferenciar los permisos de acceso entre usuarios
 * administradores y clientes.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
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

    /**
     * Usuarios asociados a este rol.
     */
    @OneToMany(mappedBy = "rol")
    @ToString.Exclude
    private List<Usuario> usuarios;
}