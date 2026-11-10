// Archivo: src/test/java/com/comercializadora/sgpc/service/ContratoServiceTest.java
package com.comercializadora.sgpc.service;

import com.comercializadora.sgpc.dto.ContratoRequest;
import com.comercializadora.sgpc.exception.LimiteCreditoExcedidoException;
import com.comercializadora.sgpc.model.Contrato;
import com.comercializadora.sgpc.model.Proveedor;
import com.comercializadora.sgpc.repository.ContratoRepository;
import com.comercializadora.sgpc.repository.ProveedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContratoServiceTest {

    @Mock
    private ContratoRepository contratos;

    @Mock
    private ProveedorRepository proveedores;

    // El servicio real, con los dos repositorios simulados
    @InjectMocks
    private ContratoService servicio;

    // Límite de 10000 y un contrato vigente de 8000: disponible = 2000.
    // Las fechas son relativas a hoy para que la prueba no caduque.
    private Proveedor proveedorConContratoVigente() {
        var p = new Proveedor("0991234567001", "Aceros del Pacífico",
                new BigDecimal("10000.00"));
        var vigente = new Contrato();
        vigente.setMonto(new BigDecimal("8000.00"));
        vigente.setFechaInicio(LocalDate.now().minusMonths(1));
        vigente.setFechaVencimiento(LocalDate.now().plusMonths(11));
        vigente.setProveedor(p);
        p.getContratos().add(vigente);
        return p;
    }

    @Test
    void creditoDisponible_descuentaLosContratosNoVencidos() {
        // ARRANGE: un contrato vencido (no compromete crédito) y otro
        // que empieza el mes próximo (sí lo compromete)
        var p = proveedorConContratoVigente();
        var vencido = new Contrato();
        vencido.setMonto(new BigDecimal("5000.00"));
        vencido.setFechaInicio(LocalDate.now().minusYears(2));
        vencido.setFechaVencimiento(LocalDate.now().minusYears(1));
        p.getContratos().add(vencido);
        var futuro = new Contrato();
        futuro.setMonto(new BigDecimal("1500.00"));
        futuro.setFechaInicio(LocalDate.now().plusMonths(1));
        futuro.setFechaVencimiento(LocalDate.now().plusMonths(7));
        p.getContratos().add(futuro);

        // ACT + ASSERT: 10000 - (8000 + 1500) = 500; el vencido no cuenta.
        // compareTo compara el valor, no la escala
        assertEquals(0, new BigDecimal("500").compareTo(p.creditoDisponible()));
    }

    @Test
    void crear_conMontoMayorQueElCreditoDisponible_lanzaExcepcion() {
        // ARRANGE: el proveedor 1 existe y el número de contrato es nuevo
        when(proveedores.findById(1L))
                .thenReturn(Optional.of(proveedorConContratoVigente()));
        when(contratos.existsByNumero("C-2026-002")).thenReturn(false);
        var peticion = new ContratoRequest("C-2026-002",
                new BigDecimal("2500.00"),             // disponible: 2000
                LocalDate.now(), LocalDate.now().plusMonths(6));

        // ACT + ASSERT: la regla RF-08 rechaza el contrato
        assertThrows(LimiteCreditoExcedidoException.class,
                () -> servicio.crear(1L, peticion));

        // VERIFY: el contrato no llegó a guardarse
        verify(contratos, never()).save(any());
    }
}
