// Archivo: src/main/java/com/comercializadora/sgpc/repository/UsuarioRepository.java
package com.comercializadora.sgpc.repository;

import com.comercializadora.sgpc.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
