package com.empresa.contratos.repository;

import com.empresa.contratos.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

	// SELECT * FROM contratos WHERE numero = ?
	Optional<Contrato> findByNumero(String numero);

	// E1.2: contratos vigentes en una fecha (consulta derivada)
	long countByFechaInicioLessThanEqualAndFechaVencimientoGreaterThanEqual(
			LocalDate inicio, LocalDate fin);
}
