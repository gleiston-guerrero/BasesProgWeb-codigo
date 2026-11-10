# Capítulo 4 — Solución de la Práctica 4: CRUD de contratos con Spring Boot 4.1.2

Proyecto generado en https://start.spring.io con: Maven, Java, Spring Boot 4.1.2, Group `com.empresa`, Artifact `contratos-api`, Name `contratos`, Package name `com.empresa.contratos`, Java 25 y las dependencias Spring Web, Spring Data JPA, H2 Database y Validation. Se añadieron `model/Contrato.java`, `repository/ContratoRepository.java`, `service/ContratoService.java`, `controller/ContratoController.java` y `application.properties`.

## Ejecutar

Linux / macOS: `./mvnw verify` y `./mvnw spring-boot:run` · Windows (PowerShell): `.\mvnw.cmd verify` y `.\mvnw.cmd spring-boot:run`.

## Probar (Linux / macOS)

```bash
api=http://localhost:8080/api/contratos
J='{"numero":"CT-2026-001","descripcion":"Suministro","valor":12500.50,"proveedorId":1}'
curl -s -i -X POST $api -H "Content-Type: application/json" -d "$J"                       # 201 + Location
curl -s -o /dev/null -w "%{http_code}\n" -X POST $api -H "Content-Type: application/json" -d "$J"   # 409
curl -s -o /dev/null -w "%{http_code}\n" -X POST $api -H "Content-Type: application/json" -d '{"valor":10}'  # 400
curl -s -o /dev/null -w "%{http_code}\n" $api              # 200
curl -s -o /dev/null -w "%{http_code}\n" $api/99           # 404 (también PUT y DELETE de /99)
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $api/1  # 204
```

En PowerShell, `curl.exe` y el cuerpo en un archivo (`-d "@contrato.json"`).

Consola H2: http://localhost:8080/h2-console, URL JDBC `jdbc:h2:mem:contratosdb`, usuario `sa`; `SELECT * FROM contratos;`.
