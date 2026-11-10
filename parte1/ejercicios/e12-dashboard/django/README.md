# Parte I — Solución del ejercicio E1.2 en Django 6.1.3 + DRF 3.18.3: GET /api/dashboard/

Parte del proyecto del Capítulo 4 (`../../../cap04-crud/django`). Al final de `proveedores/models.py`, `proveedores/views.py` y `proveedores/urls.py` se añadieron los tres fragmentos del listado de la solución de E1.2 (modelo `Contrato`, vista `dashboard` y su ruta). Migraciones:

- `0002_contrato.py`: generada con `python manage.py makemigrations`.
- `0003_datos_ejemplo.py`: creada con `python manage.py makemigrations proveedores --empty -n datos_ejemplo` y completada con dos proveedores y dos contratos (uno vigente, otro vencido).

## Ejecutar

Linux / macOS:

```bash
python3.15 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python manage.py migrate
python manage.py runserver
```

Windows (PowerShell): `py -V:3.15 -m venv .venv` y `.venv\Scripts\Activate.ps1`; el resto, igual.

## Probar

```bash
curl -s http://127.0.0.1:8000/api/dashboard/
# {"totalProveedores":2,"contratosActivos":1,"fechaServidor":"2026-11-10T01:33:00.860170+00:00"}
```

(En PowerShell, `curl.exe`.) Las rutas de Django terminan en barra. El CRUD de proveedores sigue disponible en `/api/proveedores/`.
