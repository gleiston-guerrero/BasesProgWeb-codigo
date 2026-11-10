# Capítulo 4 — CRUD de proveedores con Laravel 13.40 (PHP 8.5)

Proyecto del apartado «Laravel — CRUD de proveedores» (Capítulo 4). Se creó con las órdenes del libro:

```bash
composer create-project laravel/laravel proveedores-api "13.*"   # plantilla laravel/laravel v13.11.0
cd proveedores-api
php artisan install:api
php artisan make:model Proveedor --controller --api
php artisan make:migration create_proveedores_table
```

y se sustituyeron por completo cuatro archivos con los listados del libro:
`database/migrations/2026_01_01_000000_create_proveedores_table.php`, `app/Models/Proveedor.php`, `app/Http/Controllers/ProveedorController.php` y `routes/api.php`.
No se incluyen `vendor/`, `.env` ni la base de datos SQLite (se regeneran con las órdenes de abajo).

## Requisitos

- PHP 8.3 o superior (probado con PHP 8.5.12) con las extensiones `pdo_sqlite`, `mbstring`, `xml`, `curl`, `zip`.
- Composer 2.

## Ejecutar

Linux / macOS (en esta carpeta):

```bash
composer install
cp .env.example .env
php artisan key:generate
touch database/database.sqlite
php artisan migrate
php artisan serve          # http://127.0.0.1:8000
```

Windows (PowerShell):

```powershell
composer install
Copy-Item .env.example .env
php artisan key:generate
New-Item database\database.sqlite -ItemType File
php artisan migrate
php artisan serve
```

## Probar (otra terminal)

```bash
api=http://127.0.0.1:8000/api/proveedores
curl -s -i -X POST $api -H "Content-Type: application/json" -H "Accept: application/json" \
  -d '{"ruc":"1790012345001","razon_social":"Importadora XL","limite_credito":50000}'   # 201 + Location
curl -s -X POST $api -H "Content-Type: application/json" -H "Accept: application/json" \
  -d '{"ruc":"1790012345001","razon_social":"Importadora XL","limite_credito":50000}'   # 422 (RUC duplicado)
curl -s -X POST $api -H "Content-Type: application/json" -H "Accept: application/json" \
  -d '{"ruc":"179001234500","razon_social":"X","limite_credito":-5}'                    # 422 (dos errores)
curl -s -H "Accept: application/json" $api                                             # 200
curl -s -X PUT $api/1 -H "Content-Type: application/json" -H "Accept: application/json" \
  -d '{"ruc":"1790012345001","razon_social":"Importadora XL Actualizada","limite_credito":75000}'  # 200
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $api/1 -H "Accept: application/json"           # 204
curl -s -o /dev/null -w "%{http_code}\n" -H "Accept: application/json" $api/1                    # 404
```

`bootstrap/app.php` (plantilla 13.8 o posterior) responde con JSON a cualquier error en las rutas `api/*`, de modo que sin la cabecera `Accept` también se obtiene 422; en proyectos creados con una plantilla anterior, sin `Accept` se obtiene una redirección 302.

En PowerShell: `curl.exe`, acento grave para continuar líneas y el cuerpo en un archivo (`-d "@proveedor.json"`).
