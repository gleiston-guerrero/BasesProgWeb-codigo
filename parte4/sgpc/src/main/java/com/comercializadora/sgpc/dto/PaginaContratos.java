// Archivo: src/main/java/com/comercializadora/sgpc/dto/PaginaContratos.java
package com.comercializadora.sgpc.dto;
import java.util.List;

// Estructura estándar de respuesta paginada del SGPC.
public record PaginaContratos(
    List<ContratoDTO> contenido,   // los elementos de esta página
    int pagina,                    // número de página actual (base 0)
    int tamanio,                   // elementos por página
    long totalElementos,           // total de contratos del proveedor
    int totalPaginas               // total de páginas disponibles
) {}
