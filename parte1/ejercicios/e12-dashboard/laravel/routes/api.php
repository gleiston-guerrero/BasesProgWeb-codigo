<?php
// routes/api.php
use App\Http\Controllers\ProveedorController;
use Illuminate\Support\Facades\Route;

// Route::apiResource() genera automáticamente las 5 rutas RESTful:
//   GET       /api/proveedores               -> index()
//   POST      /api/proveedores               -> store()
//   GET       /api/proveedores/{proveedor}   -> show()
//   PUT/PATCH /api/proveedores/{proveedor}   -> update()
//   DELETE    /api/proveedores/{proveedor}   -> destroy()
// Una línea reemplaza 5 rutas manuales (equivale al Router de DRF).
// parameters() renombra el parámetro: sin él, Laravel lo llamaría
// {proveedore} (singular inglés) y no enlazaría con Proveedor $proveedor.
Route::apiResource('proveedores', ProveedorController::class)
    ->parameters(['proveedores' => 'proveedor']);

// routes/api.php (se añade al final)
use App\Models\Contrato;
use App\Models\Proveedor;

Route::get('/dashboard', function () {
    return response()->json([
        'totalProveedores' => Proveedor::count(),
        'contratosActivos' => Contrato::whereDate('fecha_inicio', '<=', today())
                                      ->whereDate('fecha_vencimiento', '>=', today())
                                      ->count(),
        'fechaServidor'    => now()->toIso8601String(),
    ]);
});
