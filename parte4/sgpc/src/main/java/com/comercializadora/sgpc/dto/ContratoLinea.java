// Archivo: src/main/java/com/comercializadora/sgpc/dto/ContratoLinea.java
package com.comercializadora.sgpc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

// Práctica 14: resumen de un contrato dentro del informe de cartera
public record ContratoLinea(String numero, BigDecimal monto,
                            LocalDate fechaVencimiento) {}
