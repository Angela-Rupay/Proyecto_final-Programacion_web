package com.trajestipicos.indira.dto;
/**
 * DTO utilizado para completar el perfil de un usuario autenticado mediante Google.
 * <p>
 * Contiene la información adicional requerida por el sistema, como documento,
 * teléfono y los datos básicos obtenidos desde la cuenta de Google.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
public class CompletarPerfilGoogleDTO {

    private Long documento;
    private String telefono;
    private String email;
    private String googleId;
    private String nombre;
    private String apellido;

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}