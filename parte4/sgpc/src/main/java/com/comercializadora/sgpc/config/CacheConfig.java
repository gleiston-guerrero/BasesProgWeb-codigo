// Archivo: src/main/java/com/comercializadora/sgpc/config/CacheConfig.java
package com.comercializadora.sgpc.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

// @EnableCaching activa el procesamiento de las anotaciones de caché
// (@Cacheable, @CacheEvict, etc.) en toda la aplicación.
@Configuration
@EnableCaching
public class CacheConfig {
    // Sin otro proveedor en el classpath, Spring Boot configura el
    // proveedor "simple": mapas en memoria, sin caducidad.
    // Para producción, el SGPC añade Caffeine, con caducidad
    // (Sección "Caducidad de las entradas").
}
