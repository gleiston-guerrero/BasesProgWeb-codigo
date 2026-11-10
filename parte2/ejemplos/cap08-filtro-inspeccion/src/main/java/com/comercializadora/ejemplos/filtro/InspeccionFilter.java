// Archivo: src/main/java/com/comercializadora/ejemplos/filtro/InspeccionFilter.java
package com.comercializadora.ejemplos.filtro;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class InspeccionFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(InspeccionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Lo que el filtro ve: el mensaje HTTP tal como llegó al servidor
        String metodo = request.getMethod();             // "GET"
        String ruta   = request.getRequestURI();         // "/api/eco/7"
        String query  = request.getQueryString();        // "detalle=si" o null
        String agente = request.getHeader("User-Agent"); // una cabecera, o null
        String ip     = request.getRemoteAddr();         // IP del cliente
        log.info("{} {} ?{} desde {} ({})", metodo, ruta, query, ip, agente);

        // Las cabeceras de la respuesta se fijan ANTES de ceder el control
        response.setHeader("X-Content-Type-Options", "nosniff");
        chain.doFilter(request, response);

        // A la vuelta, la respuesta puede estar ya confirmada (commit), y
        // entonces setHeader no tiene efecto; ocurre si el cuerpo desborda el búfer
        log.info("confirmada: {}", response.isCommitted());
    }
}
