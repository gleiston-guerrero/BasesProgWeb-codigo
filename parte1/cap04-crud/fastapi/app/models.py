# app/models.py
from decimal import Decimal
from pydantic import BaseModel, Field, field_validator

# Pydantic v2: los modelos son clases que heredan de BaseModel.
# Los campos con anotaciones de tipo se validan automáticamente.
# Si los datos de entrada no coinciden con el tipo, FastAPI devuelve 422.

class ProveedorBase(BaseModel):
    # Field() añade metadatos: descripción, ejemplo, restricciones.
    # min_length/max_length equivalen a @Size en Jakarta Bean Validation.
    ruc: str = Field(..., min_length=13, max_length=13,
        description="RUC del proveedor (13 dígitos)", examples=["1790012345001"])
    razon_social: str = Field(..., min_length=2, max_length=200)
    # Decimal y no float: el dinero exige precisión decimal exacta.
    # max_digits/decimal_places equivalen a DecimalField(max_digits=12, decimal_places=2).
    limite_credito: Decimal = Field(..., gt=0, max_digits=12, decimal_places=2,
        description="Importe máximo de crédito, mayor que cero")

    # @field_validator es el decorador de Pydantic v2 para validaciones personalizadas.
    # Equivale al método validate_<campo> de DRF.
    @field_validator('ruc')
    @classmethod
    def ruc_debe_ser_numerico(cls, v: str) -> str:
        if not v.isdigit():
            raise ValueError('El RUC debe contener solo dígitos')
        return v

class ProveedorCreate(ProveedorBase):
    """Esquema para creación: no incluye 'id' (lo genera la BD)."""
    pass

class ProveedorResponse(ProveedorBase):
    """Esquema de respuesta: incluye 'id' generado."""
    id: int

    # model_config permite configurar el comportamiento del modelo.
    # from_attributes=True habilita la creación desde objetos ORM (no solo dicts).
    model_config = {"from_attributes": True}
