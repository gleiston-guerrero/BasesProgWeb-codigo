# proveedores/models.py
from decimal import Decimal
from django.core.validators import MinValueValidator
from django.db import models

# En Django, los modelos son clases Python que heredan de models.Model.
# Django ORM traduce esta clase a una tabla SQL automáticamente.
# El campo 'id' es creado automáticamente por Django (BigAutoField por defecto).
class Proveedor(models.Model):

    # CharField: campo de texto con longitud máxima obligatoria.
    # unique=True: crea un índice único en la BD (equivale a @Column(unique=true) en JPA).
    ruc = models.CharField(max_length=13, unique=True)

    # blank=False (defecto): el campo es obligatorio en los formularios.
    # null=False (defecto): la BD no permite NULL en este campo.
    razon_social = models.CharField(max_length=200)

    # DecimalField: número decimal con precisión exacta (sin errores de punto flotante).
    # max_digits: dígitos totales. decimal_places: dígitos después del punto.
    # MinValueValidator exige un límite mayor que cero; ModelSerializer lo copia
    # al serializador, de modo que un límite negativo se rechaza con 400.
    limite_credito = models.DecimalField(
        max_digits=12, decimal_places=2,
        validators=[MinValueValidator(Decimal("0.01"))])

    # Meta contiene metadatos del modelo.
    class Meta:
        # db_table define el nombre de la tabla en la BD.
        db_table = "proveedores"

    # __str__ define la representación en texto del objeto (útil en el admin de Django).
    def __str__(self):
        return f"{self.ruc} - {self.razon_social}"


# proveedores/models.py (se añade al final)
class Contrato(models.Model):
    numero = models.CharField(max_length=20, unique=True)
    valor = models.DecimalField(max_digits=12, decimal_places=2)
    proveedor = models.ForeignKey(Proveedor, on_delete=models.PROTECT)  # columna proveedor_id
    fecha_inicio = models.DateField()
    fecha_vencimiento = models.DateField()

    class Meta:
        db_table = "contratos"
