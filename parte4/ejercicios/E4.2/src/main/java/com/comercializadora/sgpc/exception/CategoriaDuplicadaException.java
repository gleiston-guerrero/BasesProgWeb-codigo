// Archivo: src/main/java/com/comercializadora/sgpc/exception/CategoriaDuplicadaException.java
package com.comercializadora.sgpc.exception;

public class CategoriaDuplicadaException extends RuntimeException {
    public CategoriaDuplicadaException(String nombre) {
        super("Ya existe una categoría con el nombre " + nombre);   // 409
    }
}
