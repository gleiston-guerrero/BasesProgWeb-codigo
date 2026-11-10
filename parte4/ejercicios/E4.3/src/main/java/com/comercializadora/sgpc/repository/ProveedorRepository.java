// Archivo: src/main/java/com/comercializadora/sgpc/repository/ProveedorRepository.java
package com.comercializadora.sgpc.repository;

import com.comercializadora.sgpc.dto.ProveedorResumen;
import com.comercializadora.sgpc.model.EstadoProveedor;
import com.comercializadora.sgpc.model.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    // ---- Capítulo 15 (Listado "ProveedorRepository.java --- JOIN FETCH") ----
    // JOIN FETCH carga proveedores y sus contratos en UNA consulta,
    // en lugar de 1 + N. Desde Hibernate 6 no hace falta DISTINCT:
    // Hibernate devuelve cada proveedor una sola vez.
    @Query("SELECT p FROM Proveedor p "
    + "LEFT JOIN FETCH p.contratos")
    List<Proveedor> buscarTodosConContratos();

    // ---- Práctica 14: el recuento se calcula en la base de datos ----
    @Query("SELECT new com.comercializadora.sgpc.dto.ProveedorResumen("
         + "p.id, p.razonSocial, COUNT(c)) "
         + "FROM Proveedor p LEFT JOIN p.contratos c "
         + "GROUP BY p.id, p.razonSocial")
    List<ProveedorResumen> resumenConConteo();

    // ---- Ejercicio E4.3 ----
    // Misma carga anticipada, declarada con un grafo sobre una consulta derivada
    @EntityGraph(attributePaths = "contratos")
    List<Proveedor> findAllByOrderByRazonSocialAsc();

    // 1) Solo los ids de la página; orden y límite llegan en el Pageable
    @Query(value = "SELECT p.id FROM Proveedor p",
           countQuery = "SELECT COUNT(p) FROM Proveedor p")
    Page<Long> buscarIds(Pageable pageable);

    // 2) Esos proveedores con sus contratos, en el mismo orden
    @Query("SELECT p FROM Proveedor p LEFT JOIN FETCH p.contratos "
         + "WHERE p.id IN :ids ORDER BY p.razonSocial, p.id")
    List<Proveedor> buscarConContratosPorIds(@Param("ids") List<Long> ids);
}
