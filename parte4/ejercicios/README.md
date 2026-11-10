# Parte IV — Soluciones de los ejercicios E4.1 a E4.4 y del caso límite del Apéndice A

Cada carpeta contiene, **como archivos completos y con las mismas rutas relativas** que en el
proyecto, los archivos que la solución añade o modifica respecto a `../sgpc` (estado final del
libro). Los listados de la sección «Respuestas — Parte IV» del libro muestran solo lo que cambia.

| Ejercicio | Archivos |
|---|---|
| E4.1 — caché de contratos vigentes (`GET .../contratos/vigentes/todos`) | `dto/ContratoVigente.java`, `repository/ContratoRepository.java`, `service/ContratoService.java`, `controller/ContratoController.java` |
| E4.2 — API de categorías y asociación con contratos | `exception/CategoriaNoEncontradaException.java`, `exception/CategoriaDuplicadaException.java`, `repository/CategoriaRepository.java`, `dto/CategoriaRequest.java`, `dto/CategoriaDTO.java`, `service/CategoriaService.java`, `controller/CategoriaController.java`, `service/ContratoService.java`, `controller/ContratoController.java`, `controller/ManejadorExcepciones.java` |
| E4.3 — `@EntityGraph`, `JOIN FETCH` paginado por Hibernate 7.4 (`/cartera/directa`) y cartera paginada en dos consultas | `dto/PaginaCartera.java`, `repository/ProveedorRepository.java`, `service/InformeService.java`, `controller/InformeController.java` |
| E4.4 — dos instancias con Redis e indicador de administradores | `pom.xml`, `compose.yaml`, `src/main/resources/application.properties`, `src/main/resources/application-redis.properties`, `dto/ProveedorDTO.java`, `repository/UsuarioRepository.java`, `config/AdministradorHealthIndicator.java` |

| A.1 — caso límite del crédito (Apéndice A): un monto igual al disponible se acepta | `src/test/java/com/comercializadora/sgpc/service/ContratoServiceTest.java` (tercera prueba añadida) |

(rutas Java relativas a `src/main/java/com/comercializadora/sgpc/`, salvo la de A.1)

Las soluciones son independientes entre sí: se aplica una cada vez sobre una copia limpia del proyecto.

## Aplicar y probar una solución

Linux / macOS, desde esta carpeta (`parte4/ejercicios`):

```bash
cp -r ../sgpc /tmp/sgpc-e41 && cp -r E4.1/. /tmp/sgpc-e41/
cd /tmp/sgpc-e41
export SGPC_JWT_SECRET="$(openssl rand -base64 32)"
./mvnw -B verify && ./mvnw spring-boot:run
```

Windows (PowerShell):

```powershell
Copy-Item ..\sgpc $env:TEMP\sgpc-e41 -Recurse
Copy-Item E4.1\* $env:TEMP\sgpc-e41 -Recurse -Force
Set-Location $env:TEMP\sgpc-e41
$b = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b)
$env:SGPC_JWT_SECRET = [Convert]::ToBase64String($b)
.\mvnw.cmd -B verify; .\mvnw.cmd spring-boot:run
```

E4.4 se despliega con Docker: tras copiar la solución, `cp .env.example .env` (editar valores),
`./mvnw spring-boot:build-image` (su `pom.xml` fija el nombre `sgpc:1.1`, que usa su `compose.yaml`) y `docker compose up -d`;
después se da de alta el administrador como en la Práctica 15.

> **Avisos inofensivos de Maven.** Al construir o ejecutar, `./mvnw` puede mostrar `[WARNING] Could not transfer metadata ... from/to flyway-community-db-support (https://maven.pkg.github.com/...): HTTP Status: 401` y otro parecido para `red-gate.pkgs.visualstudio.com`. Proceden de los repositorios que declara el POM padre de Flyway (`org.flywaydb:flyway-parent`), no del `pom.xml` del proyecto, y no impiden la construcción: todas las dependencias se descargan de Maven Central y el resultado es `BUILD SUCCESS`.

## Resultados comprobados (10-11-2026, JDK 25, Spring Boot 4.1.2)

Las cuatro soluciones: `./mvnw -B verify` → 16 pruebas, 0 fallos; A.1 → 17 pruebas (las 16 del proyecto y la del caso límite), 0 fallos. Con tokens de `gestor`/`admin`:

- **E4.1**: primer `GET .../1/contratos/vigentes/todos` → 2 sentencias (existencia del proveedor y
  contratos); el segundo → 0; tras crear un contrato vigente aparece en la lista; `.../999/...` → 404.
- **E4.2**: `POST /api/categorias` → 201 con `Location`; repetido → 409; nombre vacío → 400;
  `PUT .../1/contratos/1/categorias/1` → 204 (idempotente: 204 otra vez); categoría 9 → 404;
  `GET .../1/contratos/1/categorias` → `[{"id":1,"nombre":"suministros"}]`.
- **E4.3**: con 6 proveedores, `GET /api/informes/cartera/directa?page=0&size=2` → 2 sentencias (una con
  subconsulta `offset ? rows fetch first ? rows only` y `left join contratos`, y el recuento);
  `GET /api/informes/cartera?page=0&size=2` → mismo JSON con 3 sentencias (ids con
  `offset/fetch first`, recuento y `JOIN FETCH ... in (?, ?)`), `totalElementos` 6 y `totalPaginas` 3;
  `GET /api/informes/cartera/grafo` → 1 sentencia; ninguna advertencia `HHH90003004`.
- **E4.4**: dos instancias (`app1`, `app2`) con `prod,redis`, Redis 8.10 y `postgres:18`: un `GET` en
  la segunda instancia, un `PUT` en la primera y otro `GET` en la segunda devuelve ya el dato nuevo;
  Redis guarda la clave `proveedores::1`; la salud muestra `redis` y `administrador` (`administradores: 1`) en `UP`.
