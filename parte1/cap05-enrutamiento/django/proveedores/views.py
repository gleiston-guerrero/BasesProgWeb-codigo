# proveedores/views.py (primera parte)
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status

# Una sola vista para la colección: la URI nombra el recurso (sustantivo)
# y el método HTTP decide la acción (GET lista, POST crea).
@api_view(['GET', 'POST'])
def proveedores(request):
    if request.method == 'POST':
        return Response({'creado': True}, status=status.HTTP_201_CREATED)
    return Response({'total': 142, 'activos': 138})


# proveedores/views.py (segunda parte)
@api_view(['GET'])
def detalle_contrato(request, id):
    return Response({'contrato_id': id})

@api_view(['GET'])
def proveedor_por_ruc(request, ruc):
    return Response({'ruc': ruc})


# proveedores/views.py (tercera parte). api_view, Response y status ya se
# importaron en la primera parte; solo se importan los nombres nuevos.
from datetime import date
from decimal import Decimal
from rest_framework import serializers

# Query strings: un Serializer convierte y valida request.query_params
class FiltroSerializer(serializers.Serializer):
    estado = serializers.CharField(default='todos')
    # default admite una función: sin ?anio=..., el año en curso
    anio   = serializers.IntegerField(default=lambda: date.today().year)
    pagina = serializers.IntegerField(default=1)

# Cuerpo JSON: request.data validado con las mismas reglas que Laravel
class ContratoSerializer(serializers.Serializer):
    numero    = serializers.CharField(max_length=20)
    monto     = serializers.DecimalField(max_digits=12, decimal_places=2,
                                        min_value=Decimal('0.01'))
    fecha_fin = serializers.DateField()

    def validate_fecha_fin(self, valor):
        if valor <= date.today():
            raise serializers.ValidationError('La fecha de fin debe ser posterior a hoy.')
        return valor

# Una sola vista para la colección, como en el Listado de rutas básicas:
# GET lista con filtros; POST valida el cuerpo y crea
@api_view(['GET', 'POST'])
def contratos(request):
    if request.method == 'POST':
        serializer = ContratoSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)    # 400 con los errores si falla
        return Response({'mensaje': 'Contrato recibido'}, status=status.HTTP_201_CREATED)
    filtro = FiltroSerializer(data=request.query_params)
    filtro.is_valid(raise_exception=True)            # ?anio=abc -> 400
    return Response(filtro.validated_data)
