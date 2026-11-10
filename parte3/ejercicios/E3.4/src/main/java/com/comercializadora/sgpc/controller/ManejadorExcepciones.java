// Archivo: src/main/java/com/comercializadora/sgpc/controller/ManejadorExcepciones.java
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// @RestControllerAdvice intercepta las excepciones lanzadas por
// CUALQUIER controlador y las traduce a respuestas HTTP uniformes.
@RestControllerAdvice
public class ManejadorExcepciones {

    // ---- Parte II (Capítulo 7) ----

    // Cuando un servicio lanza ProveedorNoEncontradoException -> 404
    @ExceptionHandler(ProveedorNoEncontradoException.class)
    public ResponseEntity<Map<String,String>> noEncontrado(
            ProveedorNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", ex.getMessage()));
    }

    // Cuando un servicio lanza RucDuplicadoException -> 409 Conflict
    @ExceptionHandler(RucDuplicadoException.class)
    public ResponseEntity<Map<String,String>> rucDuplicado(
            RucDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", ex.getMessage()));
    }

    // ---- Parte III, Capítulo 11 (contratos) ----

    @ExceptionHandler(ContratoNoEncontradoException.class)
    public ResponseEntity<Map<String,String>> contratoNoEncontrado(
            ContratoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)       // 404
            .body(Map.of("error", ex.getMessage()));
    }

    // Un único método para los tres conflictos con el estado actual (409)
    @ExceptionHandler({NumeroContratoDuplicadoException.class,
            LimiteCreditoExcedidoException.class, ProveedorNoActivoException.class})
    public ResponseEntity<Map<String,String>> conflictoContrato(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)        // 409
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(OrdenNoValidoException.class)
    public ResponseEntity<Map<String,String>> ordenNoValido(OrdenNoValidoException ex) {
        return ResponseEntity.badRequest()                       // 400
            .body(Map.of("error", ex.getMessage()));
    }

    // ---- Parte III, Capítulo 13 (validación) ----

    // Captura los fallos de @Valid y los devuelve como campo -> mensajes.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,List<String>>> manejarValidacion(
            MethodArgumentNotValidException ex) {
        Map<String,List<String>> errores = new LinkedHashMap<>();
        // Un FieldError por cada regla incumplida: un mismo campo puede
        // traer varios (p. ej. ruc vacío viola @NotBlank y @Pattern).
        // computeIfAbsent crea la lista la primera vez y luego la reutiliza,
        // así ningún mensaje sobrescribe a otro del mismo campo.
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.computeIfAbsent(error.getField(), k -> new ArrayList<>())
                   .add(error.getDefaultMessage()));
        // Ejercicio E3.4: errores globales (restricciones a nivel de clase)
        ex.getBindingResult().getGlobalErrors().forEach(error ->
            errores.computeIfAbsent("global", k -> new ArrayList<>())
                   .add(error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);   // 400
    }

    // Última red de seguridad: una restricción UNIQUE (o de otro tipo)
    // violada en la base de datos, p. ej. por dos altas simultáneas.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,String>> violacionIntegridad(
            DataIntegrityViolationException ex) {
        // No se devuelve ex.getMessage(): contiene SQL y nombres internos.
        return ResponseEntity.status(HttpStatus.CONFLICT)        // 409
            .body(Map.of("error",
                "La operación entra en conflicto con datos ya registrados"));
    }
}
