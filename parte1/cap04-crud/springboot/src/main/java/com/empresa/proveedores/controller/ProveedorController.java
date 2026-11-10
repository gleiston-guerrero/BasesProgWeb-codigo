package com.empresa.proveedores.controller;

import com.empresa.proveedores.model.Proveedor;
import com.empresa.proveedores.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

// @RestController = @Controller + @ResponseBody:
//   Los métodos retornan objetos Java que Jackson serializa automáticamente a JSON.
// @RequestMapping("/api/proveedores"):
//   Prefijo de URI aplicado a todos los métodos de este controlador.
@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

	private final ProveedorService servicio;

	public ProveedorController(ProveedorService servicio) {
		this.servicio = servicio;
	}

	// GET /api/proveedores → lista todos los proveedores (HTTP 200)
	// List<Proveedor> se serializa automáticamente como array JSON.
	@GetMapping
	public List<Proveedor> listar() {
		return servicio.listarTodos();
	}

	// GET /api/proveedores/{id} → un proveedor (200) o 404 si no existe
	// @PathVariable extrae el {id} de la URI y lo convierte a Long.
	@GetMapping("/{id}")
	public ResponseEntity<Proveedor> leer(@PathVariable Long id) {
		return servicio.buscarPorId(id)
		.map(ResponseEntity::ok)               // 200 OK con el proveedor
		.orElse(ResponseEntity.notFound().build()); // 404 Not Found
	}

	// POST /api/proveedores → crea un proveedor (201 Created)
	// @RequestBody deserializa el JSON del cuerpo de la petición a Proveedor.
	// @Valid activa las validaciones de Bean Validation (@NotBlank, @Size, etc.):
	// si fallan, Spring responde 400; un RUC duplicado produce 409 (servicio).
	@PostMapping
	public ResponseEntity<Proveedor> crear(@RequestBody @Valid Proveedor proveedor) {
		Proveedor creado = servicio.crear(proveedor);
		// URI del nuevo recurso: buena práctica REST (cabecera Location en la respuesta)
		URI location = URI.create("/api/proveedores/" + creado.getId());
		return ResponseEntity.created(location).body(creado);
	}

	// PUT /api/proveedores/{id} → reemplaza un proveedor (200), 404 si no existe,
	// 400 si los datos no son válidos o 409 si el RUC pertenece a otro proveedor
	@PutMapping("/{id}")
	public ResponseEntity<Proveedor> actualizar(
	@PathVariable Long id,
	@RequestBody @Valid Proveedor datos) {
		return servicio.actualizar(id, datos)
		.map(ResponseEntity::ok)
		.orElse(ResponseEntity.notFound().build());
	}

	// DELETE /api/proveedores/{id} → elimina un proveedor (204) o 404
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		if (servicio.eliminar(id)) {
			return ResponseEntity.noContent().build(); // 204 No Content
		}
		return ResponseEntity.notFound().build();       // 404 Not Found
	}
}
