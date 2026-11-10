// Archivo: src/test/java/com/comercializadora/sgpc/controller/PaginaErrorTest.java
// Ejercicio E2.6: servidor real, porque MockMvc no reenvía a /error
package com.comercializadora.sgpc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaginaErrorTest {

    @LocalServerPort
    private int puerto;

    private HttpResponse<String> get(String ruta, String accept) throws Exception {
        HttpRequest peticion = HttpRequest.newBuilder(
                URI.create("http://localhost:" + puerto + ruta))
            .header("Accept", accept).build();
        return HttpClient.newHttpClient().send(peticion, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void panel_proveedorInexistente_paginaHtml404() throws Exception {
        // Cabecera Accept como la de un navegador: HTML primero y */* como reserva
        HttpResponse<String> r = get("/admin/proveedores/99", "text/html,*/*;q=0.8");
        assertThat(r.statusCode()).isEqualTo(404);
        assertThat(r.headers().firstValue("Content-Type")).hasValueSatisfying(
            v -> assertThat(v).startsWith("text/html"));
        assertThat(r.body()).contains("<h1>No encontrado</h1>")
            .contains("<code>/admin/proveedores/99</code>")
            .contains("<nav>");
    }

    @Test
    void api_proveedorInexistente_sigueRespondiendoJson() throws Exception {
        HttpResponse<String> r = get("/api/proveedores/99", "application/json");
        assertThat(r.statusCode()).isEqualTo(404);
        assertThat(r.body()).isEqualTo("{\"error\":\"No existe un proveedor con id 99\"}");
    }
}
