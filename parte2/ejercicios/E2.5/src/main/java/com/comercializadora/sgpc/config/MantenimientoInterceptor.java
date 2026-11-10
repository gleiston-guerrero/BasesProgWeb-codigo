// Archivo: src/main/java/com/comercializadora/sgpc/config/MantenimientoInterceptor.java
// Ejercicio E2.5: durante el mantenimiento, la API rechaza las escrituras con 503
package com.comercializadora.sgpc.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;

@Component
public class MantenimientoInterceptor implements HandlerInterceptor {

    private final boolean enMantenimiento;

    // sgpc.mantenimiento en application.properties; false si no se declara
    public MantenimientoInterceptor(
            @Value("${sgpc.mantenimiento:false}") boolean enMantenimiento) {
        this.enMantenimiento = enMantenimiento;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws IOException {
        if (!enMantenimiento || "GET".equals(request.getMethod())) {
            return true;                                   // lecturas: siguen
        }
        response.setHeader("Retry-After", "3600");         // ANTES de sendError
        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                "SGPC en mantenimiento: solo se admiten consultas");
        return false;                       // el controlador no se ejecuta
    }
}
