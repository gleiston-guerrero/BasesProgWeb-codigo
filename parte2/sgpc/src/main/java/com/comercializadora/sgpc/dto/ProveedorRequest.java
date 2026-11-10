// Archivo: src/main/java/com/comercializadora/sgpc/dto/ProveedorRequest.java
// dto/ProveedorRequest.java -- lo que la API RECIBE al crear
package com.comercializadora.sgpc.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProveedorRequest(
    @NotBlank(message = "El RUC es obligatorio")
    @Size(min = 13, max = 13, message = "El RUC debe tener 13 dígitos")
    String ruc,

    @NotBlank(message = "La razón social es obligatoria")
    String razonSocial,

    @NotNull(message = "El límite de crédito es obligatorio")
    @Positive(message = "El límite de crédito debe ser mayor que cero")
    BigDecimal limiteCredito
) {}
