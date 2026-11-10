# proveedores/views.py
from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.reverse import reverse
from .models import Proveedor
from .serializers import ProveedorSerializer

# ModelViewSet proporciona automáticamente los cinco métodos CRUD:
#   list()    -> GET /proveedores/
#   retrieve()-> GET /proveedores/{id}/
#   create()  -> POST /proveedores/
#   update()  -> PUT /proveedores/{id}/
#   destroy() -> DELETE /proveedores/{id}/
# Al heredar de ModelViewSet, el desarrollador solo necesita declarar
# el queryset y el serializer; los métodos HTTP ya están implementados.
class ProveedorViewSet(viewsets.ModelViewSet):
    # queryset define el conjunto de objetos sobre el que opera el ViewSet.
    queryset = Proveedor.objects.all()
    serializer_class = ProveedorSerializer

    # El create() heredado ya valida, guarda y responde 201 Created, pero solo
    # añade la cabecera Location si el serializador incluye un campo 'url'
    # (no es el caso de ProveedorSerializer). Se sobrescribe para añadirla.
    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        proveedor = serializer.save()
        # 'proveedor-detail' es el nombre de ruta que el router genera a partir
        # de basename='proveedor'; reverse() de DRF devuelve la URL absoluta.
        ubicacion = reverse('proveedor-detail', args=[proveedor.pk], request=request)
        return Response(serializer.data, status=status.HTTP_201_CREATED,
                        headers={'Location': ubicacion})
