package com.empresa.rutas.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Capítulo 5: reúne los fragmentos «Parámetros de ruta en Spring Boot» y
// «Parámetros de consulta y cuerpo de la petición en Spring Boot».
@RestController
@RequestMapping("/api")
public class ContratoController {

	// Parámetro obligatorio
	@GetMapping("/contratos/{id}")
	public Map<String,Long> detalle(@PathVariable Long id) {
		return Map.of("contrato_id", id);
	}

	// Spring Boot valida el tipo automáticamente;
	// si el valor no es un Long, devuelve 400 Bad Request

	// Parámetro opcional usando query param (más idiomático en Spring).
	// Como date('Y') en Laravel: si no llega ?anio=..., se usa el año en curso.
	// (defaultValue es un texto fijo en la anotación; el año en curso se calcula aquí)
	@GetMapping("/reportes")
	public Map<String,Integer> reporte(
	@RequestParam(required = false) Integer anio) {
		int valor = (anio != null) ? anio : java.time.Year.now().getValue();
		return Map.of("anio", valor);
	}

	// Query strings con @RequestParam: GET /api/contratos?estado=...&anio=...&pagina=...
	@GetMapping("/contratos")
	public Map<String,Object> listar(
	@RequestParam(defaultValue = "todos")   String  estado,
	@RequestParam(required = false)         Integer anio,    // sin valor: año en curso
	@RequestParam(defaultValue = "1")       int     pagina) {

		int anioConsulta = (anio != null) ? anio : java.time.Year.now().getValue();
		return Map.of("estado", estado, "anio", anioConsulta, "pagina", pagina);
	}

	// Cuerpo JSON con @RequestBody y validación @Valid (400 si falla).
	// Importaciones: jakarta.validation.constraints.*, java.math.BigDecimal,
	// java.time.LocalDate y com.fasterxml.jackson.annotation.JsonProperty.
	// monto es dinero: BigDecimal, no Double (la coma flotante no es exacta).
	record ContratoRequest(
	@NotBlank @Size(max = 20)      String numero,
	@NotNull  @DecimalMin("0.01")  BigDecimal monto,
	// @JsonProperty: en el JSON se llama fecha_fin, como en Laravel y Django
	@NotNull  @Future  @JsonProperty("fecha_fin")  LocalDate fechaFin
	) {}

	@PostMapping("/contratos")
	public ResponseEntity<?> crear(@Valid @RequestBody ContratoRequest req) {
		return ResponseEntity.status(201)
		.body(Map.of("mensaje", "Contrato recibido"));
	}
}
