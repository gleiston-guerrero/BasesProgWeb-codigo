// Archivo: src/main/java/com/comercializadora/componentes/web/EstadoV1Controller.java
package com.comercializadora.componentes.web;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EstadoV1Controller {

    @GetMapping("/v1/estado")
    @ResponseBody   // sin ella, el Map se tomaría como modelo de una vista
    public Map<String, String> estado() {
        return Map.of("estado", "activo");
    }
}
