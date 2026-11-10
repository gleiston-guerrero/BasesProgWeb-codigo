// Archivo: src/main/java/com/comercializadora/sgpc/controller/ManejadorExcepciones.java
// Ejercicio E2.6: el manejador se limita a los controladores @RestController
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

// @RestControllerAdvice traduce a JSON las excepciones de los controladores
// REST; con annotations = RestController.class deja fuera las vistas.
@RestControllerAdvice(annotations = RestController.class)
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
