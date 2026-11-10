// Archivo: src/main/java/com/comercializadora/componentes/beans/ValidadorRuc.java
package com.comercializadora.componentes.beans;

import org.springframework.stereotype.Component;

@Component   // un bean de propósito general, sin papel de capa concreto
public class ValidadorRuc {
    // Un RUC ecuatoriano tiene 13 dígitos (comprobación de formato, no de dígito verificador)
    public boolean esValido(String ruc) {
        return ruc != null && ruc.matches("\\d{13}");
    }
}
