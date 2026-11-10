// Archivo: src/test/java/com/comercializadora/sgpc/config/RedInternaFilterTest.java
// Práctica 7
package com.comercializadora.sgpc.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc   // MockMvc con los filtros registrados en la aplicación
@ExtendWith(OutputCaptureExtension.class)   // captura System.out y System.err
class RedInternaFilterTest {

    @Autowired
    private MockMvc mvc;

    // Simula que la petición procede de la IP indicada. MockMvc deja vacío
    // el servletPath; se copia en él la ruta, como hace Tomcat con el
    // DispatcherServlet asociado a "/" (lo necesita la variante shouldNotFilter).
    private static RequestPostProcessor desde(String ip) {
        return request -> {
            request.setRemoteAddr(ip);
            request.setServletPath(request.getRequestURI());
            return request;
        };
    }

    @Test
    void ipExterna_recibe403EnElPanel(CapturedOutput salida) throws Exception {
        mvc.perform(get("/admin/proveedores").with(desde("10.0.0.5")))
           .andExpect(status().isForbidden());
        // La línea WARN del filtro: método, ruta e IP rechazada
        assertThat(salida.getOut()).contains("WARN")
            .contains("Acceso rechazado: GET /admin/proveedores desde 10.0.0.5");
    }

    @Test
    void ipDeLaRedInterna_accedeAlPanel() throws Exception {
        mvc.perform(get("/admin/proveedores").with(desde("192.168.10.25")))
           .andExpect(status().isOk());
    }

    @Test
    void ipExterna_noAfectaALaApi() throws Exception {
        mvc.perform(get("/api/proveedores").with(desde("10.0.0.5")))
           .andExpect(status().isOk());
    }
}
