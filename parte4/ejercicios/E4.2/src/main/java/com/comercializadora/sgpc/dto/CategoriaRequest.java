// Archivo: src/main/java/com/comercializadora/sgpc/dto/CategoriaRequest.java
package com.comercializadora.sgpc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 255: la longitud de categorias.nombre en la migración V1
public record CategoriaRequest(
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre admite como máximo 255 caracteres")
    String nombre) {}
