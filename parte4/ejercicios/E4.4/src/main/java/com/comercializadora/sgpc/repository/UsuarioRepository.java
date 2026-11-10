// Archivo: src/main/java/com/comercializadora/sgpc/repository/UsuarioRepository.java
package com.comercializadora.sgpc.repository;

import com.comercializadora.sgpc.model.Rol;
import com.comercializadora.sgpc.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    // Ejercicio E4.4: número de usuarios con un rol
    long countByRol(Rol rol);
}
