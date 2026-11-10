package com.comercializadora.sgpc;

import java.math.BigDecimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// Aplicación mínima para comprobar HTTPS (Capítulo 2): un único endpoint de solo lectura.
@SpringBootApplication
@RestController
public class SgpcApplication {

    // Datos de un proveedor que se serializan a JSON en la respuesta
    record Proveedor(Long id, String ruc, String razonSocial, BigDecimal limiteCredito) {}

    // GET /api/proveedores/{id}: devuelve un proveedor fijo (sin base de datos)
    @GetMapping("/api/proveedores/{id}")
    Proveedor leer(@PathVariable Long id) {
        return new Proveedor(id, "1790012345001", "Importadora XL S.A.", new BigDecimal("50000.00"));
    }

    public static void main(String[] args) {
        SpringApplication.run(SgpcApplication.class, args);
    }
}
