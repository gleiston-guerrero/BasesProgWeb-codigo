// Archivo: src/main/java/com/comercializadora/componentes/config/RelojConfig.java
package com.comercializadora.componentes.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RelojConfig {

    // Clock es una clase del JDK: no puede anotarse con @Component,
    // así que el bean se declara con un método @Bean
    @Bean
    public Clock reloj() {
        return Clock.system(ZoneId.of("America/Guayaquil"));
    }
}
