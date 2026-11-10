// Archivo: src/main/java/com/comercializadora/sgpc/controller/InformeController.java
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.dto.LineaCartera;
import com.comercializadora.sgpc.dto.ProveedorResumen;
import com.comercializadora.sgpc.service.InformeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Práctica 14: informes de solo lectura (exigen token, como todo /api/**)
@RestController
@RequestMapping("/api/informes")
public class InformeController {

    private final InformeService servicio;

    public InformeController(InformeService servicio) {
        this.servicio = servicio;
    }

    // GET /api/informes/cartera -> 200 con proveedores y sus contratos
    @GetMapping("/cartera")
    public List<LineaCartera> cartera() {
        return servicio.cartera();
    }

    // GET /api/informes/cartera/conteo -> 200 con el número de contratos
    @GetMapping("/cartera/conteo")
    public List<ProveedorResumen> conteo() {
        return servicio.conteo();
    }
}
