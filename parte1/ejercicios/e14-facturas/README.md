# Parte I — Solución del ejercicio E1.4: rutas anidadas de facturas (Spring Boot 4.1.2)

Copia del proyecto de enrutamiento del Capítulo 5 (`parte1/cap05-enrutamiento/springboot`: Maven, Java 25, Spring Boot 4.1.2, Group `com.empresa`, Artifact `rutas-api`, Name `rutas`, Package name `com.empresa.rutas`, dependencias Spring Web y Validation) a la que solo se añade `src/main/java/com/empresa/rutas/controller/FacturaController.java`, el listado de la solución de E1.4. Respuestas simuladas, sin base de datos.

## Requisitos

- JDK 25 (el `pom.xml` declara `<java.version>25</java.version>`). Maven no hace falta: se incluye el Maven Wrapper.

## Ejecutar

Linux / macOS: `./mvnw spring-boot:run` · Windows (PowerShell): `.\mvnw.cmd spring-boot:run` (puerto 8080).

## Probar (segunda terminal)

```bash
F=http://localhost:8080/api/v1/proveedores; J="Content-Type: application/json"
curl -s $F/7/facturas                                   # 200 {"proveedor":7,"estado":"pendiente","pagina":1}
curl -s "$F/7/facturas?estado=pagada&pagina=2"          # 200 {"proveedor":7,"estado":"pagada","pagina":2}
curl -s $F/7/facturas/001-001-000123                    # 200 {"proveedor":7,"numero":"001-001-000123"}
curl -s -i -X POST $F/7/facturas -H "$J" \
  -d '{"numero":"001-001-000123","importe":250.75,"fecha_emision":"2026-11-02"}'
                                                        # 201 + Location: /api/v1/proveedores/7/facturas/001-001-000123
curl -s -o /dev/null -w "%{http_code}\n" $F/abc/facturas            # 400 (proveedorId no es un Long)
curl -s -o /dev/null -w "%{http_code}\n" -X POST $F/7/facturas -H "$J" \
  -d '{"numero":"001-001-000124","importe":-5,"fecha_emision":"2026-11-02"}'   # 400 (importe negativo)
curl -s -o /dev/null -w "%{http_code}\n" -X POST $F/7/facturas -H "$J" \
  -d '{"numero":"001-001-000125","importe":10,"fecha_emision":"2030-01-01"}'   # 400 (fecha futura)
curl -s -o /dev/null -w "%{http_code}\n" $F/7/facturas/             # 404 (barra final)
```

`Map.of` no garantiza el orden de las claves, de modo que el JSON puede mostrarlas en otro orden. En PowerShell: `curl.exe` y el cuerpo en un archivo (`-d "@factura.json"`).

Probado el 10-11-2026 con OpenJDK 25.0.4 y `./mvnw -B -q verify`.
