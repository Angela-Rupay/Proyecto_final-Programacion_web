package com.trajestipicos.indira.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @GetMapping("/catalogo")
    public String catalogo() {
        return "catalogo";
    }

    @GetMapping("/detalle")
    public String detalle() {
        return "detalle";
    }

    @GetMapping("/sin-permisos")
    public String sinPermisos() {
        return "sin-permisos";
    }

    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
    }

    @GetMapping("/historial")
    public String historial() {
        return "historial";
    }

    @GetMapping("/pago")
    public String pago() {
        return "pago";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/crear-producto")
    public String crearProducto() {
        return "crear-producto";
    }

    @GetMapping("/historial-ventas")
    public String historialVentas() {
        return "historial-ventas";
    }

    @GetMapping("/ver-productos")
    public String verProductos() {
        return "ver-productos";
    }

}