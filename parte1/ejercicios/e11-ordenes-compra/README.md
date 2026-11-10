# Parte I — Solución del ejercicio E1.1: rutas de órdenes de compra (Spring Boot 4.1.2)

Proyecto generado en https://start.spring.io (Maven, Java 25, Spring Boot 4.1.2, Group `com.importadora`, Artifact `ordenes-compra`, Name `oc`, Package name `com.importadora.oc`, dependencia Spring Web). Solo se añadió `src/main/java/com/importadora/oc/OrdenCompraController.java` (listado de la solución de E1.1). Respuestas simuladas, sin base de datos.

## Ejecutar

Linux / macOS: `./mvnw spring-boot:run` · Windows (PowerShell): `.\mvnw.cmd spring-boot:run` (puerto 8080).

## Probar

```bash
oc=http://localhost:8080/api/ordenes-compra
curl -s "$oc?estado=pendiente&desde=2026-01-01&hasta=2026-12-31"          # 200
curl -s -o /dev/null -w "%{http_code}\n" "$oc?desde=31-01-2026"          # 400 (fecha no ISO)
curl -s $oc/5                                                            # 200
curl -s -o /dev/null -w "%{http_code}\n" $oc/500                         # 404 simulado
curl -s -i -X POST $oc -H "Content-Type: application/json" -d '{"proveedor":7}'   # 201 + Location
curl -s -X PATCH $oc/5/aprobacion                                        # 200
curl -s -X PATCH $oc/5/rechazo                                           # 200
curl -s -X PATCH $oc/5/anulacion                                         # 200
```

En PowerShell: `curl.exe` y, para el POST, `-d "@orden.json"`.
