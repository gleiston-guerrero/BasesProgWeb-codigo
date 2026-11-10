package com.empresa.contratos.service;

import com.empresa.contratos.model.Contrato;
import com.empresa.contratos.repository.ContratoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContratoService {

	private final ContratoRepository repositorio;

	public ContratoService(ContratoRepository repositorio) {
		this.repositorio = repositorio;
	}

	@Transactional(readOnly = true)
	public List<Contrato> listarTodos() {
		return repositorio.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Contrato> buscarPorId(Long id) {
		return repositorio.findById(id);
	}

	public Contrato crear(Contrato contrato) {
		// Regla de negocio: el número de contrato no puede repetirse (409 Conflict).
		verificarNumeroLibre(contrato.getNumero(), null);
		return repositorio.save(contrato);
	}

	public Optional<Contrato> actualizar(Long id, Contrato datos) {
		return repositorio.findById(id).map(existente -> {
			verificarNumeroLibre(datos.getNumero(), id);
			existente.setNumero(datos.getNumero());
			existente.setDescripcion(datos.getDescripcion());
			existente.setValor(datos.getValor());
			existente.setProveedorId(datos.getProveedorId());
			return repositorio.save(existente);
		});
	}

	public boolean eliminar(Long id) {
		if (!repositorio.existsById(id)) return false;
		repositorio.deleteById(id);
		return true;
	}

	// 409 Conflict si OTRO contrato (distinto de idActual) ya usa el número.
	private void verificarNumeroLibre(String numero, Long idActual) {
		repositorio.findByNumero(numero)
			.filter(otro -> !otro.getId().equals(idActual))
			.ifPresent(otro -> {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"Ya existe un contrato con número: " + numero);
			});
	}
}
