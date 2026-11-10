# Parte I — Solución del ejercicio E1.2: GET /api/dashboard en tres stacks

| Carpeta | Base | Ruta |
|---|---|---|
| `springboot/` | Práctica 4 (`../../cap04-crud/practica4-contratos`) | `GET /api/dashboard` |
| `laravel/` | CRUD del Capítulo 4 (`../../cap04-crud/laravel`) | `GET /api/dashboard` |
| `django/` | CRUD del Capítulo 4 (`../../cap04-crud/django`) | `GET /api/dashboard/` |

Los tres cargan los mismos datos de ejemplo (dos proveedores; un contrato vigente hasta el 31-12-2027 y otro vencido) y responden:

```json
{"totalProveedores":2,"contratosActivos":1,"fechaServidor":"2026-11-10T01:32:35+00:00"}
```

Cada carpeta tiene su propio README con las órdenes para Linux/macOS y Windows (PowerShell).
