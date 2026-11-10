# app/main.py
from fastapi import FastAPI, HTTPException, status
from typing import List
from .models import ProveedorCreate, ProveedorResponse

# FastAPI() crea la instancia de la aplicación.
# title, description y version aparecen en la documentación OpenAPI automática
# accesible en http://localhost:8000/docs
app = FastAPI(
    title="API de Proveedores",
    description="CRUD básico de proveedores — FastAPI con Pydantic v2",
    version="1.0.0"
)

# Almacén en memoria: un diccionario que se pierde al detener el servidor.
# Una aplicación real usaría una base de datos (por ejemplo, con SQLAlchemy).
_bd: dict[int, dict] = {}
_siguiente_id = 1

# GET /proveedores → lista todos
# List[ProveedorResponse]: FastAPI usa este tipo para serializar la respuesta a JSON.
# El decorador @app.get equivale a @GetMapping en Spring Boot.
@app.get("/api/proveedores", response_model=List[ProveedorResponse])
def listar():
    return [ProveedorResponse(id=k, **v) for k, v in _bd.items()]

# GET /proveedores/{id} → uno o 404
# Path parameter {proveedor_id} se declara como argumento de la función.
@app.get("/api/proveedores/{proveedor_id}", response_model=ProveedorResponse)
def leer(proveedor_id: int):
    if proveedor_id not in _bd:
        # HTTPException genera la respuesta de error con el código indicado.
        raise HTTPException(status_code=404, detail="Proveedor no encontrado")
    return ProveedorResponse(id=proveedor_id, **_bd[proveedor_id])

# POST /proveedores → crear (201 Created)
# status_code=201 define el código de respuesta para el caso exitoso.
# El cuerpo es deserializado y validado por Pydantic automáticamente.
@app.post("/api/proveedores", response_model=ProveedorResponse,
    status_code=status.HTTP_201_CREATED)
def crear(proveedor: ProveedorCreate):
    global _siguiente_id
    # Verificar RUC duplicado
    if any(v['ruc'] == proveedor.ruc for v in _bd.values()):
        raise HTTPException(status_code=409, detail="RUC ya existe")
    nuevo_id = _siguiente_id
    _siguiente_id += 1
    # model_dump() convierte el modelo Pydantic a diccionario Python.
    _bd[nuevo_id] = proveedor.model_dump()
    return ProveedorResponse(id=nuevo_id, **_bd[nuevo_id])

# PUT /proveedores/{id} → reemplazar
@app.put("/api/proveedores/{proveedor_id}", response_model=ProveedorResponse)
def actualizar(proveedor_id: int, datos: ProveedorCreate):
    if proveedor_id not in _bd:
        raise HTTPException(status_code=404, detail="Proveedor no encontrado")
    # El RUC no puede pertenecer a OTRO proveedor (el propio sí puede conservarlo)
    if any(v['ruc'] == datos.ruc for k, v in _bd.items() if k != proveedor_id):
        raise HTTPException(status_code=409, detail="RUC ya existe")
    _bd[proveedor_id] = datos.model_dump()
    return ProveedorResponse(id=proveedor_id, **_bd[proveedor_id])

# DELETE /proveedores/{id} → eliminar (204)
@app.delete("/api/proveedores/{proveedor_id}", status_code=status.HTTP_204_NO_CONTENT)
def eliminar(proveedor_id: int):
    if proveedor_id not in _bd:
        raise HTTPException(status_code=404, detail="Proveedor no encontrado")
    del _bd[proveedor_id]
    # FastAPI no incluye cuerpo en respuestas 204 (no retornar nada)
