// Archivo: src/test/java/com/comercializadora/componentes/config/CalculadoraVencimientosTest.java
package com.comercializadora.componentes.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class CalculadoraVencimientosTest {

    // Prueba unitaria sin Spring: el reloj fijo se pasa por el constructor
    @Test
    void cuentaLosDiasConUnRelojFijo() {
        Clock fijo = Clock.fixed(Instant.parse("2026-11-10T12:00:00Z"), ZoneId.of("America/Guayaquil"));
        CalculadoraVencimientos calc = new CalculadoraVencimientos(fijo);
        assertEquals(30, calc.diasHasta(LocalDate.of(2026, 12, 10)));
    }
}
