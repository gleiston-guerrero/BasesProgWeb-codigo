// Archivo: src/main/java/com/comercializadora/sgpc/repository/ProveedorRepository.java
package com.comercializadora.sgpc.repository;

import com.comercializadora.sgpc.model.EstadoProveedor;
import com.comercializadora.sgpc.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Al extender JpaRepository, esta interfaz hereda save, findById,
// findAll, deleteById, count, etc. SIN escribir implementación.
public interface ProveedorRepository
        extends JpaRepository<Proveedor, Long> {

    // Spring deriva la consulta del NOMBRE del método:
    // "findByRuc" -> SELECT * FROM proveedores WHERE ruc = ?
    Optional<Proveedor> findByRuc(String ruc);

    // Búsqueda parcial e insensible a mayúsculas por razón social.
    List<Proveedor> findByRazonSocialContainingIgnoreCase(String texto);

    // Cuando la consulta es compleja, se escribe explícitamente con @Query
    // usando JPQL (consulta sobre entidades, no sobre tablas).
    @Query("SELECT p FROM Proveedor p WHERE p.limiteCredito > :minimo")
    List<Proveedor> conLimiteSuperiorA(@Param("minimo") BigDecimal minimo);

    // ---- Práctica 9 (Listado "ProveedorRepository.java (ampliación)") ----

    // ... WHERE estado = ?
    List<Proveedor> findByEstado(EstadoProveedor estado);

    // SELECT count(...) ... WHERE estado = ?
    long countByEstado(EstadoProveedor estado);

    // ... WHERE estado = ? ORDER BY limite_credito DESC + límite de 3 filas
    List<Proveedor> findTop3ByEstadoOrderByLimiteCreditoDesc(EstadoProveedor estado);

    // JPQL: recorre la relación; DISTINCT evita proveedores repetidos.
    @Query("SELECT DISTINCT p FROM Proveedor p JOIN p.contratos c "
         + "WHERE c.fechaVencimiento < :fecha ORDER BY p.razonSocial")
    List<Proveedor> conContratosQueVencenAntesDe(@Param("fecha") LocalDate fecha);

    // ---- Ejercicio E3.1 ----

    // Consulta derivada: filtra por estado y ordena por razón social
    List<Proveedor> findByEstadoOrderByRazonSocialAsc(EstadoProveedor estado);

    // Consulta explícita con JPQL: límite de crédito por debajo de un umbral
    @Query("SELECT p FROM Proveedor p WHERE p.limiteCredito < :umbral "
            + "ORDER BY p.limiteCredito ASC")
    List<Proveedor> findConLimiteInferiorA(@Param("umbral") BigDecimal umbral);
}
