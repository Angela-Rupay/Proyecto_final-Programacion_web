package com.trajestipicos.indira.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Controlador encargado de resolver las vistas HTML de la aplicación.
 * <p>
 * A diferencia de los controladores REST, este controlador no devuelve datos
 * en formato JSON, sino nombres de plantillas que son renderizadas por Spring
 * para mostrar las páginas del frontend.
 * </p>
 *  @author Angela Sofía Rupay Aros
 */
@Controller
public class ViewController {
    /**
     * Muestra la página principal del sitio.
     *
     * @return nombre de la plantilla de inicio.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
    /**
     * Muestra la página de inicio de sesión.
     *
     * @return nombre de la plantilla de login.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    /**
     * Muestra el formulario de registro de usuarios.
     *
     * @return nombre de la plantilla de registro.
     */
    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }
    /**
     * Muestra el catálogo público de vestidos.
     *
     * @return nombre de la plantilla del catálogo.
     */
    @GetMapping("/catalogo")
    public String catalogo() {
        return "catalogo";
    }
    /**
     * Muestra la vista de detalle de un vestido seleccionado.
     *
     * @return nombre de la plantilla de detalle.
     */
    @GetMapping("/detalle")
    public String detalle() {
        return "detalle";
    }
    /**
     * Muestra una página informativa cuando el usuario no tiene permisos.
     *
     * @return nombre de la plantilla de acceso denegado.
     */
    @GetMapping("/sin-permisos")
    public String sinPermisos() {
        return "sin-permisos";
    }
    /**
     * Muestra la vista del carrito de compras del cliente.
     *
     * @return nombre de la plantilla del carrito.
     */
    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
    }
    /**
     * Muestra el historial de compras del cliente.
     *
     * @return nombre de la plantilla de historial.
     */
    @GetMapping("/historial")
    public String historial() {
        return "historial";
    }
    /**
     * Muestra la vista de pago simulado.
     *
     * @return nombre de la plantilla de pago.
     */
    @GetMapping("/pago")
    public String pago() {
        return "pago";
    }
    /**
     * Muestra el panel principal del administrador.
     *
     * @return nombre de la plantilla del panel administrativo.
     */
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    /**
     * Muestra la vista para crear o editar productos.
     *
     * @return nombre de la plantilla de gestión de producto.
     */
    @GetMapping("/crear-producto")
    public String crearProducto() {
        return "crear-producto";
    }
    /**
     * Muestra el historial general de ventas para el administrador.
     *
     * @return nombre de la plantilla de historial de ventas.
     */
    @GetMapping("/historial-ventas")
    public String historialVentas() {
        return "historial-ventas";
    }
    /**
     * Muestra la vista administrativa de productos registrados.
     *
     * @return nombre de la plantilla para visualizar productos.
     */
    @GetMapping("/ver-productos")
    public String verProductos() {
        return "ver-productos";
    }
    /**
     * Muestra la vista intermedia encargada de guardar en el navegador
     * los datos obtenidos después de una autenticación exitosa con Google.
     *
     * @return nombre de la plantilla de éxito OAuth.
     */
    @GetMapping("/oauth-success")
    public String oauthSuccess() {
        return "oauth-success";
    }
    /**
     * Muestra el formulario para completar los datos faltantes de un usuario
     * autenticado mediante Google.
     *
     * @return nombre de la plantilla para completar perfil.
     */
    @GetMapping("/completar-perfil")
    public String completarPerfil() {
        return "completar-perfil";
    }
}