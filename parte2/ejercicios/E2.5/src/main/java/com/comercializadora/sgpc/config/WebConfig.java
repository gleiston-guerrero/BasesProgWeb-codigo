// Archivo: src/main/java/com/comercializadora/sgpc/config/WebConfig.java
// Ejercicio E2.5: se registra además el MantenimientoInterceptor
package com.comercializadora.sgpc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TiempoInterceptor tiempoInterceptor;
    private final MantenimientoInterceptor mantenimientoInterceptor;

    public WebConfig(TiempoInterceptor tiempoInterceptor,
                     MantenimientoInterceptor mantenimientoInterceptor) {
        this.tiempoInterceptor = tiempoInterceptor;
        this.mantenimientoInterceptor = mantenimientoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tiempoInterceptor)            // 1.º en la cadena
                .addPathPatterns("/api/**");   // solo rutas de la API
        registry.addInterceptor(mantenimientoInterceptor)     // 2.º en la cadena
                .addPathPatterns("/api/**");
    }
}
