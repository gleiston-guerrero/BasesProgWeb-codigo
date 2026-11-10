// Archivo: src/test/java/com/comercializadora/sgpc/controller/BusquedaPanelTest.java
// Ejercicio E2.8
package com.comercializadora.sgpc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // base vacía
class BusquedaPanelTest {

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void crearProveedores() throws Exception {
        crear("{\"ruc\":\"0990000001001\",\"razonSocial\":\"Aceros Litoral S.A.\",\"limiteCredito\":15000}");
        crear("{\"ruc\":\"1790000002001\",\"razonSocial\":\"Insumos Andinos\",\"limiteCredito\":8000}");
    }

    private void crear(String json) throws Exception {
        mvc.perform(post("/api/proveedores").contentType(MediaType.APPLICATION_JSON).content(json))
           .andExpect(status().isCreated());
    }

    @Test
    void sinParametro_listaTodos() throws Exception {
        mvc.perform(get("/admin/proveedores"))
           .andExpect(status().isOk())
           .andExpect(content().string(containsString("Aceros Litoral S.A.")))
           .andExpect(content().string(containsString("Insumos Andinos")))
           .andExpect(content().string(not(containsString("Resultados para"))));
    }

    @Test
    void conParametro_filtraSinDistinguirMayusculas() throws Exception {
        mvc.perform(get("/admin/proveedores").param("q", "ACERO"))
           .andExpect(status().isOk())
           .andExpect(content().string(containsString("Aceros Litoral S.A.")))
           .andExpect(content().string(not(containsString("Insumos Andinos"))))
           .andExpect(content().string(containsString("value=\"ACERO\"")));
    }

    @Test
    void textoConEtiquetas_seMuestraEscapado() throws Exception {
        mvc.perform(get("/admin/proveedores").param("q", "<b>x</b>"))
           .andExpect(status().isOk())
           .andExpect(content().string(containsString("&lt;b&gt;x&lt;/b&gt;")))
           .andExpect(content().string(not(containsString("<b>x</b>"))))
           .andExpect(content().string(containsString("No hay proveedores registrados.")));
    }
}
