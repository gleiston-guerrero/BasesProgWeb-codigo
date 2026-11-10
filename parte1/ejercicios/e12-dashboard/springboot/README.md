# Parte I — Solución del ejercicio E1.2 en Spring Boot 4.1.2: GET /api/dashboard

Parte del proyecto de la Práctica 4 (`../../../cap04-crud/practica4-contratos`) con estos cambios:

- `model/Contrato.java`: campos `fechaInicio` y `fechaVencimiento` (`LocalDate`) con sus getters y setters.
- `model/Proveedor.java`: la entidad del Capítulo 4 (paquete cambiado a `com.empresa.contratos.model`).
- `repository/ProveedorRepository.java` y la consulta derivada `countByFechaInicioLessThanEqualAndFechaVencimientoGreaterThanEqual` en `ContratoRepository`.
- `controller/DashboardController.java`: el listado de la solución de E1.2.
- `data.sql` con dos proveedores y dos contratos (uno vigente hasta el 31-12-2027 y otro vencido) y `spring.jpa.defer-datasource-initialization=true`.

## Ejecutar y probar

Linux / macOS: `./mvnw spring-boot:run` · Windows (PowerShell): `.\mvnw.cmd spring-boot:run`

```bash
curl -s http://localhost:8080/api/dashboard
# {"totalProveedores":2,"fechaServidor":"2026-11-10T02:15:32.15...+01:00","contratosActivos":1}
```

(En PowerShell, `curl.exe`.) El orden de las claves del JSON puede variar: `Map.of` no garantiza orden.
