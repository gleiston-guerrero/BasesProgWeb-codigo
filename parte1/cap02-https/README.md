# Capítulo 2 — HTTPS y HTTP/2 con Spring Boot (Sección 2.4.1)

Proyecto mínimo que acompaña la sección «HTTPS y seguridad en tránsito» del Capítulo 2 del libro
*Bases de la Programación Web: Desarrollo Back-End*. Expone un único *endpoint* de solo lectura,
`GET /api/proveedores/{id}`, servido **solo por HTTPS** en el puerto 8443 con HTTP/2 (`h2`).

| Archivo | Listado del libro |
| --- | --- |
| `src/main/java/com/comercializadora/sgpc/SgpcApplication.java` | Aplicación y controlador de la demostración (archivo completo) |
| `src/main/resources/application.properties` | Listado «application.properties — HTTPS, HTTP/2 y cookie de sesión segura» (archivo completo) |
| `pom.xml` | Dependencia `spring-boot-starter-webmvc` |

El almacén de claves `src/main/resources/sgpc-local.p12` y el certificado `sgpc-local.crt` **no se
versionan** (están en `.gitignore`): cada lector los genera con `keytool`, como indica el libro.

## Requisitos

- JDK 25 (LTS). Spring Boot 4.1.2 requiere Java 17 o superior.
- No hace falta instalar Maven: se usa el Maven Wrapper incluido (`mvnw` / `mvnw.cmd`, Maven 3.9.16).
- curl (en Windows, `curl.exe`, incluido en Windows 10 y 11).

## 1. Generar el almacén de claves y el certificado (una sola vez)

Desde esta carpeta (`parte1/cap02-https`). `keytool` pide la contraseña del almacén; se usa la misma
en las dos órdenes y después en la variable de entorno del paso 2.

Windows (PowerShell):

```powershell
keytool -genkeypair -alias sgpc -keyalg RSA `
  -keysize 2048 -storetype PKCS12 -validity 365 `
  -keystore src\main\resources\sgpc-local.p12 `
  -dname "CN=localhost, O=Comercializadora, C=EC" `
  -ext "SAN=dns:localhost,ip:127.0.0.1"

keytool -exportcert -rfc -alias sgpc `
  -keystore src\main\resources\sgpc-local.p12 `
  -file sgpc-local.crt
```

Linux y macOS:

```bash
keytool -genkeypair -alias sgpc -keyalg RSA \
  -keysize 2048 -storetype PKCS12 -validity 365 \
  -keystore src/main/resources/sgpc-local.p12 \
  -dname "CN=localhost, O=Comercializadora, C=EC" \
  -ext "SAN=dns:localhost,ip:127.0.0.1"

keytool -exportcert -rfc -alias sgpc \
  -keystore src/main/resources/sgpc-local.p12 \
  -file sgpc-local.crt
```

## 2. Ejecutar

Windows (PowerShell):

```powershell
$env:SGPC_KEYSTORE_PASSWORD = "clave-del-almacen"
.\mvnw.cmd spring-boot:run
```

Linux y macOS:

```bash
export SGPC_KEYSTORE_PASSWORD='clave-del-almacen'
./mvnw spring-boot:run
```

El registro debe mostrar `Tomcat started on port 8443 (https) with context path '/'`.

## 3. Probar (en otra terminal, desde esta misma carpeta)

```bash
curl https://localhost:8443/api/proveedores/1
# curl: (60) SSL certificate problem: self-signed certificate

curl -v --cacert sgpc-local.crt https://localhost:8443/api/proveedores/1
# ... ALPN: server accepted h2 ... SSL certificate verify ok.
# < HTTP/2 200
# {"id":1,"ruc":"1790012345001","razonSocial":"Importadora XL S.A.","limiteCredito":50000.00}

curl http://localhost:8443/api/proveedores/1
# Bad Request
# This combination of host and port requires TLS.
```

En Windows PowerShell se escribe `curl.exe` en lugar de `curl`.

## Comprobación sin ejecutar el servidor

```bash
./mvnw -B -q verify        # Windows: .\mvnw.cmd -B -q verify
```
