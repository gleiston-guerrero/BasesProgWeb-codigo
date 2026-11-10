// Archivo: src/main/java/com/comercializadora/sgpc/repository/ContratoRepository.java
package com.comercializadora.sgpc.repository;
import com.comercializadora.sgpc.model.Contrato;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
