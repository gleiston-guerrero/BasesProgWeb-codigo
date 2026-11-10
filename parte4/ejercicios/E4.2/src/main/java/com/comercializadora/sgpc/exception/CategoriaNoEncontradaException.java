// Archivo: src/main/java/com/comercializadora/sgpc/exception/CategoriaNoEncontradaException.java
package com.comercializadora.sgpc.exception;

public class CategoriaNoEncontradaException extends RuntimeException {
    public CategoriaNoEncontradaException(Long id) {
        super("No existe una categoría con id " + id);              // 404
    }
}
