// Archivo: src/test/java/com/comercializadora/sgpc/config/MantenimientoInterceptorTest.java
// Ejercicio E2.5
package com.comercializadora.sgpc.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "sgpc.mantenimiento=true")   // modo mantenimiento
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class MantenimientoInterceptorTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void escritura_recibe503ConRetryAfter_yDejaLaLineaDeTiempo(CapturedOutput salida)
            throws Exception {
        mvc.perform(post("/api/proveedores").contentType(MediaType.APPLICATION_JSON)
                .content("{\"ruc\":\"0990000001001\",\"razonSocial\":\"Aceros Litoral S.A.\","
                        + "\"limiteCredito\":15000}"))
           .andExpect(status().isServiceUnavailable())
           .andExpect(header().string("Retry-After", "3600"));
        // El TiempoInterceptor, anterior en la cadena, devolvió true en preHandle:
        // su afterCompletion se ejecuta aunque el siguiente haya devuelto false.
        assertThat(salida.getOut()).containsPattern("POST /api/proveedores -> \\d+ ms");
        // El controlador no llegó a ejecutarse: no hay línea de auditoría.
        assertThat(salida.getOut()).doesNotContain("[AUDITORIA]");
    }

    @Test
    void lectura_siguePermitida() throws Exception {
        mvc.perform(get("/api/proveedores")).andExpect(status().isOk());
    }
}
