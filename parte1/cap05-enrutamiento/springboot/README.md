# Capítulo 5 — Enrutamiento en Spring Boot 4.1.2

Reúne los listados de Spring Boot del Capítulo 5. Proyecto generado en https://start.spring.io (Maven, Java 25, Spring Boot 4.1.2, Group `com.empresa`, Artifact `rutas-api`, Name `rutas`, Package name `com.empresa.rutas`, dependencias Spring Web y Validation).

- `controller/ProveedorController.java`: rutas básicas (`/api/proveedores`).
- `controller/ContratoController.java`: clase `@RestController @RequestMapping("/api")` con los métodos de los fragmentos «Parámetros de ruta» y «Parámetros de consulta y cuerpo de la petición».
- `controller/EmpleadoController.java`: grupo de rutas `/api/v1/rrhh/empleados`.

## Ejecutar

Linux / macOS: `./mvnw spring-boot:run` · Windows (PowerShell): `.\mvnw.cmd spring-boot:run` (puerto 8080).

## Probar

```bash
B=http://localhost:8080/api; J="Content-Type: application/json"
curl -s $B/proveedores                                   # 200 {"total":142,"activos":138}
curl -s -X POST $B/proveedores -H "$J" -d '{}'           # 201 {"creado":"true"}
curl -s $B/contratos/1042                                # 200 {"contrato_id":1042}
curl -s -o /dev/null -w "%{http_code}\n" $B/contratos/abc   # 400 (no es un Long)
curl -s $B/reportes                                      # 200 {"anio":2026}
curl -s "$B/contratos?estado=activo"                     # 200 estado, anio y pagina
curl -s -X POST $B/contratos -H "$J" \
  -d '{"numero":"CT-1","monto":100.50,"fecha_fin":"2027-12-31"}'      # 201
curl -s -o /dev/null -w "%{http_code}\n" -X POST $B/contratos -H "$J" \
  -d '{"numero":"CT-1","fecha_fin":"2027-12-31"}'                      # 400 (falta monto)
curl -s $B/v1/rrhh/empleados                             # 200 []
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $B/v1/rrhh/empleados/7   # 204
```

En PowerShell: `curl.exe` y el cuerpo en un archivo (`-d "@contrato.json"`).
