# proveedores/migrations/0003_datos_ejemplo.py -- E1.2: datos de ejemplo
# (dos proveedores; un contrato vigente y otro vencido). Creada con:
#   python manage.py makemigrations proveedores --empty -n datos_ejemplo
from datetime import date
from decimal import Decimal

from django.db import migrations


def cargar(apps, schema_editor):
    Proveedor = apps.get_model("proveedores", "Proveedor")
    Contrato = apps.get_model("proveedores", "Contrato")
    p1 = Proveedor.objects.create(ruc="1790012345001", razon_social="Importadora XL",
                                  limite_credito=Decimal("50000.00"))
    p2 = Proveedor.objects.create(ruc="0990012345001", razon_social="Aceros del Pacífico",
                                  limite_credito=Decimal("20000.00"))
    Contrato.objects.create(numero="CT-2026-001", valor=Decimal("12500.50"), proveedor=p1,
                            fecha_inicio=date(2026, 1, 1), fecha_vencimiento=date(2027, 12, 31))
    Contrato.objects.create(numero="CT-2025-009", valor=Decimal("3000.00"), proveedor=p2,
                            fecha_inicio=date(2025, 1, 1), fecha_vencimiento=date(2025, 12, 31))


class Migration(migrations.Migration):

    dependencies = [
        ("proveedores", "0002_contrato"),
    ]

    operations = [
        migrations.RunPython(cargar, migrations.RunPython.noop),
    ]
