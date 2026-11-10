// Archivo: src/main/java/com/comercializadora/sgpc/config/UsuariosIniciales.java
package com.comercializadora.sgpc.config;

import com.comercializadora.sgpc.model.Rol;
import com.comercializadora.sgpc.model.Usuario;
import com.comercializadora.sgpc.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

// "!prod": se registra en desarrollo y en pruebas, NUNCA con el perfil prod.
@Configuration
@Profile("!prod")
public class UsuariosIniciales {

    @Bean
    CommandLineRunner crearUsuarios(UsuarioRepository usuarios,
                                    PasswordEncoder encoder) {
        return args -> {
            if (usuarios.count() == 0) {
                // Se guarda el hash BCrypt, nunca la clave en claro.
                // Claves de DESARROLLO: conocidas por todos, solo para H2 local.
                usuarios.save(new Usuario("admin",
                    encoder.encode("admin-clave-dev"), Rol.ADMIN));
                usuarios.save(new Usuario("gestor",
                    encoder.encode("gestor-clave-dev"), Rol.GESTOR));
            }
        };
    }
}
