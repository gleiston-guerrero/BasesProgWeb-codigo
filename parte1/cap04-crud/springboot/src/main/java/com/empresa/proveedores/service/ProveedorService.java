package com.empresa.proveedores.service;

import com.empresa.proveedores.model.Proveedor;
import com.empresa.proveedores.repository.ProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

// @Service marca esta clase como componente de lógica de negocio.
// La diferencia con @Component es semántica: @Service comunica intención al equipo.
// @Transactional garantiza que los métodos de escritura se ejecuten dentro de
// una transacción de base de datos: si ocurre un error, los cambios se revierten (rollback).
@Service
@Transactional
public class ProveedorService {

	// Inyección de dependencias por constructor: Spring busca en su contexto un bean
	// que implemente ProveedorRepository y lo pasa al constructor. Con un único
	// constructor no hace falta @Autowired; esta forma es preferible a @Autowired en campos.
	private final ProveedorRepository repositorio;

	public ProveedorService(ProveedorRepository repositorio) {
		this.repositorio = repositorio;
	}

	// @Transactional(readOnly=true): optimización para consultas (sin transacción de escritura).
	@Transactional(readOnly = true)
	public List<Proveedor> listarTodos() {
		return repositorio.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Proveedor> buscarPorId(Long id) {
		return repositorio.findById(id);
	}

	public Proveedor crear(Proveedor proveedor) {
		// Regla de negocio: el RUC no puede estar duplicado (409 Conflict).
		verificarRucLibre(proveedor.getRuc(), null);
		return repositorio.save(proveedor);
	}

	public Optional<Proveedor> actualizar(Long id, Proveedor datos) {
		return repositorio.findById(id).map(existente -> {
			// El operador -> es una función lambda (similar a las arrow functions de JavaScript):
			// recibe 'existente' y devuelve el proveedor actualizado.
			// map() aplica la función solo si el Optional contiene un valor.
			// PUT reemplaza el recurso completo, RUC incluido, si no pertenece a otro.
			verificarRucLibre(datos.getRuc(), id);
			existente.setRuc(datos.getRuc());
			existente.setRazonSocial(datos.getRazonSocial());
			existente.setLimiteCredito(datos.getLimiteCredito());
			return repositorio.save(existente);
		});
	}

	public boolean eliminar(Long id) {
		if (!repositorio.existsById(id)) return false;
		repositorio.deleteById(id);
		return true;
	}

	// Lanza 409 Conflict si OTRO proveedor (distinto de idActual) ya usa el RUC.
	// ResponseStatusException lleva el código HTTP: Spring MVC lo aplica a la respuesta.
	// (En la Parte II se sustituye por excepciones propias del dominio.)
	private void verificarRucLibre(String ruc, Long idActual) {
		repositorio.findByRuc(ruc)
			.filter(otro -> !otro.getId().equals(idActual))
			.ifPresent(otro -> {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"Ya existe un proveedor con RUC: " + ruc);
			});
	}
}
