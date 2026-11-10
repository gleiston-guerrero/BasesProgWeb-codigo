// Archivo: src/main/java/com/comercializadora/sgpc/controller/ProveedorViewController.java
// Capítulo 9 + Práctica 8 (método detalle) + Ejercicio E2.8 (búsqueda)
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.service.ProveedorService;
import org.springframework.stereotype.Controller;   // NO @RestController
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// @Controller (no @RestController): los métodos devuelven nombres de vista.
@Controller
public class ProveedorViewController {

    private final ProveedorService servicio;

    public ProveedorViewController(ProveedorService servicio) {
        this.servicio = servicio;
    }

    // GET /admin/proveedores?q=texto -> la misma plantilla, filtrada (E2.8)
    @GetMapping("/admin/proveedores")
    public String listar(@RequestParam(defaultValue = "") String q, Model model) {
        model.addAttribute("q", q);                  // para repintar el campo
        model.addAttribute("proveedores", servicio.buscarPorRazonSocial(q));
        return "proveedores";   // -> templates/proveedores.html
    }

    // Práctica 8
    @GetMapping("/admin/proveedores/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("proveedor", servicio.buscarPorId(id));
        return "proveedor-detalle";   // -> templates/proveedor-detalle.html
    }
}
