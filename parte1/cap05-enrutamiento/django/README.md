# Capítulo 5 — Enrutamiento en Django 6.1.3 + DRF 3.18.3

- `proveedores/views.py` y `proveedores/urls.py`: concatenación, en orden, de sus tres partes del libro (rutas básicas, parámetros de ruta, parámetros de consulta y cuerpo).
- `config/urls.py` y `rrhh/urls.py`: listado «Grupos de rutas en Django».
- `rrhh/views.py` (`EmpleadoViewSet` con respuestas simuladas) y `compras/urls.py` (lista vacía): complementos que el libro no reproduce.
- `config/settings.py`: `INSTALLED_APPS` con `rest_framework`, `proveedores`, `rrhh` y `compras`.

Creado con `django-admin startproject config .` y `python manage.py startapp proveedores` (y `rrhh`, `compras`).

## Ejecutar

```bash
python3.15 -m venv .venv && source .venv/bin/activate     # PowerShell: py -V:3.15 -m venv .venv; .venv\Scripts\Activate.ps1
pip install -r requirements.txt
python manage.py migrate
python manage.py runserver                                # http://127.0.0.1:8000
```

## Probar (las rutas de Django terminan en barra)

```bash
B=http://127.0.0.1:8000/api; J="Content-Type: application/json"
curl -s $B/proveedores/                         # 200
curl -s -X POST $B/proveedores/                 # 201
curl -s $B/contratos/1042/                      # 200 {"contrato_id":1042}
curl -s -o /dev/null -w "%{http_code}\n" $B/contratos/abc/   # 404 (el conversor <int:> no coincide)
curl -s $B/proveedores/1790012345001/           # 200 {"ruc":"1790012345001"}
curl -s "$B/contratos/?estado=activo"           # 200
curl -s "$B/contratos/?anio=abc"                # 400
curl -s -X POST $B/contratos/ -H "$J" -d '{"numero":"CT-1","monto":"100.50","fecha_fin":"2027-12-31"}'   # 201
curl -s -X POST $B/contratos/ -H "$J" -d '{"numero":"CT-1","fecha_fin":"2027-12-31"}'                    # 400
curl -s $B/v1/rrhh/empleados/                   # 200 []
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $B/v1/rrhh/empleados/7/   # 204
```
