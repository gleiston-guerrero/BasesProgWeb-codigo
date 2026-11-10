// Archivo: src/main/java/com/comercializadora/sgpc/dto/ProveedorResumen.java
package com.comercializadora.sgpc.dto;

// Práctica 14: proyección con el número de contratos (COUNT devuelve Long)
public record ProveedorResumen(Long id, String razonSocial,
                               Long numeroContratos) {}
