# app/rrhh.py -- todas las rutas del router comparten el prefijo
from fastapi import APIRouter, status

# dependencies=[Depends(...)] aplicaría además, por ejemplo,
# la verificación de credenciales a todas las rutas del grupo
router = APIRouter(prefix="/api/v1/rrhh/empleados", tags=["RRHH"])

@router.get("")
def index():
    return []

@router.post("", status_code=status.HTTP_201_CREATED)
def store(datos: dict):
    return datos

@router.get("/{id}")
def show(id: int):
    return {"id": id}

@router.put("/{id}")
def update(id: int, datos: dict):
    return datos

@router.delete("/{id}", status_code=status.HTTP_204_NO_CONTENT)
def destroy(id: int):
    return None
