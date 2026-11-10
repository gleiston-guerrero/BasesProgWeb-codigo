// Archivo: src/main/java/com/comercializadora/sgpc/model/Proveedor.java
package com.comercializadora.sgpc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// @Entity marca la clase como persistente; @Table fija el nombre de la tabla.
@Entity
@Table(name = "proveedores")
public class Proveedor {

    // @Id marca la clave primaria; @GeneratedValue delega su generación
    // a la base de datos (columna autoincremental).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column configura la columna: única, obligatoria, longitud 13.
    @Column(unique = true, nullable = false, length = 13)
    private String ruc;

    @Column(nullable = false, length = 200)
    private String razonSocial;

    // precision/scale definen un decimal de 12 dígitos, 2 decimales.
    @Column(precision = 12, scale = 2)
    private BigDecimal limiteCredito;

    // @Enumerated guarda el enum como texto ("ACTIVO") y no como número.
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoProveedor estado = EstadoProveedor.ACTIVO;

    // Relación uno-a-muchos: un proveedor tiene muchos contratos.
    @OneToMany(mappedBy = "proveedor",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contrato> contratos = new ArrayList<>();

    // JPA exige un constructor sin argumentos. Es público para que el
    // servicio y las pruebas puedan escribir new Proveedor() + setters.
    public Proveedor() {}
    // Constructor de conveniencia con los datos de negocio.
    public Proveedor(String ruc, String razonSocial, BigDecimal limiteCredito) {
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.limiteCredito = limiteCredito;
    }
    public Long getId() { return id; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String r) { this.razonSocial = r; }
    public BigDecimal getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(BigDecimal l) { this.limiteCredito = l; }
    public EstadoProveedor getEstado() { return estado; }
    public void setEstado(EstadoProveedor e) { this.estado = e; }
    public List<Contrato> getContratos() { return contratos; }

    // Regla de negocio (RF-08): crédito aún disponible = límite de
    // crédito menos la suma de los montos de los contratos no vencidos
    // (vigentes o con inicio futuro). BigDecimal: nunca double para dinero.
    public BigDecimal creditoDisponible() {
        BigDecimal comprometido = contratos.stream()
            .filter(Contrato::comprometeCredito)
            .map(Contrato::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal limite = (limiteCredito != null) ? limiteCredito : BigDecimal.ZERO;
        return limite.subtract(comprometido);
    }
}
