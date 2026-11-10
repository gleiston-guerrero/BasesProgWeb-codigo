// Archivo: src/main/java/com/comercializadora/sgpc/config/JwtConfig.java
package com.comercializadora.sgpc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    // Clave HMAC-SHA256 construida a partir de sgpc.jwt.secret.
    @Bean
    SecretKey claveJwt(@Value("${sgpc.jwt.secret}") String secreto) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException(
                "sgpc.jwt.secret debe tener al menos 32 bytes (HS256)");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    // Emisor: firma los tokens que entrega POST /auth/login.
    @Bean
    JwtEncoder jwtEncoder(SecretKey clave) {
        return NimbusJwtEncoder.withSecretKey(clave)
            .algorithm(MacAlgorithm.HS256)
            .build();
    }

    // Verificador: comprueba firma y caducidad en cada petición.
    @Bean
    JwtDecoder jwtDecoder(SecretKey clave) {
        return NimbusJwtDecoder.withSecretKey(clave)
            .macAlgorithm(MacAlgorithm.HS256)
            .build();
    }
}
