package com.empresa.proveedores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication es una anotación compuesta que activa tres mecanismos:
//   @SpringBootConfiguration: marca esta clase como fuente de configuración.
//   @EnableAutoConfiguration: activa la autoconfiguración (detecta JPA, web, etc.).
//   @ComponentScan: escanea el paquete actual y los subpaquetes buscando
//     @Component, @Service, @Repository, @Controller y los registra en el contexto.
@SpringBootApplication
public class ProveedoresApplication {

	public static void main(String[] args) {
		// SpringApplication.run() inicia el contenedor de Spring (ApplicationContext),
		// arranca Tomcat embebido en el puerto 8080 y publica todos los endpoints.
		SpringApplication.run(ProveedoresApplication.class, args);
	}
}
