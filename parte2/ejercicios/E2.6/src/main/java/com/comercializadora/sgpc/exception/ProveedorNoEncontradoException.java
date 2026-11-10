// Archivo: src/main/java/com/comercializadora/sgpc/exception/ProveedorNoEncontradoException.java
// Ejercicio E2.6: @ResponseStatus fija el 404 cuando ningún manejador la atiende
package com.comercializadora.sgpc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Se lanza cuando se busca un proveedor por un id que no existe -> 404
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProveedorNoEncontradoException extends RuntimeException {
    public ProveedorNoEncontradoException(Long id) {
        super("No existe un proveedor con id " + id);
    }
}
