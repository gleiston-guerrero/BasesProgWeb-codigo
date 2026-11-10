# proveedores/serializers.py
from rest_framework import serializers
from .models import Proveedor

# ModelSerializer genera automáticamente los campos del serializador
# a partir del modelo Django, incluyendo las validaciones (max_length, unique).
class ProveedorSerializer(serializers.ModelSerializer):
    class Meta:
        model = Proveedor
        # fields define qué campos se incluyen en la respuesta JSON.
        # '__all__' incluye todos los campos del modelo.
        fields = '__all__'

    # Validación personalizada a nivel de campo:
    # Los métodos validate_<campo> se ejecutan automáticamente.
    def validate_ruc(self, value):
        if len(value) != 13:
            raise serializers.ValidationError("El RUC debe tener exactamente 13 dígitos.")
        if not value.isdigit():
            raise serializers.ValidationError("El RUC debe contener solo dígitos.")
        return value
