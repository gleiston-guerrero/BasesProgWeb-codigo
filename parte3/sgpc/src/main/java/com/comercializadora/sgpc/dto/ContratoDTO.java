// Archivo: src/main/java/com/comercializadora/sgpc/dto/ContratoDTO.java
package com.comercializadora.sgpc.dto;

import com.comercializadora.sgpc.model.Contrato;
import java.math.BigDecimal;
import java.time.LocalDate;

// Jackson serializa este record a JSON automáticamente.
// "vigente" y "diasParaVencer" son campos calculados, no columnas.
public record ContratoDTO(
    Long id,
    String numero,
    BigDecimal monto,
    LocalDate fechaInicio,
    LocalDate fechaVencimiento,
    boolean vigente,
    long diasParaVencer
) {
    // Fábrica estática: convierte la entidad en DTO y calcula los
    // campos derivados con los métodos de negocio de Contrato.
    public static ContratoDTO desde(Contrato c) {
        return new ContratoDTO(c.getId(), c.getNumero(), c.getMonto(),
            c.getFechaInicio(), c.getFechaVencimiento(),
            c.estaVigente(), c.diasParaVencer());
    }
}
