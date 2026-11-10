# Parte III — Soluciones de los ejercicios E3.1 a E3.4

Cada carpeta contiene, **como archivos completos y con las mismas rutas relativas** que en el
proyecto, los archivos que la solución añade o modifica respecto a `../sgpc` (estado al final de la
Parte III). Los listados de la sección «Respuestas — Parte III» del libro muestran solo lo que cambia.

| Ejercicio | Archivos |
|---|---|
| E3.1 — consulta derivada y `@Query` en `ProveedorRepository` | `repository/ProveedorRepository.java` |
| E3.2 — `GET /api/proveedores/{proveedorId}/contratos/vigentes` paginado | `repository/ContratoRepository.java`, `service/ContratoService.java`, `controller/ContratoController.java` |
| E3.3 — `PUT` de contratos solo para ADMIN | `config/SeguridadConfig.java`, `controller/ContratoController.java` |
| E3.4 — restricción de clase `@FechasCoherentes` | `dto/FechasCoherentes.java`, `dto/ConRangoFechas.java`, `dto/FechasCoherentesValidator.java`, `dto/ContratoRequest.java`, `controller/ManejadorExcepciones.java` |

(rutas relativas a `src/main/java/com/comercializadora/sgpc/`)

Las soluciones son independientes entre sí: se aplica una cada vez sobre una copia limpia del proyecto.

## Aplicar y probar una solución

Linux / macOS, desde esta carpeta (`parte3/ejercicios`):

```bash
cp -r ../sgpc /tmp/sgpc-e32 && cp -r E3.2/. /tmp/sgpc-e32/
cd /tmp/sgpc-e32
export SGPC_JWT_SECRET="$(openssl rand -base64 32)"
./mvnw -B verify && ./mvnw spring-boot:run
```

Windows (PowerShell):

```powershell
Copy-Item ..\sgpc $env:TEMP\sgpc-e32 -Recurse
Copy-Item E3.2\* $env:TEMP\sgpc-e32 -Recurse -Force
Set-Location $env:TEMP\sgpc-e32
$b = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b)
$env:SGPC_JWT_SECRET = [Convert]::ToBase64String($b)
.\mvnw.cmd -B verify; .\mvnw.cmd spring-boot:run
```

## Resultados comprobados (10-11-2026, JDK 25, Spring Boot 4.1.2)

Con un token de `admin` en `$A` (ver `../sgpc/README.md`) y el proveedor 1 creado:

- **E3.2**: con un contrato vigente, otro que empieza el año próximo y otro vencido,
  `GET .../1/contratos/vigentes` → 200 con `"totalElementos":1`; `.../9/contratos/vigentes` → 404;
  `?sort=clave` → 400 `{"error":"No se puede ordenar por clave"}`.
- **E3.3**: `PUT .../1/contratos/1` con el token de `gestor` → 403; sin token → 401; con el de `admin` → 200.
- **E3.4**: el cuerpo `{"numero":"","monto":-500,"fechaInicio":"2026-12-31","fechaVencimiento":"2026-01-01"}`
  → 400 `{"numero":[...],"monto":[...],"global":["La fecha de vencimiento debe ser posterior a la de inicio"]}`.
- **E3.1** no añade *endpoints*: se comprueba compilando y arrancando (`./mvnw -B verify`).
