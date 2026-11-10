# SGPC — estado al final de la Parte II

Proyecto integrador **SGPC (Sistema de Gestión de Proveedores y Contratos)** del libro
*Bases de la Programación Web: Desarrollo Back-End*, tal como queda al terminar la
**Parte II (capítulos 6 a 9)**. El código es exactamente el de los listados del libro;
cada archivo indica en su primera línea su ruta y, cuando procede, la práctica o el
ejercicio que lo introduce.

| Capítulo / actividad | Archivos |
|---|---|
| Cap. 7 — capa web y de servicio | `controller/ProveedorController.java`, `controller/ManejadorExcepciones.java`, `service/ProveedorService.java`, `dto/ProveedorDTO.java`, `dto/ProveedorRequest.java`, `exception/*.java` |
| Cap. 7 — entidad y repositorio **provisionales** (los del CRUD de Spring Boot de la Parte I con el paquete `com.comercializadora.sgpc`) | `model/Proveedor.java`, `repository/ProveedorRepository.java` |
| Práctica 6 — `PUT` y cabecera `Location` absoluta | `ProveedorService.actualizar`, `ProveedorController.actualizar` y `crear` |
| Cap. 8 — filtros, interceptor y aspecto | `config/RegistroAccesosFilter.java`, `config/RedInternaFilter.java`, `config/FiltrosConfig.java`, `config/TiempoInterceptor.java`, `config/WebConfig.java`, `audit/Auditable.java`, `audit/AuditoriaAspect.java`, dependencia `spring-boot-starter-aspectj` del `pom.xml` |
| Práctica 7 — prueba del filtro de red interna | `src/test/java/com/comercializadora/sgpc/config/RedInternaFilterTest.java` |
| Cap. 9 — panel Thymeleaf | `controller/ProveedorViewController.java`, `templates/proveedores.html`, `templates/fragmentos/menu.html` |
| Práctica 8 — ficha del proveedor | `ProveedorViewController.detalle`, `templates/proveedor-detalle.html` |
| Ejercicio E2.1 — `DELETE` (baja física provisional) | `ProveedorService.darDeBaja`, `ProveedorController.darDeBaja` |
| Ejercicio E2.2 — `GET /api/proveedores/resumen` | `dto/ResumenProveedores.java`, `ProveedorService.resumen`, `ProveedorController.resumen` |
| Ejercicio E2.3 — aspecto de cronometraje | `audit/CronometroServicioAspect.java` |
| Ejercicio E2.4 — aviso, menú y pie | `templates/fragmentos/pie.html`, `proveedor-detalle.html`, `proveedores.html` |

Las rutas de código son relativas a `src/main/java/com/comercializadora/sgpc/` y las de
plantillas, a `src/main/resources/`. La variante de la Práctica 7 con `shouldNotFilter`
no está aplicada: el repositorio conserva la primera técnica (`FilterRegistrationBean`).

## Requisitos

- JDK 25 (LTS). Spring Boot 4.1.2 admite Java 17 o superior, pero el proyecto declara `java.version` 25.
- No hace falta instalar Maven: se incluye el *Maven Wrapper* (`mvnw`, `mvnw.cmd`), que descarga Maven 3.10.0.
- `curl` (en Windows PowerShell, `curl.exe`).

Generado con Spring Initializr (Spring Boot 4.1.2, Maven, Java 25, *Group* `com.comercializadora`,
*Artifact* `sgpc`, paquete `com.comercializadora.sgpc`) con las dependencias *Spring Web*,
*Spring Data JPA*, *Validation*, *H2 Database*, *Lombok* y *Thymeleaf*; Initializr añade por su cuenta
`spring-boot-h2console` y los iniciadores de prueba. En el Capítulo 8 se añadió a mano
`spring-boot-starter-aspectj`.

## Compilar, probar y ejecutar

Desde esta carpeta (`parte2/sgpc`):

Linux / macOS:

```bash
./mvnw -B verify            # compila y ejecuta las 4 pruebas
./mvnw spring-boot:run      # arranca en http://localhost:8080 (Ctrl+C para detener)
```

Windows (PowerShell):

```powershell
.\mvnw.cmd -B verify
.\mvnw.cmd spring-boot:run
```

Los datos se guardan en una base H2 en memoria y se pierden al detener la aplicación.

## Probar con curl

En otra terminal, con la aplicación en marcha (Linux / macOS):

```bash
curl -i -X POST localhost:8080/api/proveedores -H "Content-Type: application/json" \
     -d '{"ruc":"0990000001001","razonSocial":"Aceros Litoral S.A.","limiteCredito":15000}'
curl -i -X POST localhost:8080/api/proveedores -H "Content-Type: application/json" \
     -d '{"ruc":"1790000002001","razonSocial":"Insumos Andinos","limiteCredito":8000}'
curl -i localhost:8080/api/proveedores/1                 # 200
curl -i localhost:8080/api/proveedores/99                # 404
curl -i -X PUT localhost:8080/api/proveedores/2 -H "Content-Type: application/json" \
     -d '{"ruc":"0990000001001","razonSocial":"Insumos Andinos","limiteCredito":8000}'  # 409
curl -s localhost:8080/api/proveedores/resumen           # E2.2
curl -s localhost:8080/admin/proveedores/1               # Práctica 8 (HTML)
curl -i -X DELETE localhost:8080/api/proveedores/1       # E2.1: 204, y 404 si se repite
```

En Windows PowerShell se escribe `curl.exe`, las líneas se continúan con el acento grave (`` ` ``) y el
cuerpo JSON se guarda en un archivo y se envía con `-d "@proveedor.json"`, por ejemplo:

```powershell
Set-Content -Path proveedor.json -Encoding ascii `
  -Value '{"ruc":"0990000001001","razonSocial":"Aceros Litoral S.A.","limiteCredito":15000}'
curl.exe -i -X POST http://localhost:8080/api/proveedores `
  -H "Content-Type: application/json" -d "@proveedor.json"
```

Salida esperada del primer `POST` (las cabeceras `Date` y `Transfer-Encoding` se omiten):

```
HTTP/1.1 201
Location: http://localhost:8080/api/proveedores/1
Content-Type: application/json

{"id":1,"ruc":"0990000001001","razonSocial":"Aceros Litoral S.A.","limiteCredito":15000}
```

Cada petición deja en la consola una línea del filtro `RegistroAccesosFilter`; las de `/api/**`, otra del
`TiempoInterceptor`; las escrituras, una línea `[AUDITORIA]`; y cada método del servicio, una línea
`[TIEMPO]` del aspecto del Ejercicio E2.3.
