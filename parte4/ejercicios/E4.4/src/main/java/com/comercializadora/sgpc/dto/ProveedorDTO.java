// Archivo: src/main/java/com/comercializadora/sgpc/dto/ProveedorDTO.java
// dto/ProveedorDTO.java -- lo que la API DEVUELVE
package com.comercializadora.sgpc.dto;
import java.io.Serializable;
import java.math.BigDecimal;

// Un record es una clase inmutable y concisa: declara los campos
// y Java genera constructor, getters, equals, hashCode y toString.
public record ProveedorDTO(
    Long id,
    String ruc,
    String razonSocial,
    BigDecimal limiteCredito
) implements Serializable {}   // Ejercicio E4.4: Redis guarda bytes
