// Archivo: src/main/java/com/comercializadora/sgpc/config/WebConfig.java
package com.comercializadora.sgpc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TiempoInterceptor tiempoInterceptor;

    public WebConfig(TiempoInterceptor tiempoInterceptor) {
        this.tiempoInterceptor = tiempoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tiempoInterceptor)
                .addPathPatterns("/api/**");   // solo rutas de la API
    }
}
