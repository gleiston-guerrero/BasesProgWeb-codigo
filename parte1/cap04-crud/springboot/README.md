# Capítulo 4 — CRUD de proveedores con Spring Boot 4.1.2

Proyecto del apartado «Spring Boot — CRUD de proveedores» (Capítulo 4, Listados del `pom.xml` al `application.properties`). El código coincide con el del libro.

## Requisitos

- JDK 25 (Spring Boot 4.1.2 admite de Java 17 a Java 27).
- No hace falta instalar Maven: el proyecto incluye el Maven Wrapper (`mvnw`, `mvnw.cmd`), que descarga Maven 3.10.0.
- curl (incluido en Windows 10/11 como `curl.exe`).

## Ejecutar

Linux / macOS (en esta carpeta):

```bash
./mvnw verify            # compila y ejecuta la prueba ProveedoresApplicationTests
./mvnw spring-boot:run   # arranca en http://localhost:8080
```

Windows (PowerShell, en esta carpeta):

```powershell
.\mvnw.cmd verify
.\mvnw.cmd spring-boot:run
```

Si el puerto 8080 está ocupado: `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`.

## Probar (otra terminal)

Linux / macOS:

```bash
api=http://localhost:8080/api/proveedores
curl -s -i -X POST $api -H "Content-Type: application/json" \
  -d '{"ruc":"1790012345001","razonSocial":"Importadora XL","limiteCredito":50000}'   # 201 + Location
curl -s $api                       # 200: [{"ruc":"1790012345001",...,"limiteCredito":50000.00,"id":1}]
curl -s $api/1                     # 200
curl -s -o /dev/null -w "%{http_code}\n" -X POST $api -H "Content-Type: application/json" \
  -d '{"ruc":"1790012345001","razonSocial":"Otra","limiteCredito":1000}'              # 409
curl -s -o /dev/null -w "%{http_code}\n" -X POST $api -H "Content-Type: application/json" \
  -d '{"ruc":"179001234500","razonSocial":"Otra","limiteCredito":-5}'                 # 400
curl -s -X PUT $api/1 -H "Content-Type: application/json" \
  -d '{"ruc":"1790012345001","razonSocial":"Importadora XL Actualizada","limiteCredito":75000}'  # 200
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $api/1   # 204
curl -s -o /dev/null -w "%{http_code}\n" $api/1             # 404
```

Windows (PowerShell): el cuerpo JSON se guarda en un archivo y se envía con `-d "@archivo.json"`:

```powershell
$api = "http://localhost:8080/api/proveedores"
Set-Content -Path proveedor.json -Encoding ascii -Value '{"ruc":"1790012345001","razonSocial":"Importadora XL","limiteCredito":50000}'
curl.exe -s -i -X POST $api -H "Content-Type: application/json" -d "@proveedor.json"
curl.exe -s $api
curl.exe -s -X DELETE "$api/1" -w "HTTP %{http_code}`n"
```

Consola H2: http://localhost:8080/h2-console (URL JDBC `jdbc:h2:mem:proveedoresdb`, usuario `sa`, sin contraseña).
