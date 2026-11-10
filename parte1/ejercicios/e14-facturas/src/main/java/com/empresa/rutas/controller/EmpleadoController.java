package com.empresa.rutas.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// El prefijo /api/v1/rrhh/empleados se define a nivel de clase.
// La seguridad (equivalente al middleware auth:sanctum de Laravel) no se declara
// aquí: con Spring Security se configura aparte (Parte III); este proyecto no la usa.
@RestController
@RequestMapping("/api/v1/rrhh/empleados")
public class EmpleadoController {

	@GetMapping
	public List<?> index() {
		return List.of();
	}

	@PostMapping
	public ResponseEntity<?> store(@RequestBody Object r) {
		return ResponseEntity.status(201).build();
	}

	@GetMapping("/{id}")
	public Object show(@PathVariable Long id) {
		return Map.of("id", id);
	}

	@PutMapping("/{id}")
	public Object update(@PathVariable Long id, @RequestBody Object r) {
		return r;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> destroy(@PathVariable Long id) {
		return ResponseEntity.noContent().build();
	}
}
