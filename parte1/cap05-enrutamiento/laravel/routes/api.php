<?php
// routes/api.php (primera parte). Laravel antepone /api a estas rutas.
use Illuminate\Support\Facades\Route;

// GET simple
Route::get('/proveedores', function () {
	return response()->json([
	'total'   => 142,
	'activos' => 138,
	]);
});

// POST (recibe datos del cliente)
Route::post('/proveedores', function (\Illuminate\Http\Request $request) {
	// $request contiene el cuerpo JSON de la petición
	return response()->json(['creado' => true], 201);
});

// routes/api.php (segunda parte)
// Parámetro obligatorio
Route::get('/contratos/{id}', function (int $id) {
	return response()->json(['contrato_id' => $id]);
});

// Con restricción: solo acepta números
Route::get('/empleados/{id}', function (int $id) {
	return response()->json(['empleado_id' => $id]);
})->where('id', '[0-9]+');

// Parámetro opcional (valor por defecto si no se provee)
Route::get('/reportes/{anio?}', function (?int $anio = null) {
	// date('Y') devuelve una cadena: (int) mantiene el tipo numérico en el JSON
	return response()->json(['anio' => $anio ?? (int) date('Y')]);
});

// routes/api.php (tercera parte). PHP admite la declaración use en
// cualquier punto del nivel superior del archivo.
use Illuminate\Http\Request;

Route::get('/contratos', function (Request $request) {
	$estado = $request->query('estado', 'todos');       // valor por defecto
	$anio   = (int) $request->query('anio', date('Y'));  // sin valor: año en curso
	$pagina = (int) $request->query('pagina', 1);

	return response()->json(compact('estado', 'anio', 'pagina'));
});

Route::post('/contratos', function (Request $request) {
	// Validar y acceder al cuerpo JSON (422 con los errores en JSON si falla;
	// en las rutas api/* de un proyecto nuevo, aunque falte Accept: application/json)
	$datos = $request->validate([
	'numero'    => 'required|string|max:20',
	'monto'     => 'required|numeric|min:0.01',
	'fecha_fin' => 'required|date|after:today',
	]);
	return response()->json(['mensaje' => 'Contrato recibido'], 201);
});

// routes/api.php (cuarta parte)
use App\Http\Controllers\EmpleadoController;

// Todas las rutas del grupo tienen prefijo /api/v1/rrhh
// y requieren autenticación con Sanctum: sin token válido, 401 (la petición
// debe enviar Accept: application/json; sin ella, Laravel intenta redirigir
// a una ruta login que una API no define y responde 500)
Route::prefix('v1/rrhh')
->middleware('auth:sanctum')
->group(function () {
	Route::get('/empleados',          [EmpleadoController::class, 'index']);
	Route::post('/empleados',         [EmpleadoController::class, 'store']);
	Route::get('/empleados/{id}',     [EmpleadoController::class, 'show']);
	Route::put('/empleados/{id}',     [EmpleadoController::class, 'update']);
	Route::delete('/empleados/{id}',  [EmpleadoController::class, 'destroy']);
});
