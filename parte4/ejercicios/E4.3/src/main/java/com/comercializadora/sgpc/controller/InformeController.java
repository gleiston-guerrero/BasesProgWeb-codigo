// Archivo: src/main/java/com/comercializadora/sgpc/controller/InformeController.java
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.dto.LineaCartera;
import com.comercializadora.sgpc.dto.PaginaCartera;
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

    // Ejercicio E4.3: sustituye al cartera() de la Práctica 14.
    // GET /api/informes/cartera?page=0&size=2 -> 200 con una página
    @GetMapping("/cartera")
    public PaginaCartera cartera(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size) {
        return servicio.carteraPaginada(page, size);
    }

    // Ejercicio E4.3, primera parte: el informe completo con @EntityGraph
    @GetMapping("/cartera/grafo")
    public List<LineaCartera> carteraConGrafo() {
        return servicio.carteraConGrafo();
    }

    // GET /api/informes/cartera/conteo -> 200 con el número de contratos
    @GetMapping("/cartera/conteo")
    public List<ProveedorResumen> conteo() {
        return servicio.conteo();
    }
}
