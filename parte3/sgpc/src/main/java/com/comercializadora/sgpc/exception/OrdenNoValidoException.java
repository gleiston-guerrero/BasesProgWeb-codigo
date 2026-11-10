// Archivo: src/main/java/com/comercializadora/sgpc/exception/
//          OrdenNoValidoException.java
package com.comercializadora.sgpc.exception;

// Se lanza cuando ?sort= pide un atributo no permitido -> 400
public class OrdenNoValidoException extends RuntimeException {
    public OrdenNoValidoException(String atributo) {
        super("No se puede ordenar por " + atributo);
    }
}
