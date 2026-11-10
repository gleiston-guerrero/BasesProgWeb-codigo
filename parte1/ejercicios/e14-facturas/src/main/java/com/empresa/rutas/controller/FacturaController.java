// src/main/java/com/empresa/rutas/controller/FacturaController.java
package com.empresa.rutas.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Prefijo de clase con una variable: todas las rutas reciben {proveedorId}
@RestController
@RequestMapping("/api/v1/proveedores/{proveedorId}/facturas")
public class FacturaController {

    // Cuerpo del POST: el importe es dinero (BigDecimal) y la fecha no puede ser futura
    record FacturaRequest(
        @NotBlank @Size(max = 20)                               String numero,
        @NotNull  @DecimalMin("0.01")                           BigDecimal importe,
        @NotNull  @PastOrPresent @JsonProperty("fecha_emision") LocalDate fechaEmision
    ) {}

    // GET .../facturas?estado=...&pagina=...  -> 200
    @GetMapping
    public Map<String, Object> listar(@PathVariable Long proveedorId,
            @RequestParam(defaultValue = "pendiente") String estado,
            @RequestParam(defaultValue = "1")         int pagina) {
        return Map.of("proveedor", proveedorId, "estado", estado, "pagina", pagina);
    }

    // GET .../facturas/{numero}  -> 200 (el número es texto: 001-001-000123)
    @GetMapping("/{numero}")
    public Map<String, Object> detalle(@PathVariable Long proveedorId,
            @PathVariable String numero) {
        return Map.of("proveedor", proveedorId, "numero", numero);
    }

    // POST .../facturas  -> 201 + Location; 400 si el cuerpo no es válido
    @PostMapping
    public ResponseEntity<Map<String, Object>> registrar(@PathVariable Long proveedorId,
            @Valid @RequestBody FacturaRequest req) {
        URI ubicacion = URI.create(
                "/api/v1/proveedores/" + proveedorId + "/facturas/" + req.numero());
        return ResponseEntity.created(ubicacion)
                .body(Map.of("proveedor", proveedorId, "numero", req.numero()));
    }
}
