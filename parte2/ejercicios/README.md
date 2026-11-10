# Parte II — Soluciones de los ejercicios E2.5 a E2.8

Los ejercicios E2.1 a E2.4 están aplicados directamente en `../sgpc`. E2.5 a E2.8 cambian el
comportamiento del sistema o su registro (el modo de mantenimiento rechaza escrituras; el panel responde
con una página HTML de error; se registran los errores del servicio; el panel admite búsquedas), por eso
se entregan aparte: cada carpeta contiene, **como archivos
completos y con las mismas rutas relativas** que en el proyecto, los archivos que la solución añade o
sustituye respecto a `../sgpc`, más su prueba automatizada. La sección «Respuestas — Parte II» del
libro muestra solo lo que cambia.

| Ejercicio | Archivos |
|---|---|
| E2.5 — modo de mantenimiento con un interceptor (`503` + `Retry-After`) | `config/MantenimientoInterceptor.java` (nuevo), `config/WebConfig.java` (sustituye), prueba `config/MantenimientoInterceptorTest.java` |
| E2.6 — página HTML de error 404 para el panel | `controller/ManejadorExcepciones.java` y `exception/ProveedorNoEncontradoException.java` (sustituyen), `templates/error/404.html` (nueva), prueba `controller/PaginaErrorTest.java` |
| E2.7 — registro de errores con `@AfterThrowing` | `audit/ErroresServicioAspect.java` (nuevo), prueba `audit/ErroresServicioAspectTest.java` |
| E2.8 — búsqueda por razón social en el panel | `service/ProveedorService.java` y `controller/ProveedorViewController.java` (sustituyen), `templates/proveedores.html` (sustituye), prueba `controller/BusquedaPanelTest.java` |

(rutas de código relativas a `src/main/java/com/comercializadora/sgpc/` y `src/test/java/com/comercializadora/sgpc/`;
plantillas, a `src/main/resources/`)

Las soluciones son independientes entre sí: se aplica una cada vez sobre una copia limpia del proyecto.

## Requisitos

JDK 25. Maven no hace falta: el proyecto `../sgpc` incluye el *Maven Wrapper*.

## Aplicar y probar una solución

Linux / macOS, desde esta carpeta (`parte2/ejercicios`):

```bash
cp -r ../sgpc /tmp/sgpc-e25 && cp -r E2.5/. /tmp/sgpc-e25/
cd /tmp/sgpc-e25
./mvnw -B verify                                   # 6 pruebas (7 con E2.8)
./mvnw spring-boot:run -Dspring-boot.run.arguments=--sgpc.mantenimiento=true
```

Windows (PowerShell):

```powershell
Copy-Item ..\sgpc $env:TEMP\sgpc-e25 -Recurse
Copy-Item E2.5\* $env:TEMP\sgpc-e25 -Recurse -Force
Set-Location $env:TEMP\sgpc-e25
.\mvnw.cmd -B verify
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--sgpc.mantenimiento=true"
```

Para E2.6, E2.7 y E2.8, lo mismo con su carpeta (y sin el argumento `--sgpc.mantenimiento`).

## Probar con curl (segunda terminal)

E2.5 (aplicación arrancada con `--sgpc.mantenimiento=true`):

```bash
curl -i -X POST localhost:8080/api/proveedores -H "Content-Type: application/json" \
     -d '{"ruc":"0990000001001","razonSocial":"Aceros Litoral S.A.","limiteCredito":15000}'
# HTTP/1.1 503, Retry-After: 3600
curl -s -o /dev/null -w "%{http_code}\n" localhost:8080/api/proveedores      # 200
```

E2.6:

```bash
curl -i -H "Accept: text/html,*/*;q=0.8" localhost:8080/admin/proveedores/99   # 404, página HTML
curl -i localhost:8080/api/proveedores/99    # 404, {"error":"No existe un proveedor con id 99"}
curl -i localhost:8080/admin/proveedores/99  # 404, JSON de error de Spring Boot (Accept: */*)
```

E2.7 (en la consola de la aplicación aparece una línea `WARN ... [ERROR] ProveedorService.buscarPorId(..) -> ProveedorNoEncontradoException: ...`):

```bash
curl -i localhost:8080/api/proveedores/99    # 404, como siempre
```

E2.8 (tras crear los dos proveedores del `README` de `../sgpc`):

```bash
curl -s "localhost:8080/admin/proveedores?q=ACERO" | grep -c -E "Aceros Litoral|Insumos Andinos"   # 1
curl -s --get localhost:8080/admin/proveedores --data-urlencode "q=<b>x</b>" | grep "Resultados"
# <p>Resultados para «<span>&lt;b&gt;x&lt;/b&gt;</span>»
```

En PowerShell se escribe `curl.exe` y el cuerpo JSON se envía desde un archivo con `-d "@proveedor.json"`,
como se explica en `../sgpc/README.md`.
