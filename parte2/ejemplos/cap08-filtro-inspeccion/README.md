# Capítulo 8 — Lo que un filtro ve de la petición

Proyecto de ejemplo del libro *Bases de la Programación Web: Desarrollo Back-End* (Capítulo 8,
Sección «Qué es un filtro y en qué nivel actúa»). El libro muestra solo el método `doFilterInternal`
de `InspeccionFilter`; aquí está el proyecto completo:

| Archivo (bajo `src/main/java/com/comercializadora/ejemplos/filtro/`) | Papel |
|---|---|
| `InspeccionFilter.java` | Filtro que lee método, ruta, cadena de consulta, una cabecera y la IP; fija `X-Content-Type-Options` antes de `chain.doFilter` y registra si la respuesta ya está confirmada a la vuelta |
| `EcoController.java` | `GET /api/eco/{id}` (respuesta pequeña) y `GET /api/eco/grande` (cuerpo de unos 20 KB) |
| `FiltroInspeccionApplication.java` | Clase principal |

La prueba `src/test/.../InspeccionFilterTest.java` arranca Tomcat en un puerto aleatorio y comprueba que la
cabecera fijada antes de ceder el control llega al cliente, y que `isCommitted()` vale `false` a la vuelta
de la respuesta pequeña y `true` a la vuelta de la grande.

## Requisitos

JDK 25 (LTS). Maven no hace falta: se incluye el *Maven Wrapper* (`mvnw`, `mvnw.cmd`).

## Compilar, probar y ejecutar

Linux / macOS, desde esta carpeta:

```bash
./mvnw -B verify            # 2 pruebas
./mvnw spring-boot:run      # http://localhost:8080
```

Windows (PowerShell):

```powershell
.\mvnw.cmd -B verify
.\mvnw.cmd spring-boot:run
```

## Probar con curl (segunda terminal)

```bash
curl -i "localhost:8080/api/eco/7?detalle=si"     # X-Content-Type-Options: nosniff y {"id":7}
curl -s -o /dev/null localhost:8080/api/eco/grande
```

En la consola de la aplicación aparecen, para cada petición, la línea con método, ruta, consulta, IP y
agente de usuario, y la línea `confirmada: false` (respuesta pequeña) o `confirmada: true` (respuesta grande).
En PowerShell se escribe `curl.exe`.
