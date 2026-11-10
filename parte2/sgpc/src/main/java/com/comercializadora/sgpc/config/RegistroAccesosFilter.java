// Archivo: src/main/java/com/comercializadora/sgpc/config/RegistroAccesosFilter.java
package com.comercializadora.sgpc.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// @Component registra el filtro para todas las rutas;
// @Order(1) fija su posición en la cadena (los números menores van antes).
@Component
@Order(1)
public class RegistroAccesosFilter extends OncePerRequestFilter {

    // Registrador SLF4J; con los starters, Spring Boot usa Logback por debajo.
    private static final Logger log =
            LoggerFactory.getLogger(RegistroAccesosFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        long inicio = System.currentTimeMillis();   // ANTES del controlador
        try {
            chain.doFilter(request, response);       // pasa al siguiente eslabón
        } finally {
            // DESPUÉS: se ejecuta aunque el resto de la cadena lance una excepción
            long duracion = System.currentTimeMillis() - inicio;
            log.info("{} {} -> {} ({} ms)", request.getMethod(),
                    request.getRequestURI(), response.getStatus(), duracion);
        }
    }
}
