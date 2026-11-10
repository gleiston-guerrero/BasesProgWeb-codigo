// Archivo: src/main/java/com/comercializadora/componentes/beans/RegistroProveedores.java
package com.comercializadora.componentes.beans;

import org.springframework.stereotype.Service;

@Service
public class RegistroProveedores {
    private final ValidadorRuc validador;

    // El contenedor pasa aquí el bean ValidadorRuc (sin Spring: new ValidadorRuc())
    public RegistroProveedores(ValidadorRuc validador) {
        this.validador = validador;
    }

    public String registrar(String ruc) {
        return (validador.esValido(ruc) ? "RUC aceptado: " : "RUC rechazado: ") + ruc;
    }
}
