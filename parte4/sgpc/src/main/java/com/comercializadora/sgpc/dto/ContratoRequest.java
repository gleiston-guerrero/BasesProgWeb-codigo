// Archivo: src/main/java/com/comercializadora/sgpc/dto/ContratoRequest.java
package com.comercializadora.sgpc.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Datos que el cliente envía en POST y PUT .../contratos
public record ContratoRequest(
    @NotBlank(message = "El número de contrato es obligatorio")
    @Size(max = 30, message = "Máximo 30 caracteres")
    String numero,

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor que cero")
    BigDecimal monto,

    @NotNull(message = "La fecha de inicio es obligatoria")
    LocalDate fechaInicio,

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    LocalDate fechaVencimiento
) {
    // Capítulo 13: propiedad derivada "rangoFechasValido": Bean Validation
    // la lee con este método y exige que valga true.
    @AssertTrue(message = "La fecha de vencimiento debe ser posterior a la de inicio")
    public boolean isRangoFechasValido() {
        // Si falta alguna fecha, ya lo informan los @NotNull.
        if (fechaInicio == null || fechaVencimiento == null) {
            return true;
        }
        return fechaVencimiento.isAfter(fechaInicio);
    }
}
