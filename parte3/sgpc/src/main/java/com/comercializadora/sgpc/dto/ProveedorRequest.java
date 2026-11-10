// Archivo: src/main/java/com/comercializadora/sgpc/dto/ProveedorRequest.java
package com.comercializadora.sgpc.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProveedorRequest(

    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(regexp = "\\d{13}", message = "El RUC debe tener 13 dígitos")
    String ruc,

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 200, message = "Máximo 200 caracteres")
    String razonSocial,

    @NotNull(message = "El límite de crédito es obligatorio")
    @Positive(message = "El límite debe ser mayor que cero")
    BigDecimal limiteCredito
) {}
