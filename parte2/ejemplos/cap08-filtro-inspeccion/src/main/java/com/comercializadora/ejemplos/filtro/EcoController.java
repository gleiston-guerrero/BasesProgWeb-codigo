// Archivo: src/main/java/com/comercializadora/ejemplos/filtro/EcoController.java
package com.comercializadora.ejemplos.filtro;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

// Controlador mínimo para que el filtro tenga una petición que inspeccionar
@RestController
public class EcoController {

    @GetMapping("/api/eco/{id}")
    public Map<String, Object> eco(@PathVariable Long id) {
        return Map.of("id", id);
    }

    // Cuerpo de unos 20 KB, mayor que el búfer de respuesta del contenedor
    @GetMapping("/api/eco/grande")
    public Map<String, Object> grande() {
        return Map.of("texto", "x".repeat(20_000));
    }
}
