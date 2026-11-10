// Archivo: src/main/java/com/comercializadora/sgpc/controller/ManejadorExcepciones.java
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

// @RestControllerAdvice intercepta las excepciones lanzadas por
// CUALQUIER controlador y las traduce a respuestas HTTP uniformes.
@RestControllerAdvice
public class ManejadorExcepciones {

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
}
