// Archivo: src/main/java/com/comercializadora/componentes/ComponentesApplication.java
package com.comercializadora.componentes;

import com.comercializadora.componentes.beans.RegistroProveedores;
import com.comercializadora.componentes.ciclo.BeanObservado;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ComponentesApplication {

    private static final Logger log = LoggerFactory.getLogger(ComponentesApplication.class);

    public static void main(String[] args) {
        // run() crea el ApplicationContext; try-with-resources lo cierra al final
        try (ConfigurableApplicationContext ctx =
                SpringApplication.run(ComponentesApplication.class, args)) {

            // Pedir dos veces el mismo bean al contenedor
            RegistroProveedores r1 = ctx.getBean(RegistroProveedores.class);
            RegistroProveedores r2 = ctx.getBean(RegistroProveedores.class);
            log.info("¿Misma instancia? {}", r1 == r2);
            log.info("Nombre del bean: {}",
                    Arrays.toString(ctx.getBeanNamesForType(RegistroProveedores.class)));
            log.info(r1.registrar("0990000001001"));
            log.info(r1.registrar("099-123"));

            ctx.getBean(BeanObservado.class).usar();
        }   // al cerrarse el contexto se ejecutan los métodos @PreDestroy
    }
}
