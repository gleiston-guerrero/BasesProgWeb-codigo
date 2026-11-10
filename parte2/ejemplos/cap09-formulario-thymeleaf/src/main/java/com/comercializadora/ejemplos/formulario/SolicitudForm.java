// Archivo: src/main/java/com/comercializadora/ejemplos/formulario/SolicitudForm.java
package com.comercializadora.ejemplos.formulario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// Objeto de respaldo del formulario: th:field lee sus propiedades al mostrarlo
// y Spring las rellena con los parámetros recibidos al enviarlo.
public class SolicitudForm {

    @Pattern(regexp = "\\d{13}", message = "El RUC debe tener 13 dígitos")
    private String ruc = "";

    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial = "";

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
}
