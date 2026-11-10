// Archivo: src/main/java/com/comercializadora/sgpc/config/FiltrosConfig.java
package com.comercializadora.sgpc.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FiltrosConfig {

    // RedInternaFilter NO lleva @Component: este método lo crea y lo registra,
    // de modo que Spring Boot no lo aplique además a todas las rutas.
    @Bean
    public FilterRegistrationBean<RedInternaFilter> redInternaFilter() {
        FilterRegistrationBean<RedInternaFilter> registro =
                new FilterRegistrationBean<>(new RedInternaFilter());
        registro.addUrlPatterns("/admin/*");   // sintaxis de servlet, no /admin/**
        registro.setOrder(2);                  // después del registro de accesos
        return registro;
    }
}
