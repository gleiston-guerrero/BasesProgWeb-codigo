# Capítulo 4 — CRUD de proveedores con FastAPI 0.162.2 (Pydantic 2.14.3, Uvicorn 0.54.0)

Proyecto del apartado «FastAPI — CRUD de proveedores» (Capítulo 4): `app/models.py` y `app/main.py` son los de los listados; `app/__init__.py` está vacío. Los datos se guardan en memoria y se pierden al detener el servidor.

## Requisitos

- Python 3.15 (probado con 3.15.2).
- Dependencias fijadas en `requirements.txt`.

## Ejecutar

Linux / macOS:

```bash
python3.15 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload     # http://127.0.0.1:8000  (documentación: /docs y /redoc)
```

Windows (PowerShell):

```powershell
py -V:3.15 -m venv .venv
.venv\Scripts\Activate.ps1
pip install -r requirements.txt
uvicorn app.main:app --reload
```

## Probar (otra terminal)

```bash
api=http://127.0.0.1:8000/api/proveedores
curl -s -i -X POST $api -H "Content-Type: application/json" \
  -d '{"ruc":"1790012345001","razon_social":"Importadora XL","limite_credito":"50000.00"}'   # 201 y cabecera location: /api/proveedores/1
curl -s -X POST $api -H "Content-Type: application/json" \
  -d '{"ruc":"1790012345001","razon_social":"Importadora XL","limite_credito":"50000.00"}'   # 409 {"detail":"RUC ya existe"}
curl -s -X POST $api -H "Content-Type: application/json" \
  -d '{"ruc":"179001234500","razon_social":"Importadora XL","limite_credito":"50000.00"}'    # 422
curl -s $api                                   # 200
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $api/1   # 204
curl -s $api/1                                 # 404 {"detail":"Proveedor no encontrado"}
```

En PowerShell: `curl.exe` y el cuerpo en un archivo (`-d "@proveedor.json"`).
