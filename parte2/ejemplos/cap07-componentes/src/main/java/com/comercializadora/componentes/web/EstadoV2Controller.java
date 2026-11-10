// Archivo: src/main/java/com/comercializadora/componentes/web/EstadoV2Controller.java
package com.comercializadora.componentes.web;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController   // = @Controller + @ResponseBody en todos sus métodos
public class EstadoV2Controller {

    @GetMapping("/v2/estado")
    public Map<String, String> estado() {
        return Map.of("estado", "activo");
    }
}
