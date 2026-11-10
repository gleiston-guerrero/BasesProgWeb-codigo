package com.empresa.proveedores.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

// En Spring Boot, esta configuración es automática.
// En Spring MVC clásico, debe declararse explícitamente.
// @Configuration: esta clase es fuente de definiciones de beans.
// @EnableWebMvc: activa el módulo Spring MVC con sus configuraciones por defecto.
// @ComponentScan: registra los controladores; solo el paquete controller, porque
// los servicios ya están en el contexto raíz (AppConfig) y no deben duplicarse.
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.empresa.proveedores.controller")
public class WebConfig implements WebMvcConfigurer {

	// El método configureMessageConverters() configura los convertidores de mensajes HTTP.
	// JacksonJsonHttpMessageConverter (Jackson 3) convierte objetos Java a/desde JSON.
	// En Spring Boot, Jackson se configura automáticamente si está en el classpath.
	@Override
	public void configureMessageConverters(
	org.springframework.http.converter.HttpMessageConverters.ServerBuilder builder) {
		builder.withJsonConverter(new org.springframework.http.converter.json
		.JacksonJsonHttpMessageConverter());
	}
}
