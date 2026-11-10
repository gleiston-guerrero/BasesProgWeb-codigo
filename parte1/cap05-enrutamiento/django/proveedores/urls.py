# proveedores/urls.py (primera parte; el proyecto la incluye bajo 'api/')
from django.urls import path
from . import views

urlpatterns = [
    path('proveedores/', views.proveedores),   # GET y POST sobre la misma URI
]

# proveedores/urls.py (segunda parte) -- conversores de tipo en los patrones
urlpatterns += [
    # <int:id> -- Django convierte el segmento a entero; /contratos/abc/ -> 404
    path('contratos/<int:id>/', views.detalle_contrato),
    # <str:ruc> -- segmento como cadena de texto
    path('proveedores/<str:ruc>/', views.proveedor_por_ruc),
]

# proveedores/urls.py (tercera parte)
urlpatterns += [
    path('contratos/', views.contratos),   # GET y POST sobre la misma URI
]
