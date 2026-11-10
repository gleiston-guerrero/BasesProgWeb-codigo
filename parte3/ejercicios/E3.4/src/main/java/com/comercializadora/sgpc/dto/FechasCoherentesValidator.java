// Archivo: src/main/java/com/comercializadora/sgpc/dto/FechasCoherentesValidator.java
package com.comercializadora.sgpc.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Valida la interfaz, no un DTO concreto: sirve para todos los que la implementen
public class FechasCoherentesValidator
        implements ConstraintValidator<FechasCoherentes, ConRangoFechas> {
    @Override
    public boolean isValid(ConRangoFechas obj, ConstraintValidatorContext ctx) {
        if (obj == null || obj.fechaInicio() == null || obj.fechaVencimiento() == null) {
            return true;                       // lo informan los @NotNull
        }
        return obj.fechaVencimiento().isAfter(obj.fechaInicio());
    }
}
