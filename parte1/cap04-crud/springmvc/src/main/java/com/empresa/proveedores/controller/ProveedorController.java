package com.empresa.proveedores.controller;

import com.empresa.proveedores.model.Proveedor;
import com.empresa.proveedores.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

// La sintaxis del controlador es IDÉNTICA a Spring Boot:
// las anotaciones @GetMapping, @PostMapping, etc. pertenecen a Spring MVC,
// que Spring Boot usa internamente. La diferencia está en la configuración,
// no en el código del controlador.
@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

	private final ProveedorService servicio;

	public ProveedorController(ProveedorService servicio) {
		this.servicio = servicio;
	}

	@GetMapping
	public List<Proveedor> listar() {
		return servicio.listarTodos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Proveedor> leer(@PathVariable Long id) {
		return servicio.buscarPorId(id)
		.map(ResponseEntity::ok)
		.orElse(ResponseEntity.notFound().build());
	}

	// @Valid: con @EnableWebMvc y Hibernate Validator en el classpath, Spring MVC
	// valida el cuerpo igual que en Spring Boot (400 si falla); el servicio
	// compartido responde 409 ante un RUC duplicado.
	@PostMapping
	public ResponseEntity<Proveedor> crear(@RequestBody @Valid Proveedor p) {
		Proveedor creado = servicio.crear(p);
		// 201 Created con la cabecera Location del nuevo recurso, igual que en Spring Boot
		URI location = URI.create("/api/proveedores/" + creado.getId());
		return ResponseEntity.created(location).body(creado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Proveedor> actualizar(@PathVariable Long id,
			@RequestBody @Valid Proveedor p) {
		return servicio.actualizar(id, p)
		.map(ResponseEntity::ok)
		.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		return servicio.eliminar(id)
		? ResponseEntity.noContent().build()
		: ResponseEntity.notFound().build();
	}
}
