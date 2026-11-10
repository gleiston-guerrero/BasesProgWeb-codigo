// Archivo: src/main/java/com/comercializadora/sgpc/model/Categoria.java
package com.comercializadora.sgpc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

// Entidad con Lombok, como Contrato (Capítulo 10)
@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    // mappedBy = "categorias" indica que el lado dueño de la relación
    // es Contrato; aquí NO se crea otra tabla intermedia.
    @ManyToMany(mappedBy = "categorias")
    private Set<Contrato> contratos = new HashSet<>();
}
