// Archivo: src/test/java/com/comercializadora/sgpc/repository/ProveedorRepositoryTest.java
package com.comercializadora.sgpc.repository;

import com.comercializadora.sgpc.model.Proveedor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

// Solo entidades y repositorios, sobre una base H2 embebida.
// Cada prueba se ejecuta en una transacción que se revierte al final.
@DataJpaTest
class ProveedorRepositoryTest {
    
    @Autowired
    private ProveedorRepository repositorio;
    
    @Test
    void findByRuc_devuelveElProveedorGuardado() {
        // ARRANGE: el dato lo crea la propia prueba
        var proveedor = new Proveedor();
        proveedor.setRuc("0991234567001");
        proveedor.setRazonSocial("Aceros del Pacífico");
        proveedor.setLimiteCredito(new BigDecimal("5000.00"));
        repositorio.save(proveedor);
        
        // ACT
        var encontrado = repositorio.findByRuc("0991234567001");
        
        // ASSERT: lo encuentra por su RUC... y no inventa otros
        assertTrue(encontrado.isPresent());
        assertEquals("Aceros del Pacífico", encontrado.get().getRazonSocial());
        assertTrue(repositorio.findByRuc("0000000000000").isEmpty());
    }
}
