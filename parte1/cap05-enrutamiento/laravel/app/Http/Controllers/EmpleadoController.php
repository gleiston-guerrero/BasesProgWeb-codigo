<?php
// app/Http/Controllers/EmpleadoController.php
// Creado con: php artisan make:controller EmpleadoController --api
// Respuestas simuladas, sin base de datos (Capítulo 5, grupos de rutas).
namespace App\Http\Controllers;

use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class EmpleadoController extends Controller
{
    public function index(): JsonResponse
    {
        return response()->json([]);
    }

    public function store(Request $request): JsonResponse
    {
        return response()->json($request->all(), 201);
    }

    public function show(int $id): JsonResponse
    {
        return response()->json(['id' => $id]);
    }

    public function update(Request $request, int $id): JsonResponse
    {
        return response()->json($request->all());
    }

    public function destroy(int $id): JsonResponse
    {
        return response()->json(null, 204);
    }
}
