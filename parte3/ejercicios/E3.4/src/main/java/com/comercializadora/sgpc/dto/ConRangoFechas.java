// Archivo: src/main/java/com/comercializadora/sgpc/dto/ConRangoFechas.java
package com.comercializadora.sgpc.dto;

import java.time.LocalDate;

// Cualquier DTO con fecha de inicio y de vencimiento
public interface ConRangoFechas {
    LocalDate fechaInicio();
    LocalDate fechaVencimiento();
}
