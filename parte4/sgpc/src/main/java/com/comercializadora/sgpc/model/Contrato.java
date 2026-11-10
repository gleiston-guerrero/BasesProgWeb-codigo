// Archivo: src/main/java/com/comercializadora/sgpc/model/Contrato.java
package com.comercializadora.sgpc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

// @Getter y @Setter generan en tiempo de compilación los métodos de
// acceso de TODOS los campos. @NoArgsConstructor genera el constructor
// público sin argumentos que exige JPA y que usa el servicio
// (new Contrato() + setters).
@Entity
@Table(name = "contratos")
@Getter
@Setter
@NoArgsConstructor
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String numero;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    // Ambas fechas son obligatorias: las reglas de negocio las usan.
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    // Lado "muchos" de la relación. fetch LAZY: el proveedor se carga
    // solo cuando se accede a él, no automáticamente (rendimiento).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    // Capítulo 15: @JoinTable describe la tabla intermedia "contrato_categoria" con
    // las dos claves foráneas; Hibernate mantiene sus filas. La tabla
    // la crea ddl-auto en desarrollo o la migración V1 de Flyway.
    @ManyToMany
    @JoinTable(name = "contrato_categoria",
        joinColumns = @JoinColumn(name = "contrato_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    private Set<Categoria> categorias = new HashSet<>();

    // Los métodos de negocio se escriben normalmente: Lombok solo
    // genera getters/setters, nunca la lógica propia del dominio.
    // Regla de negocio: el contrato está vigente si hoy está dentro
    // del rango [fechaInicio, fechaVencimiento].
    public boolean estaVigente() {
        // Un contrato aún sin fechas (recién creado en memoria) no está vigente.
        if (fechaInicio == null || fechaVencimiento == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaVencimiento);
    }

    // Regla de negocio (RF-08): el contrato compromete crédito mientras
    // no haya vencido, esté ya vigente o empiece en una fecha futura.
    public boolean comprometeCredito() {
        return fechaVencimiento != null
            && !LocalDate.now().isAfter(fechaVencimiento);
    }

    // Días que faltan para el vencimiento (negativo si ya venció; 0 si
    // la fecha aún no se ha asignado).
    public long diasParaVencer() {
        if (fechaVencimiento == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    // Capítulo 15, métodos auxiliares: mantienen sincronizados AMBOS lados
    // de la relación bidireccional en memoria (Hibernate solo persiste el
    // lado dueño, pero el objeto Categoria también debe verlo).
    public void agregarCategoria(Categoria c) {
        categorias.add(c);
        c.getContratos().add(this);
    }

    public void quitarCategoria(Categoria c) {
        categorias.remove(c);
        c.getContratos().remove(this);
    }
}
