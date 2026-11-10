// Archivo: src/main/java/com/comercializadora/sgpc/dto/ContratoRequest.java
package com.comercializadora.sgpc.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Ejercicio E3.4: la anotación va sobre el tipo; los accesores del record
// fechaInicio() y fechaVencimiento() implementan la interfaz. Se elimina
// el método isRangoFechasValido() del Capítulo 13.
@FechasCoherentes
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
) implements ConRangoFechas {}
