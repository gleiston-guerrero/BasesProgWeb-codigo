// Archivo: src/main/java/com/comercializadora/sgpc/exception/
//          NumeroContratoDuplicadoException.java
package com.comercializadora.sgpc.exception;

// Se lanza cuando el número de contrato ya existe -> 409
public class NumeroContratoDuplicadoException extends RuntimeException {
    public NumeroContratoDuplicadoException(String numero) {
        super("Ya existe un contrato con el número " + numero);
    }
}
