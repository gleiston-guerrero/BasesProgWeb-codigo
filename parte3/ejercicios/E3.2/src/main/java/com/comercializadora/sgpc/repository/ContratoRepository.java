// Archivo: src/main/java/com/comercializadora/sgpc/repository/ContratoRepository.java
package com.comercializadora.sgpc.repository;
import com.comercializadora.sgpc.model.Contrato;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    // Contratos de un proveedor, una página cada vez:
    // ... WHERE proveedor_id = ? + límite y desplazamiento de Pageable
    Page<Contrato> findByProveedorId(Long proveedorId, Pageable pageable);

    // Un contrato SOLO si pertenece al proveedor indicado
    Optional<Contrato> findByIdAndProveedorId(Long id, Long proveedorId);

    // Unicidad del número: al crear, y al actualizar excluyendo el propio
    boolean existsByNumero(String numero);
    boolean existsByNumeroAndIdNot(String numero, Long id);

    // Ejercicio E3.2: contratos vigentes hoy, una página cada vez
    @Query("SELECT c FROM Contrato c WHERE c.proveedor.id = :id "
            + "AND c.fechaInicio <= CURRENT_DATE AND c.fechaVencimiento >= CURRENT_DATE")
    Page<Contrato> findVigentesPaginados(@Param("id") Long id, Pageable pageable);
}
