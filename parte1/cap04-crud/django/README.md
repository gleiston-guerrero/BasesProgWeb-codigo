# Capítulo 4 — CRUD de proveedores con Django 6.1.3 + Django REST Framework 3.18.3

Proyecto del apartado «Django con Django REST Framework — CRUD de proveedores» (Capítulo 4). Se creó con las órdenes del libro (`django-admin startproject config .` y `python manage.py startapp proveedores`); los archivos `proveedores/models.py`, `serializers.py`, `views.py`, `urls.py` y `config/urls.py` son los de los listados, y en `config/settings.py` solo cambia `INSTALLED_APPS`. Incluye la migración `proveedores/migrations/0001_initial.py` que genera `makemigrations` (si se ejecuta de nuevo, responde «No changes detected»).

## Requisitos

- Python 3.15 (Django 6.1 admite 3.12–3.15; 3.15 desde 6.1.3).
- Dependencias fijadas en `requirements.txt`.

## Ejecutar

Linux / macOS (en esta carpeta):

```bash
python3.15 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python manage.py migrate
python manage.py runserver        # http://127.0.0.1:8000/
```

Windows (PowerShell):

```powershell
py -V:3.15 -m venv .venv
.venv\Scripts\Activate.ps1
pip install -r requirements.txt
python manage.py migrate
python manage.py runserver
```

## Probar (otra terminal)

```bash
api=http://127.0.0.1:8000/api/proveedores/
curl -s -i -X POST $api -H "Content-Type: application/json" \
  -d '{"ruc":"1790012345001","razon_social":"Importadora XL","limite_credito":"50000.00"}'   # 201 + Location .../1/
curl -s -X POST $api -H "Content-Type: application/json" \
  -d '{"ruc":"1790012345001","razon_social":"Importadora XL","limite_credito":"50000.00"}'   # 400 (RUC duplicado)
curl -s -X POST $api -H "Content-Type: application/json" \
  -d '{"ruc":"179001234500","razon_social":"Otra","limite_credito":"-5"}'                   # 400 (dos errores)
curl -s $api                                                                                # 200
curl -s -X PUT ${api}1/ -H "Content-Type: application/json" \
  -d '{"ruc":"1790012345001","razon_social":"Importadora XL Actualizada","limite_credito":"75000"}'  # 200
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE ${api}1/                                 # 204
curl -s ${api}1/                                                                            # 404
```

En PowerShell: `curl.exe`, acento grave para continuar líneas y el cuerpo en un archivo (`-d "@proveedor.json"`). Las rutas de Django terminan en barra (`/api/proveedores/1/`).
