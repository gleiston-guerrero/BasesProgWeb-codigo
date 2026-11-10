# Capítulo 5 — Solución de la Práctica 5 en Spring Boot 4.1.2: rutas de contratos

Proyecto generado en https://start.spring.io (Maven, Java 25, Spring Boot 4.1.2, Group `com.constructora`, Artifact `contratos-rutas`, Name `contratos`, Package name `com.constructora.contratos`, dependencias Spring Web y Validation). Se añadió `controller/ContratoController.java` con prefijo común `/api/v1/contratos` y respuestas simuladas.

## Ejecutar

Linux / macOS: `./mvnw spring-boot:run` · Windows (PowerShell): `.\mvnw.cmd spring-boot:run`

## Tabla de pruebas (ruta, método, esperado; todos coinciden con lo obtenido)

| Ruta | Método | Código |
|---|---|---|
| /api/v1/contratos | GET | 200 |
| /api/v1/contratos?estado=activo&anio=2025 | GET | 200 (`estado` y `anio` reflejados) |
| /api/v1/contratos/CT-2026-001 | GET | 200 |
| /api/v1/contratos (cuerpo completo) | POST | 201 + Location |
| /api/v1/contratos (sin `monto`) | POST | 400 |
| /api/v1/contratos/CT-2026-001 (`{"monto":7000}`) | PATCH | 200 |
| /api/v1/contratos/CT-2026-001 | DELETE | 204 |
| /contratos (sin prefijo) | GET | 404 |

```bash
B=http://localhost:8080/api/v1/contratos; J="Content-Type: application/json"
curl -s "$B?estado=activo"
curl -s -i -X POST $B -H "$J" -d '{"numero":"CT-2026-002","monto":5000,"fecha_fin":"2027-06-30"}'
curl -s -o /dev/null -w "%{http_code}\n" -X POST $B -H "$J" -d '{"numero":"CT-2026-002","fecha_fin":"2027-06-30"}'
curl -s -X PATCH $B/CT-2026-001 -H "$J" -d '{"monto":7000}'
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $B/CT-2026-001
curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/contratos
```

En PowerShell: `curl.exe` y el cuerpo en un archivo (`-d "@contrato.json"`).
