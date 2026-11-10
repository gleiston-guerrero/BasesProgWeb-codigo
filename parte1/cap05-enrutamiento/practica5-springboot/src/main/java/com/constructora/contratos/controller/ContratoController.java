package com.constructora.contratos.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Solución de la Práctica 5 (Capítulo 5): mapa de rutas del recurso contratos
// bajo el prefijo común /api/v1/contratos, con respuestas simuladas.
@RestController
@RequestMapping("/api/v1/contratos")
public class ContratoController {

	// Cuerpo de la creación: mismas reglas que en la Sección de query strings y cuerpo
	record ContratoRequest(
			@NotBlank @Size(max = 20) String numero,
			@NotNull @DecimalMin("0.01") BigDecimal monto,
			@NotNull @Future @JsonProperty("fecha_fin") LocalDate fechaFin) {}

	// Cuerpo de la actualización parcial: solo el monto
	record MontoRequest(@NotNull @DecimalMin("0.01") BigDecimal monto) {}

	// GET /api/v1/contratos?estado=...&anio=... -> 200
	@GetMapping
	public Map<String, Object> listar(
			@RequestParam(defaultValue = "todos") String estado,
			@RequestParam(required = false) Integer anio) {
		int anioConsulta = (anio != null) ? anio : Year.now().getValue();
		return Map.of("estado", estado, "anio", anioConsulta, "contratos", List.of());
	}

	// GET /api/v1/contratos/{numero} -> 200
	@GetMapping("/{numero}")
	public Map<String, Object> obtener(@PathVariable String numero) {
		return Map.of("numero", numero, "monto", new BigDecimal("12500.00"));
	}

	// POST /api/v1/contratos -> 201 + Location; cuerpo inválido -> 400
	@PostMapping
	public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody ContratoRequest req) {
		return ResponseEntity.created(URI.create("/api/v1/contratos/" + req.numero()))
				.body(Map.of("numero", req.numero(), "monto", req.monto()));
	}

	// PATCH /api/v1/contratos/{numero} -> 200 (actualización parcial: solo el monto)
	@PatchMapping("/{numero}")
	public Map<String, Object> actualizarMonto(@PathVariable String numero,
			@Valid @RequestBody MontoRequest req) {
		return Map.of("numero", numero, "monto", req.monto());
	}

	// DELETE /api/v1/contratos/{numero} -> 204
	@DeleteMapping("/{numero}")
	public ResponseEntity<Void> darDeBaja(@PathVariable String numero) {
		return ResponseEntity.noContent().build();
	}
}
