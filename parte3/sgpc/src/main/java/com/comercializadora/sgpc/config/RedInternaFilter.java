// Archivo: src/main/java/com/comercializadora/sgpc/config/RedInternaFilter.java
package com.comercializadora.sgpc.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.net.InetAddress;

// Sin @Component: lo crea y lo registra FiltrosConfig solo para /admin/*.
public class RedInternaFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(RedInternaFilter.class);

    // Red interna de Comercializadora Andina: 192.168.10.0/24
    private static final String PREFIJO_RED_INTERNA = "192.168.10.";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        if (esPermitida(ip)) {
            chain.doFilter(request, response);   // permitida: sigue la cadena
            return;
        }
        log.warn("Acceso rechazado: {} {} desde {}",
                request.getMethod(), request.getRequestURI(), ip);
        // 403 y NO se llama a chain.doFilter: la cadena se detiene aquí
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    // Red interna (IPv4) o la propia máquina (127.0.0.1 o ::1), para las
    // pruebas locales. Con una IP literal, getByName no consulta el DNS.
    private boolean esPermitida(String ip) throws IOException {
        return ip.startsWith(PREFIJO_RED_INTERNA)
                || InetAddress.getByName(ip).isLoopbackAddress();
    }
}
