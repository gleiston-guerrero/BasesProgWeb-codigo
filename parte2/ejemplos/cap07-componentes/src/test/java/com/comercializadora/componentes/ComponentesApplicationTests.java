// Archivo: src/test/java/com/comercializadora/componentes/ComponentesApplicationTests.java
package com.comercializadora.componentes;

import static org.junit.jupiter.api.Assertions.assertSame;

import com.comercializadora.componentes.beans.RegistroProveedores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ComponentesApplicationTests {

    @Autowired
    ApplicationContext ctx;

    @Test
    void losBeansSonSingletonsPorDefecto() {
        assertSame(ctx.getBean(RegistroProveedores.class), ctx.getBean(RegistroProveedores.class));
    }
}
