# rrhh/views.py -- EmpleadoViewSet con respuestas simuladas (sin base de datos).
# Un ViewSet agrupa las acciones; DefaultRouter genera sus rutas.
from rest_framework import status, viewsets
from rest_framework.response import Response


class EmpleadoViewSet(viewsets.ViewSet):
    def list(self, request):                       # GET /api/v1/rrhh/empleados/
        return Response([])

    def create(self, request):                     # POST /api/v1/rrhh/empleados/
        return Response(request.data, status=status.HTTP_201_CREATED)

    def retrieve(self, request, pk=None):          # GET /api/v1/rrhh/empleados/{id}/
        return Response({'id': int(pk)})

    def update(self, request, pk=None):            # PUT /api/v1/rrhh/empleados/{id}/
        return Response(request.data)

    def partial_update(self, request, pk=None):    # PATCH /api/v1/rrhh/empleados/{id}/
        return Response(request.data)

    def destroy(self, request, pk=None):           # DELETE /api/v1/rrhh/empleados/{id}/
        return Response(status=status.HTTP_204_NO_CONTENT)
