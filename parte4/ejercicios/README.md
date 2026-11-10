# Parte IV — Soluciones de los ejercicios E4.1 a E4.4

Cada carpeta contiene, **como archivos completos y con las mismas rutas relativas** que en el
proyecto, los archivos que la solución añade o modifica respecto a `../sgpc` (estado final del
libro). Los listados de la sección «Respuestas — Parte IV» del libro muestran solo lo que cambia.

| Ejercicio | Archivos |
|---|---|
| E4.1 — caché de contratos vigentes (`GET .../contratos/vigentes/todos`) | `dto/ContratoVigente.java`, `repository/ContratoRepository.java`, `service/ContratoService.java`, `controller/ContratoController.java` |
| E4.2 — API de categorías y asociación con contratos | `exception/CategoriaNoEncontradaException.java`, `exception/CategoriaDuplicadaException.java`, `repository/CategoriaRepository.java`, `dto/CategoriaRequest.java`, `dto/CategoriaDTO.java`, `service/CategoriaService.java`, `controller/CategoriaController.java`, `service/ContratoService.java`, `controller/ContratoController.java`, `controller/ManejadorExcepciones.java` |
| E4.3 — `@EntityGraph` y cartera paginada en dos consultas | `dto/PaginaCartera.java`, `repository/ProveedorRepository.java`, `service/InformeService.java`, `controller/InformeController.java` |
| E4.4 — dos instancias con Redis e indicador de administradores | `pom.xml`, `compose.yaml`, `src/main/resources/application.properties`, `src/main/resources/application-redis.properties`, `dto/ProveedorDTO.java`, `repository/UsuarioRepository.java`, `config/AdministradorHealthIndicator.java` |

(rutas Java relativas a `src/main/java/com/comercializadora/sgpc/`)

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
`./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=sgpc:1.1` y `docker compose up -d`;
después se da de alta el administrador como en la Práctica 15.

## Resultados comprobados (10-11-2026, JDK 25, Spring Boot 4.1.2)

Las cuatro soluciones: `./mvnw -B verify` → 16 pruebas, 0 fallos. Con tokens de `gestor`/`admin`:

- **E4.1**: primer `GET .../1/contratos/vigentes/todos` → 2 sentencias (existencia del proveedor y
  contratos); el segundo → 0; tras crear un contrato vigente aparece en la lista; `.../999/...` → 404.
- **E4.2**: `POST /api/categorias` → 201 con `Location`; repetido → 409; nombre vacío → 400;
  `PUT .../1/contratos/1/categorias/1` → 204 (idempotente: 204 otra vez); categoría 9 → 404;
  `GET .../1/contratos/1/categorias` → `[{"id":1,"nombre":"suministros"}]`.
- **E4.3**: con 6 proveedores, `GET /api/informes/cartera?page=0&size=2` → 3 sentencias (ids con
  `offset/fetch first`, recuento y `JOIN FETCH ... in (?, ?)`), `totalElementos` 6 y `totalPaginas` 3;
  `GET /api/informes/cartera/grafo` → 1 sentencia; ninguna advertencia `HHH90003004`.
- **E4.4**: dos instancias (`app1`, `app2`) con `prod,redis`, Redis 8.10 y `postgres:18`: un `GET` en
  la segunda instancia, un `PUT` en la primera y otro `GET` en la segunda devuelve ya el dato nuevo;
  Redis guarda la clave `proveedores::1`; la salud muestra `redis` y `administrador` (`administradores: 1`) en `UP`.
