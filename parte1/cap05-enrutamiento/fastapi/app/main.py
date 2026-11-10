# app/main.py (primera parte)
from datetime import date
from fastapi import FastAPI, status

app = FastAPI()

# Rutas básicas: el decorador declara método HTTP y URI sobre la función
@app.get("/api/proveedores")
def listar():
    return {"total": 142, "activos": 138}

@app.post("/api/proveedores", status_code=status.HTTP_201_CREATED)
def crear():
    return {"creado": True}

# Parámetro de ruta: {id} se convierte a int; "abc" produce un error 422
@app.get("/api/contratos/{id}")
def detalle(id: int):
    return {"contrato_id": id}

# Parámetro de consulta opcional: sin ?anio=..., se usa el año en curso
@app.get("/api/reportes")
def reporte(anio: int | None = None):
    return {"anio": anio if anio is not None else date.today().year}


# app/main.py (segunda parte). date, FastAPI, status y app ya están
# definidos en la primera parte; solo se importan las clases nuevas.
from decimal import Decimal
from pydantic import BaseModel, Field, field_validator

# Query strings: argumentos de la función que no aparecen en la ruta
@app.get("/api/contratos")
def listar_contratos(estado: str = "todos", anio: int | None = None, pagina: int = 1):
    return {"estado": estado,
            "anio": anio if anio is not None else date.today().year,
            "pagina": pagina}

# Cuerpo JSON: un modelo Pydantic con las mismas reglas que Laravel
class ContratoRequest(BaseModel):
    numero: str = Field(min_length=1, max_length=20)
    monto: Decimal = Field(ge=Decimal("0.01"))
    fecha_fin: date

    @field_validator("fecha_fin")
    @classmethod
    def fecha_futura(cls, v: date) -> date:
        if v <= date.today():
            raise ValueError("La fecha de fin debe ser posterior a hoy")
        return v

@app.post("/api/contratos", status_code=status.HTTP_201_CREATED)
def crear_contrato(req: ContratoRequest):    # cuerpo inválido -> 422
    return {"mensaje": "Contrato recibido"}


# app/main.py (tercera parte) -- registro del grupo en la aplicación ya creada
from .rrhh import router as rrhh_router

app.include_router(rrhh_router)
