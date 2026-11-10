# Capítulo 5 — Enrutamiento en Laravel 13.40 (PHP 8.5)

Reúne en `routes/api.php`, en este orden, los cuatro listados de Laravel del Capítulo 5: rutas básicas, parámetros de ruta, parámetros de consulta y cuerpo, y grupos de rutas. `app/Http/Controllers/EmpleadoController.php` (respuestas simuladas) lo usa el grupo `v1/rrhh`. Proyecto creado como el del Capítulo 4 (`composer create-project laravel/laravel ... "13.*"` y `php artisan install:api`).

## Ejecutar

```bash
composer install
cp .env.example .env            # PowerShell: Copy-Item .env.example .env
php artisan key:generate
touch database/database.sqlite  # PowerShell: New-Item database\database.sqlite -ItemType File
php artisan migrate
php artisan serve               # http://127.0.0.1:8000
```

## Probar

```bash
B=http://127.0.0.1:8000/api; H="Accept: application/json"
curl -s -H "$H" $B/proveedores                       # 200 {"total":142,"activos":138}
curl -s -H "$H" -X POST $B/proveedores               # 201 {"creado":true}
curl -s -H "$H" $B/contratos/1042                    # 200 {"contrato_id":1042}
curl -s -H "$H" $B/empleados/7                       # 200; /empleados/abc -> 404 (restricción [0-9]+)
curl -s -H "$H" $B/reportes                          # 200 {"anio":2026}; /reportes/2025 -> {"anio":2025}
curl -s -H "$H" "$B/contratos?estado=activo"         # 200 {"estado":"activo","anio":2026,"pagina":1}
curl -s -H "$H" -X POST $B/contratos -H "Content-Type: application/json" \
  -d '{"numero":"CT-1","monto":100.50,"fecha_fin":"2027-12-31"}'   # 201
curl -s -H "$H" -X POST $B/contratos -H "Content-Type: application/json" \
  -d '{"numero":"CT-1","fecha_fin":"2027-12-31"}'                  # 422 (falta monto)
curl -s -H "$H" $B/v1/rrhh/empleados                 # 401 (auth:sanctum sin token)
```

Sin la cabecera `Accept: application/json`, la ruta protegida responde 500 («Route [login] not defined»): el middleware de autenticación intenta redirigir a una página de inicio de sesión que una API no tiene. En PowerShell: `curl.exe`.
