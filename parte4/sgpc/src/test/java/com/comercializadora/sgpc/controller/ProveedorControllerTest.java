// Archivo: src/test/java/com/comercializadora/sgpc/controller/ProveedorControllerTest.java
package com.comercializadora.sgpc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")                 // H2 en memoria y clave de prueba
@Transactional                          // revierte los datos de cada prueba
@WithMockUser(roles = "GESTOR")         // usuario autenticado de prueba
class ProveedorControllerTest {
    
    private static final String PROVEEDOR_VALIDO = """
            {
                "ruc": "0991234567001",
                "razonSocial": "Aceros del Pacífico",
                "limiteCredito": 5000.00
            }
            """;
    
    @Autowired
    private MockMvc mvc;
    
    @Test
    void crearProveedor_conDatosValidos_devuelve201() throws Exception {
        // perform() envía la petición; andExpect() verifica la respuesta.
        mvc.perform(post("/api/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PROVEEDOR_VALIDO))
                .andExpect(status().isCreated())                 // 201
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.ruc").value("0991234567001"));
    }
    
    @Test
    void crearProveedor_conRucRepetido_devuelve409() throws Exception {
        // ARRANGE: la propia prueba crea el proveedor que luego se repite
        mvc.perform(post("/api/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PROVEEDOR_VALIDO))
                .andExpect(status().isCreated());
        
        mvc.perform(post("/api/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PROVEEDOR_VALIDO))
                .andExpect(status().isConflict());               // 409
    }
    
    @Test
    void crearProveedor_conRucInvalido_devuelve400() throws Exception {
        // RUC de 5 dígitos: viola la regla @Pattern del DTO (cap. 13)
        String cuerpo = """
                {
                    "ruc": "12345",
                    "razonSocial": "Empresa X",
                    "limiteCredito": 5000.00
                }
                """;
        
        mvc.perform(post("/api/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isBadRequest())              // 400
                .andExpect(jsonPath("$.ruc").exists());          // mensaje de error
    }
}
