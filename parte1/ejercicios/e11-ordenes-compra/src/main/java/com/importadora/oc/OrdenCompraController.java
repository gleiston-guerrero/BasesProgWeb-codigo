// src/main/java/com/importadora/oc/OrdenCompraController.java
package com.importadora.oc;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes-compra")   // prefijo común de todas las rutas
public class OrdenCompraController {

    // GET con filtros opcionales (estado, proveedor, rango de fechas) -> 200
    @GetMapping
    public List<Map<String, Object>> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long proveedor,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde, // 2026-01-31
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return List.of(
            // importes con BigDecimal, nunca con double (Capítulo 4)
            Map.of("id", 1, "proveedor", 7, "estado", "pendiente", "total", new BigDecimal("1500.00")),
            Map.of("id", 2, "proveedor", 3, "estado", "aprobada",  "total", new BigDecimal("9800.00")));
    }

    // GET por id -> 200 / 404
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detalle(@PathVariable Long id) {
        if (id > 100) return ResponseEntity.notFound().build();           // 404 simulado
        return ResponseEntity.ok(Map.of("id", id, "estado", "pendiente"));
    }

    // POST creación -> 201 + Location
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Map<String, Object> cuerpo) {
        long nuevoId = 101;                                               // id simulado
        return ResponseEntity.created(URI.create("/api/ordenes-compra/" + nuevoId))
                             .body(Map.of("id", nuevoId, "estado", "pendiente"));
    }

    // Transiciones de estado como subrecursos de acción (PATCH) -> 200 / 409
    @PatchMapping("/{id}/aprobacion")
    public ResponseEntity<Map<String, Object>> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("id", id, "estado", "aprobada"));
    }

    @PatchMapping("/{id}/rechazo")
    public ResponseEntity<Map<String, Object>> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("id", id, "estado", "rechazada"));
    }

    @PatchMapping("/{id}/anulacion")
    public ResponseEntity<Map<String, Object>> anular(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("id", id, "estado", "anulada"));
    }
}
