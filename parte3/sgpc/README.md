# SGPC — estado al final de la Parte III

Proyecto integrador **SGPC (Sistema de Gestión de Proveedores y Contratos)** del libro
*Bases de la Programación Web: Desarrollo Back-End*, tal como queda al terminar la
**Parte III (capítulos 10 a 13)**: persistencia con Spring Data JPA, API REST de contratos con
paginación y OpenAPI, seguridad con Spring Security y JWT, y validación con Bean Validation.
Parte del proyecto de la Parte II (`parte2/sgpc`); el código es exactamente el de los listados
del libro y cada archivo indica en su primera línea su ruta y, cuando procede, el capítulo, la
práctica o el ejercicio que lo introduce o lo modifica.

| Capítulo / actividad | Archivos (relativos a `src/main/java/com/comercializadora/sgpc/`) |
|---|---|
| Cap. 10 — entidades definitivas (sustituyen a la provisional de la Parte II) | `model/Proveedor.java`, `model/EstadoProveedor.java`, `model/Contrato.java` (con Lombok) |
| Cap. 10 — repositorio de proveedores (sustituye al de la Parte II) y Práctica 9 | `repository/ProveedorRepository.java` |
| Cap. 10 — esquema derivado y Práctica 9 (SQL en consola, H2 `jdbc:h2:mem:sgpcdb`, consola H2) | `src/main/resources/application.properties` |
| Cap. 11 — API de contratos como subrecurso, paginación, reglas 400/404/409 | `controller/ContratoController.java`, `dto/ContratoRequest.java`, `dto/ContratoDTO.java`, `dto/PaginaContratos.java`, `repository/ContratoRepository.java`, `service/ContratoService.java`, `exception/Contrato*.java`, `exception/NumeroContratoDuplicadoException.java`, `exception/LimiteCreditoExcedidoException.java`, `exception/ProveedorNoActivoException.java`, `exception/OrdenNoValidoException.java`, métodos nuevos de `controller/ManejadorExcepciones.java` |
| Cap. 11 — OpenAPI | dependencia `springdoc-openapi-starter-webmvc-ui` 3.2.0 del `pom.xml` |
| Cap. 12 — JWT con el servidor de recursos de Spring Security | `model/Usuario.java`, `model/Rol.java`, `repository/UsuarioRepository.java`, `security/UsuarioDetailsService.java`, `security/TokenService.java`, `config/JwtConfig.java`, `config/SeguridadConfig.java`, `controller/AuthController.java`, propiedades `sgpc.jwt.*` |
| Cap. 12 — baja lógica solo para ADMIN, estado visible en la API y auditoría con el usuario | `ProveedorController.darDeBaja` (`@PreAuthorize`), `ProveedorService.darDeBaja`, `dto/ProveedorDTO.java` (campo `estado`, sustituye al de la Parte II) y `ProveedorService.aDTO`, `audit/AuditoriaAspect.usuarioActual()` |
| Práctica 11 — usuarios de prueba (fuera del perfil `prod`) | `config/UsuariosIniciales.java` |
| Cap. 12 — prueba de la Práctica 7 con la seguridad activa | `@WithMockUser` en `src/test/.../config/RedInternaFilterTest.java` y dependencia `spring-boot-starter-security-test` |
| Cap. 13 — validación | `dto/ProveedorRequest.java` (sustituye al de la Parte II), `ContratoRequest.isRangoFechasValido()`, `ManejadorExcepciones.manejarValidacion` y `violacionIntegridad` |

La clase temporal `DatosDemo` de la Práctica 9 no se incluye, porque el libro pide borrarla al
terminar. Las soluciones de los ejercicios E3.1 a E3.4 están en `../ejercicios`.

## Requisitos

- JDK 25 (LTS).
- No hace falta instalar Maven: el *Maven Wrapper* (`mvnw`, `mvnw.cmd`) descarga Maven 3.10.0.
- `curl` (en Windows PowerShell, `curl.exe`) y, en Linux/macOS, `openssl` para generar la clave.
- Versiones gestionadas por Spring Boot 4.1.2: Spring Framework 7.0.10, Spring Security 7.1.2,
  Spring Data JPA 4.1.2, Hibernate ORM 7.4.13, Hibernate Validator 9.1.5, Jackson 3.1.7, H2 2.4.240,
  Lombok 1.18.48. springdoc-openapi 3.2.0 se declara con su versión.

