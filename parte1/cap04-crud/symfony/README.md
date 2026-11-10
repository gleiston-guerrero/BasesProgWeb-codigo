# Capítulo 4 — CRUD de proveedores con Symfony 8.1.11 (PHP 8.5)

Proyecto del apartado «Symfony — CRUD de proveedores» (Capítulo 4), creado con:

```bash
composer create-project symfony/skeleton:"8.1.*" proveedores-symfony   # o: symfony new proveedores-symfony --version="8.1.*"
cd proveedores-symfony
composer require symfony/orm-pack
composer require --dev symfony/maker-bundle
composer require symfony/validator
composer require symfony/serializer-pack
php bin/console make:entity Proveedor -n
```

Archivos del libro: `src/Entity/Proveedor.php`, `src/Controller/ProveedorController.php` y `config/packages/serializer.yaml` (nombres en snake_case). `src/Repository/ProveedorRepository.php` es el que genera `make:entity`. Incluye la migración `migrations/Version20261110012537.php` que genera `make:migration`.

## Requisitos

- PHP 8.4.1 o superior (probado con PHP 8.5.12) con `pdo_sqlite`, `xml`, `mbstring`, `intl`.
- Composer 2. Symfony CLI es opcional.

## Ejecutar

Linux / macOS:

```bash
composer install
echo 'DATABASE_URL="sqlite:///%kernel.project_dir%/var/app.db"' > .env.local
php bin/console doctrine:migrations:migrate -n
php -S 127.0.0.1:8000 -t public          # o: symfony serve
```

Windows (PowerShell):

```powershell
composer install
Set-Content .env.local 'DATABASE_URL="sqlite:///%kernel.project_dir%/var/app.db"'
php bin/console doctrine:migrations:migrate -n
php -S 127.0.0.1:8000 -t public
```

(La migración ya está en `migrations/`; no hace falta ejecutar `make:migration`.)

## Probar

```bash
api=http://127.0.0.1:8000/api/proveedores; J="Content-Type: application/json"
curl -s -i -X POST $api -H "$J" -d '{"ruc":"1790012345001","razon_social":"Importadora XL","limite_credito":"50000.00"}'  # 201 + Location
curl -s -X POST $api -H "$J" -d '{"ruc":"1790012345001","razon_social":"Importadora XL","limite_credito":"50000.00"}'     # 409
curl -s -X POST $api -H "$J" -d '{"ruc":"179001234500","razon_social":"X","limite_credito":"-5"}'                        # 422
curl -s $api                                                                                                          # 200
curl -s -X PUT $api/1 -H "$J" -d '{"ruc":"1790012345001","razon_social":"Importadora XL Actualizada","limite_credito":"75000.00"}'  # 200
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $api/1    # 204
curl -s -o /dev/null -w "%{http_code}\n" $api/1              # 404
```

Con SQLite, el listado devuelve `"limite_credito":"50000"` (SQLite no conserva la escala); con PostgreSQL o MySQL, `"50000.00"`. En PowerShell: `curl.exe` y el cuerpo en un archivo (`-d "@proveedor.json"`).
