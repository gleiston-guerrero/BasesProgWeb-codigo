// Archivo: src/main/java/com/comercializadora/sgpc/controller/CategoriaController.java
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.dto.CategoriaDTO;
import com.comercializadora.sgpc.dto.CategoriaRequest;
import com.comercializadora.sgpc.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService servicio;
    public CategoriaController(CategoriaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<CategoriaDTO> listar() { return servicio.listar(); }

    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@RequestBody @Valid CategoriaRequest req) {
        CategoriaDTO creada = servicio.crear(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(location).body(creada);           // 201
    }
}
