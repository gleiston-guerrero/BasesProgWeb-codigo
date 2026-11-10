# rrhh/urls.py -- rutas del módulo RRHH
from rest_framework.routers import DefaultRouter
from . import views

router = DefaultRouter()
router.register(r'empleados', views.EmpleadoViewSet, basename='empleado')
urlpatterns = router.urls
# Genera automáticamente: GET/POST /empleados/ y GET/PUT/PATCH/DELETE /empleados/{id}/
