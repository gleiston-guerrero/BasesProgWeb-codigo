// Archivo: src/main/java/com/comercializadora/sgpc/controller/AuthController.java
package com.comercializadora.sgpc.controller;

import com.comercializadora.sgpc.security.TokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    public record LoginRequest(
            @NotBlank(message = "El usuario es obligatorio") String username,
            @NotBlank(message = "La contraseña es obligatoria") String password) {}
    public record TokenResponse(String token) {}

    private final AuthenticationManager authenticationManager;
    private final TokenService tokens;

    public AuthController(AuthenticationManager authenticationManager,
                          TokenService tokens) {
        this.authenticationManager = authenticationManager;
        this.tokens = tokens;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                    req.username(), req.password()));
            return ResponseEntity.ok(new TokenResponse(tokens.emitir(auth)));
        } catch (AuthenticationException ex) {
            // Usuario inexistente o clave incorrecta: 401 sin más detalles.
            // Todo 401 debe llevar WWW-Authenticate (RFC 9110).
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
                .build();
        }
    }
}
