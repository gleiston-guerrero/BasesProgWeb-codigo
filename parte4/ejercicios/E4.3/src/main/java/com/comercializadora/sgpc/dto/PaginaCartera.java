// Archivo: src/main/java/com/comercializadora/sgpc/dto/PaginaCartera.java
package com.comercializadora.sgpc.dto;

import java.util.List;

// Ejercicio E4.3: página del informe de cartera, análoga a PaginaContratos
public record PaginaCartera(List<LineaCartera> contenido, int pagina,
        int tamanio, long totalElementos, int totalPaginas) {}
