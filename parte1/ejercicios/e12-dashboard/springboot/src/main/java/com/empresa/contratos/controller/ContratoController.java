package com.empresa.contratos.controller;

import com.empresa.contratos.model.Contrato;
import com.empresa.contratos.service.ContratoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

	private final ContratoService servicio;

	public ContratoController(ContratoService servicio) {
		this.servicio = servicio;
	}

	@GetMapping
	public List<Contrato> listar() {
		return servicio.listarTodos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Contrato> leer(@PathVariable Long id) {
		return servicio.buscarPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Contrato> crear(@RequestBody @Valid Contrato contrato) {
		Contrato creado = servicio.crear(contrato);
		URI location = URI.create("/api/contratos/" + creado.getId());
		return ResponseEntity.created(location).body(creado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Contrato> actualizar(@PathVariable Long id,
			@RequestBody @Valid Contrato datos) {
		return servicio.actualizar(id, datos)
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
