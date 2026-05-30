package com.trajestipicos.indira.model;

import jakarta.persistence.*;
import lombok.*;
/**
 * Entidad que representa a los usuarios registrados en el sistema.
 * <p>
 * Un usuario puede tener rol de cliente o administrador. Además, puede
 * autenticarse de forma local mediante correo y contraseña, o mediante Google
 * OAuth2 cuando el proveedor asociado sea GOOGLE.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
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
    
    @Column(nullable = false, unique = true)
    private String telefono;

    @Column(nullable = true)
    private String password;

    /**
     * Indica si el usuario puede acceder al sistema.
     * <p>
     * Permite desactivar usuarios sin eliminarlos de la base de datos.
     * </p>
     */
    @Column(nullable = false)
    private Boolean activo = true;

    /**
     * Rol asignado al usuario para controlar sus permisos dentro del sistema.
     */
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    @ToString.Exclude
    private Rol rol;

    /**
     * Proveedor de autenticación usado por el usuario: LOCAL o GOOGLE.
     */
    @Column(nullable = false)
    private String provider; // LOCAL o GOOGLE


    /**
     * Identificador único entregado por Google cuando el usuario se autentica
     * mediante OAuth2.
     */
    @Column(unique = true)
    private String googleId;

    @Column(nullable = true, length = 150)
    private String direccion;

    @Column(nullable = true, length = 100)
    private String barrio;
}