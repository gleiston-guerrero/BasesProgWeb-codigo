package com.empresa.contratos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "El número de contrato es obligatorio")
    @Size(max = 20)
    private String numero;

    private String descripcion;

    @Column(precision = 12, scale = 2, nullable = false)
    @NotNull @Positive(message = "El valor debe ser mayor que cero")
    private BigDecimal valor;

    // En Java, camelCase; JPA lo guarda en la columna proveedor_id
    @Column(name = "proveedor_id")
    private Long proveedorId;

    // E1.2: vigencia del contrato (activo si fechaInicio <= hoy <= fechaVencimiento)
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;

    public Contrato() {}   // constructor vacío: JPA lo exige
    // getters y setters de los cinco campos (generados con el IDE)
    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public Long getProveedorId() { return proveedorId; }
    public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
}
