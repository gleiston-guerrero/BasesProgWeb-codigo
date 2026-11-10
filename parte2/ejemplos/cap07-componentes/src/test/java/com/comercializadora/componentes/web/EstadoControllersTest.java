// Archivo: src/test/java/com/comercializadora/componentes/web/EstadoControllersTest.java
package com.comercializadora.componentes.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class EstadoControllersTest {

    // Ambos controladores producen la misma respuesta JSON
    private final MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new EstadoV1Controller(), new EstadoV2Controller()).build();

    @Test
    void lasDosVersionesDevuelvenElMismoJson() throws Exception {
        for (String ruta : new String[] {"/v1/estado", "/v2/estado"}) {
            mvc.perform(get(ruta))
               .andExpect(status().isOk())
               .andExpect(content().json("{\"estado\":\"activo\"}"));
        }
    }
}
