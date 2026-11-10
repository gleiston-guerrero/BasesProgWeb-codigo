// Archivo: src/main/java/com/comercializadora/componentes/config/CalculadoraVencimientos.java
package com.comercializadora.componentes.config;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class CalculadoraVencimientos {

    private final Clock reloj;   // el bean declarado en RelojConfig

    public CalculadoraVencimientos(Clock reloj) {
        this.reloj = reloj;
    }

    // Días que faltan para una fecha, medidos con el reloj inyectado
    public long diasHasta(LocalDate fecha) {
        return ChronoUnit.DAYS.between(LocalDate.now(reloj), fecha);
    }
}
