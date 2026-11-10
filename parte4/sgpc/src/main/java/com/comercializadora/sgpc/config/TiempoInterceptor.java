// Archivo: src/main/java/com/comercializadora/sgpc/config/TiempoInterceptor.java
package com.comercializadora.sgpc.config;

import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TiempoInterceptor implements HandlerInterceptor {

    // Registrador SLF4J, como en el filtro de registro de accesos
    private static final Logger log =
            LoggerFactory.getLogger(TiempoInterceptor.class);

    // preHandle: ANTES del controlador. Devolver true continúa la cadena;
    // devolver false la detiene (útil para control de acceso).
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        request.setAttribute("inicio", System.currentTimeMillis());
        return true;
    }

    // afterCompletion: DESPUÉS de todo, incluso si hubo excepción.
    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
        long inicio = (Long) request.getAttribute("inicio");
        long duracion = System.currentTimeMillis() - inicio;
        log.info("{} {} -> {} ms", request.getMethod(),
                request.getRequestURI(), duracion);
    }
}
