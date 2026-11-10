// Archivo: src/main/java/com/comercializadora/sgpc/repository/CategoriaRepository.java
package com.comercializadora.sgpc.repository;

import com.comercializadora.sgpc.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombre(String nombre);
}
