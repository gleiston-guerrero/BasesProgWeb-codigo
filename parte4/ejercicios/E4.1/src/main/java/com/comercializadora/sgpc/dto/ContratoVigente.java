// Archivo: src/main/java/com/comercializadora/sgpc/dto/ContratoVigente.java
package com.comercializadora.sgpc.dto;

import com.comercializadora.sgpc.model.Contrato;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

// Solo datos que no cambian con el paso de los días (sin diasParaVencer)
public record ContratoVigente(Long id, String numero, BigDecimal monto,
        LocalDate fechaInicio, LocalDate fechaVencimiento) implements Serializable {
    public static ContratoVigente desde(Contrato c) {
        return new ContratoVigente(c.getId(), c.getNumero(), c.getMonto(),
                c.getFechaInicio(), c.getFechaVencimiento());
    }
}
