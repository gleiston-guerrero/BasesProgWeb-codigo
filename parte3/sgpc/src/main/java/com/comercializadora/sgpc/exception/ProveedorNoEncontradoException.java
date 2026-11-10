// Archivo: src/main/java/com/comercializadora/sgpc/exception/ProveedorNoEncontradoException.java
package com.comercializadora.sgpc.exception;

// Se lanza cuando se busca un proveedor por un id que no existe -> 404
public class ProveedorNoEncontradoException extends RuntimeException {
    public ProveedorNoEncontradoException(Long id) {
        super("No existe un proveedor con id " + id);
    }
}
