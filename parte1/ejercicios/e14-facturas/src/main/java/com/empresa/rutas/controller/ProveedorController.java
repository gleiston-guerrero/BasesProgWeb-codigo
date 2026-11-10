package com.empresa.rutas.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProveedorController {

	@GetMapping("/proveedores")
	public Map<String, Integer> listar() {
		return Map.of("total", 142, "activos", 138);
	}

	@PostMapping("/proveedores")
	public ResponseEntity<Map<String,String>> crear(
	@RequestBody Map<String,Object> datos) {
		return ResponseEntity.status(201).body(Map.of("creado", "true"));
	}
}
