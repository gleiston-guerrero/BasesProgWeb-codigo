// Archivo: src/main/java/com/comercializadora/sgpc/controller/ContratoController.java
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.dto.*;
import com.comercializadora.sgpc.service.ContratoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/proveedores/{proveedorId}/contratos")
public class ContratoController {

    private final ContratoService servicio;

    public ContratoController(ContratoService servicio) {
        this.servicio = servicio;
    }

    // GET .../contratos?page=0&size=20  -> lista paginada (200), 400 o 404
    // Sin ?sort=..., orden fijo por vencimiento y, a igualdad, por id.
    @GetMapping
    public PaginaContratos listar(@PathVariable Long proveedorId,
            @PageableDefault(size = 20, sort = {"fechaVencimiento", "id"})
            Pageable pageable) {
        return servicio.listarPorProveedor(proveedorId, pageable);
    }

    // Ejercicio E3.2: GET .../contratos/vigentes -> 200, 400 o 404
    // El segmento literal /vigentes tiene prioridad sobre el patrón /{id}.
    @GetMapping("/vigentes")
    public PaginaContratos vigentes(@PathVariable Long proveedorId,
            @PageableDefault(size = 20, sort = {"fechaVencimiento", "id"})
            Pageable pageable) {
        return servicio.vigentesDeProveedor(proveedorId, pageable);   // 200
    }

    // GET .../contratos/{id}  -> un contrato (200) o 404
    @GetMapping("/{id}")
    public ResponseEntity<ContratoDTO> obtener(
            @PathVariable Long proveedorId, @PathVariable Long id) {
        return ResponseEntity.ok(servicio.buscar(proveedorId, id));
    }

    // POST .../contratos  -> crea (201) con Location; 400, 404 o 409
    @PostMapping
    public ResponseEntity<ContratoDTO> crear(
            @PathVariable Long proveedorId,
            @RequestBody @Valid ContratoRequest req) {
        ContratoDTO creado = servicio.crear(proveedorId, req);
        URI loc = URI.create("/api/proveedores/" + proveedorId
                + "/contratos/" + creado.id());
        return ResponseEntity.created(loc).body(creado);
    }

    // PUT .../contratos/{id}  -> reemplaza (200); 400, 404 o 409
    @PutMapping("/{id}")
    public ContratoDTO actualizar(
            @PathVariable Long proveedorId, @PathVariable Long id,
            @RequestBody @Valid ContratoRequest req) {
        return servicio.actualizar(proveedorId, id, req);
    }

    // DELETE .../contratos/{id}  -> elimina (204) o 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long proveedorId, @PathVariable Long id) {
        servicio.eliminar(proveedorId, id);
        return ResponseEntity.noContent().build();
    }
}
