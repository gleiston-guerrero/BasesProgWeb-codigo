// Archivo: src/main/java/com/comercializadora/sgpc/config/JwtClaveHealthIndicator.java
package com.comercializadora.sgpc.config;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;

// Al implementar HealthIndicator y anotarse con @Component, Spring
// integra esta comprobación en /actuator/health con el nombre
// "jwtClave" (nombre del bean sin el sufijo HealthIndicator).
@Component
public class JwtClaveHealthIndicator implements HealthIndicator {

    private final JwtEncoder encoder;   // los mismos beans de JwtConfig
    private final JwtDecoder decoder;

    public JwtClaveHealthIndicator(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public Health health() {
        try {
            // 1) Firmar un token de prueba de vida muy corta (sin roles)
            Instant ahora = Instant.now();
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .subject("health-check")
                    .issuedAt(ahora)
                    .expiresAt(ahora.plusSeconds(30))
                    .build();
            JwsHeader cabecera = JwsHeader.with(MacAlgorithm.HS256).build();
            String token = encoder
                    .encode(JwtEncoderParameters.from(cabecera, claims))
                    .getTokenValue();
            // 2) Verificarlo con el decodificador que usa la API
            decoder.decode(token);
            return Health.up()                          // estado "UP"
                    .withDetail("algoritmo", "HS256")
                    .build();
        } catch (JwtException ex) {
            // Firma o verificación fallidas: ningún login funcionaría.
            // El detalle es genérico: no revela la clave ni la excepción.
            return Health.down()                        // estado "DOWN"
                    .withDetail("motivo", "no se pudo firmar y verificar un JWT de prueba")
                    .build();
        }
    }
}
