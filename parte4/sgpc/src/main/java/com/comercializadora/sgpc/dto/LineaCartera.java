// Archivo: src/main/java/com/comercializadora/sgpc/dto/LineaCartera.java
package com.comercializadora.sgpc.dto;

import java.util.List;

// Práctica 14: una línea del informe (un proveedor y sus contratos)
public record LineaCartera(String razonSocial, List<ContratoLinea> contratos) {}
