// Archivo: src/main/java/com/comercializadora/sgpc/exception/RucDuplicadoException.java
package com.comercializadora.sgpc.exception;

// Se lanza cuando se intenta registrar un RUC que ya existe -> 409
public class RucDuplicadoException extends RuntimeException {
    public RucDuplicadoException(String ruc) {
        super("Ya existe un proveedor con RUC " + ruc);
    }
}
