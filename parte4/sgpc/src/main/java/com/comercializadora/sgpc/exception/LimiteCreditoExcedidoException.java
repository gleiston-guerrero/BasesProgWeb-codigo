// Archivo: src/main/java/com/comercializadora/sgpc/exception/
//          LimiteCreditoExcedidoException.java
package com.comercializadora.sgpc.exception;

import java.math.BigDecimal;

// Se lanza cuando el monto supera el crédito disponible (RF-08) -> 409
public class LimiteCreditoExcedidoException extends RuntimeException {
    public LimiteCreditoExcedidoException(Long proveedorId, BigDecimal monto,
                                          BigDecimal disponible) {
        super("El monto " + monto.toPlainString() + " supera el crédito disponible ("
            + disponible.toPlainString() + ") del proveedor " + proveedorId);
    }
}
