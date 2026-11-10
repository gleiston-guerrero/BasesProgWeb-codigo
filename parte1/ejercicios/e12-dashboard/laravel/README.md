# Parte I — Solución del ejercicio E1.2 en Laravel 13.40 (PHP 8.5): GET /api/dashboard

Parte del proyecto del Capítulo 4 (`../../../cap04-crud/laravel`) con estos añadidos:

- `app/Models/Contrato.php` (creado con `php artisan make:model Contrato`).
- `database/migrations/2026_01_02_000000_create_contratos_table.php`: columnas `numero`, `valor`, `proveedor_id`, `fecha_inicio`, `fecha_vencimiento`.
- `database/seeders/DashboardSeeder.php` (creado con `php artisan make:seeder DashboardSeeder`): dos proveedores y dos contratos (uno vigente, otro vencido).
- Al final de `routes/api.php`: el listado de la solución de E1.2 (ruta `/dashboard`).

## Ejecutar

Linux / macOS:

```bash
composer install
cp .env.example .env
php artisan key:generate
touch database/database.sqlite
php artisan migrate --seed --seeder=DashboardSeeder
php artisan serve
```

Windows (PowerShell): igual, con `Copy-Item .env.example .env` y `New-Item database\database.sqlite -ItemType File`.

## Probar

```bash
curl -s http://127.0.0.1:8000/api/dashboard
# {"totalProveedores":2,"contratosActivos":1,"fechaServidor":"2026-11-10T01:32:35+00:00"}
```

(En PowerShell, `curl.exe`.) El CRUD de proveedores del Capítulo 4 sigue disponible en `/api/proveedores`.
