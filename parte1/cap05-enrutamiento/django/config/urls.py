# config/urls.py -- urls.py del proyecto: agrupa por módulo con include()
from django.urls import path, include

urlpatterns = [
    path('api/', include('proveedores.urls')),     # Listados anteriores
    path('api/v1/rrhh/', include('rrhh.urls')),
    path('api/v1/compras/', include('compras.urls')),
]
