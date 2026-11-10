// Archivo: src/test/java/com/comercializadora/sgpc/service/ProveedorServiceTest.java
package com.comercializadora.sgpc.service;

import com.comercializadora.sgpc.dto.ProveedorRequest;
import com.comercializadora.sgpc.exception.RucDuplicadoException;
import com.comercializadora.sgpc.model.Proveedor;
import com.comercializadora.sgpc.repository.ProveedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class) activa Mockito en JUnit Jupiter.
@ExtendWith(MockitoExtension.class)
class ProveedorServiceTest {
	
	// @Mock crea una simulación del repositorio.
	@Mock
	private ProveedorRepository repositorio;
	
	// @InjectMocks crea el servicio e inyecta el mock anterior.
	@InjectMocks
	private ProveedorService servicio;
	
	@Test
	void crear_conRucDuplicado_lanzaExcepcion() {
		// ARRANGE: el repositorio simulado ya tiene un proveedor con ese RUC
		var existente = new Proveedor();
		when(repositorio.findByRuc("0991234567001"))
		.thenReturn(Optional.of(existente));
		
		var peticion = new ProveedorRequest(
		"0991234567001", "Aceros del Pacífico", new BigDecimal("5000"));
		
		// ACT + ASSERT: crear debe lanzar la excepción de RUC duplicado
		assertThrows(RucDuplicadoException.class,
		() -> servicio.crear(peticion));
		
		// VERIFY: el servicio NO debe haber intentado guardar nada
		verify(repositorio, never()).save(any());
	}
}
