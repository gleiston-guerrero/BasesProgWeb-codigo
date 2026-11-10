package com.empresa.contratos.repository;

import com.empresa.contratos.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

	// SELECT * FROM contratos WHERE numero = ?
	Optional<Contrato> findByNumero(String numero);
}
