// Archivo: src/main/java/com/comercializadora/sgpc/dto/FechasCoherentes.java
package com.comercializadora.sgpc.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

// Restricción a nivel de clase: se coloca sobre el tipo completo
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FechasCoherentesValidator.class)
public @interface FechasCoherentes {
    String message() default "La fecha de vencimiento debe ser posterior a la de inicio";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
