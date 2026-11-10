// Archivo: src/main/java/com/comercializadora/sgpc/dto/ProveedorDTO.java
// DTO de salida: lo que la API DEVUELVE
package com.comercializadora.sgpc.dto;
import com.comercializadora.sgpc.model.EstadoProveedor;
import java.io.Serializable;
import java.math.BigDecimal;

// Un record es una clase inmutable y concisa: declara los campos
// y Java genera constructor, getters, equals, hashCode y toString.
public record ProveedorDTO(
    Long id,
    String ruc,
    String razonSocial,
    BigDecimal limiteCredito,
    EstadoProveedor estado     // Capítulo 12: hace visible la baja lógica
) implements Serializable {}   // Ejercicio E4.4: Redis guarda bytes
