<?php
// app/Http/Controllers/ProveedorController.php
namespace App\Http\Controllers;

use App\Models\Proveedor;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;

class ProveedorController extends Controller {

	// index() responde a GET /api/proveedores
	// Proveedor::all() es equivalente a findAll() en Spring Data JPA.
	public function index(): JsonResponse {
		return response()->json(Proveedor::all());
	}

	// show() responde a GET /api/proveedores/{proveedor}
	// Laravel inyecta automáticamente el modelo por ID (route model binding):
	// si no existe, devuelve 404 sin código adicional.
	public function show(Proveedor $proveedor): JsonResponse {
		return response()->json($proveedor);
	}

	// store() responde a POST /api/proveedores
	// $request->validate() detiene la ejecución si falla: responde 422 con los
	// errores en JSON (bootstrap/app.php lo exige para las rutas api/*; en otras
	// rutas, solo si la petición envía Accept: application/json, y si no, 302).
	// Las reglas de validación son: required (obligatorio), string (cadena),
	// size:13 (exactamente 13 caracteres), unique:proveedores,ruc (sin duplicados).
	public function store(Request $request): JsonResponse {
		$datos = $request->validate([
		'ruc'            => 'required|string|size:13|unique:proveedores,ruc',
		'razon_social'   => 'required|string|max:200',
		'limite_credito' => 'required|numeric|min:0.01',
		]);
		// Proveedor::create() inserta el registro y devuelve el modelo con el ID generado.
		$proveedor = Proveedor::create($datos);
		// 201 Created con la URI del nuevo recurso en la cabecera Location.
		return response()->json($proveedor, 201)
			->header('Location', route('proveedores.show', ['proveedor' => $proveedor]));
	}

	// update() responde a PUT /api/proveedores/{proveedor}
	public function update(Request $request, Proveedor $proveedor): JsonResponse {
		$datos = $request->validate([
		// El tercer argumento de unique ({$proveedor->id}) excluye el registro actual,
		// permitiendo que el propio proveedor mantenga su RUC.
		'ruc'            => "required|string|size:13|unique:proveedores,ruc,{$proveedor->id}",
		'razon_social'   => 'required|string|max:200',
		'limite_credito' => 'required|numeric|min:0.01',
		]);
		$proveedor->update($datos);
		return response()->json($proveedor);
	}

	// destroy() responde a DELETE /api/proveedores/{proveedor}
	public function destroy(Proveedor $proveedor): JsonResponse {
		$proveedor->delete();
		// 204 No Content: éxito sin cuerpo de respuesta (mismo que Spring Boot)
		return response()->json(null, 204);
	}
}