## La clave del JWT

Desde el Capítulo 12, la aplicación **y las pruebas** necesitan la variable de entorno
`SGPC_JWT_SECRET` con al menos 32 bytes; sin ella, el arranque falla con
`Could not resolve placeholder 'SGPC_JWT_SECRET'`. Se define en la terminal desde la que se
ejecutan las órdenes (solo vive en esa terminal; al cambiarla, los tokens anteriores dejan de valer).

## Compilar, probar y ejecutar

Desde esta carpeta (`parte3/sgpc`).

Linux / macOS:

```bash
export SGPC_JWT_SECRET="$(openssl rand -base64 32)"
./mvnw -B verify            # compila y ejecuta las 4 pruebas
./mvnw spring-boot:run      # arranca en http://localhost:8080 (Ctrl+C para detener)
```

Windows (PowerShell):

```powershell
$b = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b)
$env:SGPC_JWT_SECRET = [Convert]::ToBase64String($b)
.\mvnw.cmd -B verify
.\mvnw.cmd spring-boot:run
```

Al arrancar se crean los usuarios de desarrollo `admin` / `admin-clave-dev` (rol ADMIN) y
`gestor` / `gestor-clave-dev` (rol GESTOR). Los datos se guardan en H2 en memoria y se pierden al
detener la aplicación. La consola de H2 (`/h2-console`), que usan las prácticas 9 y 10, queda detrás
de la seguridad del Capítulo 12 y responde 401.

## Probar con curl (Linux / macOS)

En otra terminal, con la aplicación recién arrancada:

```bash
H='Content-Type: application/json'
login() { curl -s -X POST localhost:8080/auth/login -H "$H" \
               -d "{\"username\":\"$1\",\"password\":\"$2\"}" \
          | sed 's/.*"token":"\([^"]*\)".*/\1/'; }
GESTOR=$(login gestor gestor-clave-dev); ADMIN=$(login admin admin-clave-dev)
A="Authorization: Bearer $GESTOR"

curl -i localhost:8080/api/proveedores                   # 401 WWW-Authenticate: Bearer resource_metadata=...
curl -s -X POST localhost:8080/api/proveedores -H "$H" -H "$A" \
     -d '{"ruc":"0990000001001","razonSocial":"Distribuidora Andina S.A.","limiteCredito":50000.00}'   # 201

B=localhost:8080/api/proveedores/1/contratos
F='"fechaInicio":"2026-01-01","fechaVencimiento":"2027-12-31"'   # debe incluir el día de hoy
curl -i -X POST $B -H "$H" -H "$A" -d "{\"numero\":\"C-2026-001\",\"monto\":10000.00,$F}"  # 201 + Location
curl -s -X POST $B -H "$H" -H "$A" -d "{\"numero\":\"C-2026-002\",\"monto\":45000.00,$F}"  # 409 (40000.00 disponibles)
curl -s -X POST $B -H "$H" -H "$A" -d '{}'                                                # 400 cuatro claves
curl -s "$B?sort=monto,desc" -H "$A"                                                      # 200 página
curl -s "$B?sort=clave" -H "$A"                                                           # 400
curl -i -X DELETE localhost:8080/api/proveedores/1 -H "$A"                                # 403 (GESTOR)
curl -i -X DELETE localhost:8080/api/proveedores/1 -H "Authorization: Bearer $ADMIN"      # 204 (baja lógica)
curl -s localhost:8080/api/proveedores/1 -H "$A"                                         # 200 ...,"estado":"INACTIVO"}
curl -s -o /dev/null -w "%{http_code}\n" -L localhost:8080/swagger-ui.html                # 200
```

Estas respuestas se comprobaron el 10-11-2026 con el JDK 25 y Spring Boot 4.1.2. Los listados de
verificación completos de las prácticas 10, 11 y 12 están en el libro.

En Windows PowerShell se escribe `curl.exe`, las líneas se continúan con el acento grave (`` ` ``) y
el cuerpo JSON se guarda en un archivo y se envía con `-d "@archivo.json"`; el token puede obtenerse con
`$GESTOR = (Invoke-RestMethod -Method Post -Uri http://localhost:8080/auth/login -ContentType 'application/json' -Body '{"username":"gestor","password":"gestor-clave-dev"}').token`
y enviarse con `-H "Authorization: Bearer $GESTOR"`.
