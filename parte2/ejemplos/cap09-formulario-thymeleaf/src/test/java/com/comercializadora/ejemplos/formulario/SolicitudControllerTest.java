// Archivo: src/test/java/com/comercializadora/ejemplos/formulario/SolicitudControllerTest.java
package com.comercializadora.ejemplos.formulario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SolicitudControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void formularioVacio() throws Exception {
        mvc.perform(get("/solicitudes/nueva"))
           .andExpect(status().isOk())
           .andExpect(content().string(containsString("name=\"ruc\"")))
           .andExpect(content().string(not(containsString("th:"))));
    }

    @Test
    void datosInvalidos_vuelveAlFormularioConMensajesYValores() throws Exception {
        mvc.perform(post("/solicitudes").param("ruc", "123").param("razonSocial", ""))
           .andExpect(status().isOk())
           .andExpect(view().name("solicitud-form"))
           .andExpect(content().string(containsString("El RUC debe tener 13 dígitos")))
           .andExpect(content().string(containsString("La razón social es obligatoria")))
           .andExpect(content().string(containsString("value=\"123\"")));
    }

    @Test
    void datosValidos_redirigeConMensajeFlash() throws Exception {
        mvc.perform(post("/solicitudes").param("ruc", "0990000001001")
                                        .param("razonSocial", "Aceros Litoral S.A."))
           .andExpect(status().isFound())
           .andExpect(redirectedUrl("/solicitudes/nueva"))
           .andExpect(flash().attribute("mensaje",
                   "Solicitud de Aceros Litoral S.A. registrada"));
    }
}
