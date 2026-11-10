// Archivo: src/main/java/com/comercializadora/sgpc/model/Proveedor.java
// Entidad PROVISIONAL de la Parte II: la del CRUD de Spring Boot del
// Capítulo 4 (Proveedor.java, paquete com.empresa.proveedores.model)
// trasladada al paquete com.comercializadora.sgpc.model; solo cambia la
// línea package. La Parte III la sustituye por la entidad definitiva.
package com.comercializadora.sgpc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

// @Entity declara esta clase como una entidad JPA: se mapea a una tabla en la BD.
// @Table(name="...") especifica el nombre de la tabla (opcional; por defecto el nombre de la clase).
@Entity
@Table(name = "proveedores")
public class Proveedor {

    // @Id marca este campo como clave primaria.
    // @GeneratedValue(strategy=IDENTITY) delega la generación del ID a la BD (AUTO_INCREMENT).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(unique=true) crea un índice único en la BD para este campo.
    // @NotBlank: validación Bean Validation — el campo no puede ser nulo ni vacío.
    // @Size(min,max): valida la longitud de la cadena.
    @Column(unique = true, nullable = false)
    @NotBlank(message = "El RUC es obligatorio")
    @Size(min = 13, max = 13, message = "El RUC debe tener exactamente 13 dígitos")
    private String ruc;

    @Column(nullable = false)
    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;

    // Dinero con BigDecimal, nunca con Double/double: la coma flotante binaria no
    // representa exactamente valores como 1.10 y acumula errores de redondeo.
    // precision/scale: 12 dígitos en total, 2 decimales (DECIMAL(12,2) en la BD).
    // @Positive: valida que el valor sea mayor que cero.
    @Column(precision = 12, scale = 2)
    @Positive(message = "El límite de crédito debe ser mayor que cero")
    private BigDecimal limiteCredito;

    // Constructores: JPA exige uno público sin argumentos
    public Proveedor() {}

    public Proveedor(String ruc, String razonSocial, BigDecimal limiteCredito) {
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.limiteCredito = limiteCredito;
    }

    // Getters y setters (el IDE puede generarlos automáticamente)
    public Long getId() { return id; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String r) { this.razonSocial = r; }
    public BigDecimal getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(BigDecimal l) { this.limiteCredito = l; }
}
