// Archivo: src/test/java/com/comercializadora/ejemplos/filtro/InspeccionFilterTest.java
package com.comercializadora.ejemplos.filtro;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.assertj.core.api.Assertions.assertThat;

// Servidor Tomcat real en un puerto aleatorio: la confirmación (commit) de la
// respuesta es la del contenedor, no la de un objeto simulado.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
class InspeccionFilterTest {

    @LocalServerPort
    private int puerto;

    private HttpResponse<String> get(String ruta) throws Exception {
        HttpRequest peticion = HttpRequest.newBuilder(
                URI.create("http://localhost:" + puerto + ruta))
            .header("User-Agent", "prueba/1.0").build();
        return HttpClient.newHttpClient().send(peticion, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void registraLaPeticion_yLaCabeceraFijadaAntesLlega(CapturedOutput salida) throws Exception {
        HttpResponse<String> respuesta = get("/api/eco/7?detalle=si");
        assertThat(respuesta.statusCode()).isEqualTo(200);
        assertThat(respuesta.body()).isEqualTo("{\"id\":7}");
        assertThat(respuesta.headers().firstValue("X-Content-Type-Options")).hasValue("nosniff");
        assertThat(salida.getOut())
            .contains("GET /api/eco/7 ?detalle=si desde 127.0.0.1 (prueba/1.0)")
            .contains("confirmada: false");      // cuerpo pequeño: aún en el búfer
    }

    @Test
    void conUnCuerpoGrande_laRespuestaYaEstaConfirmadaALaVuelta(CapturedOutput salida) throws Exception {
        HttpResponse<String> respuesta = get("/api/eco/grande");
        assertThat(respuesta.statusCode()).isEqualTo(200);
        assertThat(respuesta.headers().firstValue("X-Content-Type-Options")).hasValue("nosniff");
        assertThat(salida.getOut()).contains("confirmada: true");
    }
}
