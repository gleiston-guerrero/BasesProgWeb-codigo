// Archivo: src/test/java/com/comercializadora/sgpc/audit/ErroresServicioAspectTest.java
// Ejercicio E2.7
package com.comercializadora.sgpc.audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class ErroresServicioAspectTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void proveedorInexistente_dejaLineaWarn_yLaRespuestaSigueSiendo404(CapturedOutput salida)
            throws Exception {
        mvc.perform(get("/api/proveedores/99")).andExpect(status().isNotFound());
        assertThat(salida.getOut()).contains("WARN").contains(
            "[ERROR] ProveedorService.buscarPorId(..) -> ProveedorNoEncontradoException: "
            + "No existe un proveedor con id 99");
    }

    @Test
    void sinExcepcion_noHayLinea(CapturedOutput salida) throws Exception {
        mvc.perform(get("/api/proveedores")).andExpect(status().isOk());
        assertThat(salida.getOut()).doesNotContain("[ERROR]");
    }
}
