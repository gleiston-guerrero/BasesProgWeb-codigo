// Archivo: src/main/java/com/comercializadora/sgpc/dto/CategoriaDTO.java
package com.comercializadora.sgpc.dto;

import com.comercializadora.sgpc.model.Categoria;

public record CategoriaDTO(Long id, String nombre) {
    public static CategoriaDTO desde(Categoria c) {
        return new CategoriaDTO(c.getId(), c.getNombre());
    }
}
