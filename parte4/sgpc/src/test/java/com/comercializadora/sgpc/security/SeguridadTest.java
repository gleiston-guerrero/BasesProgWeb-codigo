// Archivo: src/test/java/com/comercializadora/sgpc/security/SeguridadTest.java
package com.comercializadora.sgpc.security;

import com.comercializadora.sgpc.model.Proveedor;
import com.comercializadora.sgpc.repository.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SeguridadTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ProveedorRepository repositorio;
    
    private Long id;   // proveedor creado para cada prueba
    
    @BeforeEach
    void crearProveedorDePrueba() {
        var proveedor = new Proveedor();
        proveedor.setRuc("0990000000001");
        proveedor.setRazonSocial("Proveedor de prueba");
        proveedor.setLimiteCredito(new BigDecimal("1000.00"));
        id = repositorio.save(proveedor).getId();
    }
    
    @Test
    void listar_sinToken_devuelve401() throws Exception {
        // Sin token: el resource server rechaza la petición.
        mvc.perform(get("/api/proveedores"))
                .andExpect(status().isUnauthorized())            // 401
                .andExpect(header().exists("WWW-Authenticate"));
    }
    
    @Test
    void listar_conJwtDeGestor_devuelve200() throws Exception {
        // jwt() simula un token válido; las autoridades se indican
        // ya convertidas, con el prefijo ROLE_ que produce el converter.
        mvc.perform(get("/api/proveedores")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_GESTOR"))))
                .andExpect(status().isOk());                     // 200
    }
    
    @Test
    void darDeBaja_sinToken_devuelve401() throws Exception {
        mvc.perform(delete("/api/proveedores/{id}", id))
                .andExpect(status().isUnauthorized());           // 401
    }
    
    // Un GESTOR está autenticado, pero DELETE /api/** exige ADMIN.
    @Test
    @WithMockUser(roles = "GESTOR")
    void darDeBaja_comoGestor_devuelve403() throws Exception {
        mvc.perform(delete("/api/proveedores/{id}", id))
                .andExpect(status().isForbidden());              // 403
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void darDeBaja_comoAdmin_devuelve204() throws Exception {
        mvc.perform(delete("/api/proveedores/{id}", id))
                .andExpect(status().isNoContent());              // 204
    }
}
