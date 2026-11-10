# config/urls.py
from django.contrib import admin
from django.urls import include, path

urlpatterns = [
    path("admin/", admin.site.urls),
    # include() incorpora todas las rutas definidas en proveedores/urls.py
    path("", include("proveedores.urls")),
]
