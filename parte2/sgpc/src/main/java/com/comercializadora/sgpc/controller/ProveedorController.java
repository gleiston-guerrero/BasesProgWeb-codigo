// Archivo: src/main/java/com/comercializadora/sgpc/controller/ProveedorController.java
// Estado al final de la Parte II: Capítulo 7 + Práctica 6 (PUT y Location absoluta)
// + Ejercicios E2.1 (DELETE) y E2.2 (GET /resumen).
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.dto.*;
import com.comercializadora.sgpc.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    // Inyección por constructor (la forma recomendada de la Sección 7.1)
    private final ProveedorService servicio;

    public ProveedorController(ProveedorService servicio) {
        this.servicio = servicio;
    }

    // GET /api/proveedores -> 200 con la lista
    @GetMapping
    public List<ProveedorDTO> listar() {
        return servicio.listarTodos();
    }

    // Ejercicio E2.2
    // GET /api/proveedores/resumen -> 200; el literal prevalece sobre /{id}
    @GetMapping("/resumen")
    public ResumenProveedores resumen() {
        return servicio.resumen();
    }

    // GET /api/proveedores/{id} -> 200 con el proveedor
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.buscarPorId(id));
    }

    // POST /api/proveedores -> 201 con cabecera Location (Práctica 6: URI absoluta)
    @PostMapping
    public ResponseEntity<ProveedorDTO> crear(@RequestBody @Valid ProveedorRequest req) {
        ProveedorDTO creado = servicio.crear(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()   // URI absoluta
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(location).body(creado);
    }

    // Práctica 6
    // PUT /api/proveedores/{id} -> 200 con el DTO actualizado
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> actualizar(@PathVariable Long id,
            @RequestBody @Valid ProveedorRequest req) {
        return ResponseEntity.ok(servicio.actualizar(id, req));            // 200
    }

    // Ejercicio E2.1
    // DELETE /api/proveedores/{id} -> 204 sin cuerpo, o 404 si no existe
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBaja(@PathVariable Long id) {
        servicio.darDeBaja(id);
        return ResponseEntity.noContent().build();                      // 204
    }
}
