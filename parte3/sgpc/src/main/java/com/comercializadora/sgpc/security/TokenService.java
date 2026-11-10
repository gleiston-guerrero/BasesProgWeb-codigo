// Archivo: src/main/java/com/comercializadora/sgpc/security/TokenService.java
package com.comercializadora.sgpc.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final long expiracionMinutos;

    public TokenService(JwtEncoder encoder,
            @Value("${sgpc.jwt.expiracion-minutos:60}") long expiracionMinutos) {
        this.encoder = encoder;
        this.expiracionMinutos = expiracionMinutos;
    }

    public String emitir(Authentication autenticacion) {
        Instant ahora = Instant.now();
        // Autoridad "ROLE_ADMIN" -> rol "ADMIN" (el token lleva el rol sin prefijo)
        List<String> roles = autenticacion.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(a -> a.startsWith("ROLE_"))
            .map(a -> a.substring("ROLE_".length()))
            .toList();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(autenticacion.getName())                          // sub
            .issuedAt(ahora)                                           // iat
            .expiresAt(ahora.plus(expiracionMinutos, ChronoUnit.MINUTES)) // exp
            .claim("roles", roles)                                     // roles
            .build();
        // La cabecera declara HS256, el algoritmo de la clave HMAC.
        JwsHeader cabecera = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(cabecera, claims))
            .getTokenValue();
    }
}
