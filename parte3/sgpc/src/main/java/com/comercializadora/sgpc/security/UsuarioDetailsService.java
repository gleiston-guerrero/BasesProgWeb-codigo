// Archivo: src/main/java/com/comercializadora/sgpc/security/UsuarioDetailsService.java
package com.comercializadora.sgpc.security;

import com.comercializadora.sgpc.model.Usuario;
import com.comercializadora.sgpc.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarios;

    public UsuarioDetailsService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario u = usuarios.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
        // roles("ADMIN") se convierte en la autoridad ROLE_ADMIN
        return User.withUsername(u.getUsername())
            .password(u.getPassword())      // el hash BCrypt guardado
            .roles(u.getRol().name())
            .build();
    }
}
