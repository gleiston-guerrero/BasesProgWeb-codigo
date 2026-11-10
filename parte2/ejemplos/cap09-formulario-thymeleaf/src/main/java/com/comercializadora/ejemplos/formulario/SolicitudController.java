// Archivo: src/main/java/com/comercializadora/ejemplos/formulario/SolicitudController.java
package com.comercializadora.ejemplos.formulario;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SolicitudController {

    // GET: muestra el formulario vacío
    @GetMapping("/solicitudes/nueva")
    public String formulario(Model model) {
        model.addAttribute("solicitud", new SolicitudForm());
        return "solicitud-form";
    }

    // POST: valida; si hay errores, vuelve a la misma vista con los
    // valores escritos y los mensajes; si no, redirige (Post/Redirect/Get)
    @PostMapping("/solicitudes")
    public String registrar(@Valid @ModelAttribute("solicitud") SolicitudForm solicitud,
                            BindingResult errores,          // justo después
                            RedirectAttributes redireccion) {
        if (errores.hasErrors()) {
            return "solicitud-form";                         // 200, con errores
        }
        redireccion.addFlashAttribute("mensaje",
                "Solicitud de " + solicitud.getRazonSocial() + " registrada");
        return "redirect:/solicitudes/nueva";                // 302
    }
}
