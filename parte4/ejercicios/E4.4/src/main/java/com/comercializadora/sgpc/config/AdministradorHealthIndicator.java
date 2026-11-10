// Archivo: src/main/java/com/comercializadora/sgpc/config/
//          AdministradorHealthIndicator.java
package com.comercializadora.sgpc.config;

import com.comercializadora.sgpc.model.Rol;
import com.comercializadora.sgpc.repository.UsuarioRepository;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

// En UsuarioRepository se añade la consulta derivada:
//     long countByRol(Rol rol);

@Component
public class AdministradorHealthIndicator implements HealthIndicator {

    private final UsuarioRepository usuarios;

    public AdministradorHealthIndicator(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public Health health() {
        long administradores = usuarios.countByRol(Rol.ADMIN);
        // Siempre UP: la falta de administrador es un dato de negocio,
        // no un fallo que impida a la instancia atender peticiones.
        Health.Builder estado = Health.up()
                .withDetail("administradores", administradores);
        if (administradores == 0) {
            estado.withDetail("advertencia", "no existe ningún usuario con rol ADMIN");
        }
        return estado.build();
    }
}
