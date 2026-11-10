# Parte I — Solución del ejercicio E1.5: idempotencia observada con curl

No es un proyecto nuevo: la secuencia se envía al CRUD de proveedores de Spring Boot del Capítulo 4 (`parte1/cap04-crud/springboot`), que debe estar en ejecución y recién arrancado (la base H2 en memoria empieza vacía, de modo que el primer proveedor recibe el id 1).

Archivos:

- `secuencia.sh`: las ocho peticiones de la solución (listado de E1.5 del libro).
- `proveedor.json` y `proveedor-put.json`: cuerpos del POST y del PUT.

## Ejecutar

Terminal 1, en `parte1/cap04-crud/springboot`: `./mvnw spring-boot:run` (Linux/macOS) o `.\mvnw.cmd spring-boot:run` (Windows PowerShell).

Terminal 2, en esta carpeta:

- Linux / macOS (o Git Bash en Windows): `./secuencia.sh`
- Windows PowerShell, petición a petición con `curl.exe`, por ejemplo:
  `curl.exe -s -i -X POST http://localhost:8080/api/proveedores -H "Content-Type: application/json" -d "@proveedor.json"`

## Salida obtenida (10-11-2026, OpenJDK 25.0.4, Spring Boot 4.1.2)

```
1. POST:
HTTP/1.1 201
Location: /api/proveedores/1
2. POST repetido:
409
3. PUT:
200
4. PUT repetido:
200
5. GET colección:
[{"ruc":"1790012345001","razonSocial":"Importadora XL Andina","limiteCredito":75000.00,"id":1}]
6. DELETE:
204
7. DELETE repetido:
404
8. GET colección:
[]
```

Si la aplicación no se reinicia entre dos ejecuciones, el proveedor creado recibe otro id y los pasos 3, 4, 6 y 7 responden 404: el script supone una base vacía.
