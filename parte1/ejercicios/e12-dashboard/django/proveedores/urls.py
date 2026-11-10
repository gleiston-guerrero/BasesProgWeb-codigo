# proveedores/urls.py
from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import ProveedorViewSet

# DefaultRouter genera automáticamente las rutas para todas las acciones del ViewSet:
#   GET    /proveedores/         -> list
#   POST   /proveedores/         -> create
#   GET    /proveedores/{id}/    -> retrieve
#   PUT    /proveedores/{id}/    -> update
#   DELETE /proveedores/{id}/    -> destroy
# Una sola línea de registro reemplaza cinco rutas manuales.
router = DefaultRouter()
router.register(r'proveedores', ProveedorViewSet, basename='proveedor')

urlpatterns = [
    path('api/', include(router.urls)),
]


# proveedores/urls.py (se añade al final)
from .views import dashboard

urlpatterns += [
    path('api/dashboard/', dashboard),   # en Django, la ruta termina en barra
]
