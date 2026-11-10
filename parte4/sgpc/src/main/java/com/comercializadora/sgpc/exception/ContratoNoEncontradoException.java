// Archivo: src/main/java/com/comercializadora/sgpc/exception/
//          ContratoNoEncontradoException.java
package com.comercializadora.sgpc.exception;

public class ContratoNoEncontradoException extends RuntimeException {
    public ContratoNoEncontradoException(Long proveedorId, Long id) {
        super("No existe el contrato " + id + " del proveedor " + proveedorId);
    }
}
