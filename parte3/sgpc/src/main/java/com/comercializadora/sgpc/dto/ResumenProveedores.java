// Archivo: src/main/java/com/comercializadora/sgpc/dto/ResumenProveedores.java
// Ejercicio E2.2
package com.comercializadora.sgpc.dto;

import java.math.BigDecimal;

public record ResumenProveedores(long total, BigDecimal limiteTotal,
                                 BigDecimal limiteMedio) {}
