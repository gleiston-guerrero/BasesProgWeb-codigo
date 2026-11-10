// En ContratoRepository se declara la consulta derivada:
//     long countByFechaInicioLessThanEqualAndFechaVencimientoGreaterThanEqual(
//             LocalDate inicio, LocalDate fin);
package com.empresa.contratos.controller;

import com.empresa.contratos.repository.ContratoRepository;
import com.empresa.contratos.repository.ProveedorRepository;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class DashboardController {
    private final ProveedorRepository proveedores;
    private final ContratoRepository contratos;
    DashboardController(ProveedorRepository p, ContratoRepository c) {
        this.proveedores = p; this.contratos = c;
    }
    @GetMapping("/api/dashboard")
    Map<String, Object> dashboard() {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        return Map.of(
            "totalProveedores", proveedores.count(),
            "contratosActivos", contratos
                .countByFechaInicioLessThanEqualAndFechaVencimientoGreaterThanEqual(hoy, hoy),
            "fechaServidor",    java.time.OffsetDateTime.now().toString());
    }
}
