# Capítulo 5 — Enrutamiento en FastAPI 0.162.2

`app/main.py` es la concatenación, en orden, de sus tres partes del libro (rutas básicas; parámetros de consulta y cuerpo; registro del grupo); `app/rrhh.py` es el grupo de rutas con `APIRouter`; `app/__init__.py` está vacío.

## Ejecutar

```bash
python3.15 -m venv .venv && source .venv/bin/activate     # PowerShell: py -V:3.15 -m venv .venv; .venv\Scripts\Activate.ps1
pip install -r requirements.txt
uvicorn app.main:app --reload                            # http://127.0.0.1:8000 (documentación en /docs)
```

## Probar

```bash
B=http://127.0.0.1:8000/api; J="Content-Type: application/json"
curl -s $B/proveedores                          # 200
curl -s -X POST $B/proveedores                  # 201 {"creado":true}
curl -s $B/contratos/1042                       # 200 {"contrato_id":1042}
curl -s $B/contratos/abc                        # 422 (int_parsing)
curl -s "$B/reportes?anio=2025"                 # 200 {"anio":2025}
curl -s "$B/contratos?estado=activo"            # 200
curl -s -X POST $B/contratos -H "$J" -d '{"numero":"CT-1","monto":"100.50","fecha_fin":"2027-12-31"}'   # 201
curl -s -X POST $B/contratos -H "$J" -d '{"numero":"CT-1","fecha_fin":"2027-12-31"}'                    # 422
curl -s $B/v1/rrhh/empleados                    # 200 []
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $B/v1/rrhh/empleados/7   # 204
```
