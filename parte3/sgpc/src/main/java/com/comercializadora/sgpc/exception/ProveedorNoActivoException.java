// Archivo: src/main/java/com/comercializadora/sgpc/exception/
//          ProveedorNoActivoException.java
package com.comercializadora.sgpc.exception;

// Se lanza al crear un contrato para un proveedor que no está ACTIVO -> 409
public class ProveedorNoActivoException extends RuntimeException {
    public ProveedorNoActivoException(Long proveedorId) {
        super("El proveedor " + proveedorId + " no está activo");
    }
}
