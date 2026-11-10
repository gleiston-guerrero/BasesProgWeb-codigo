<?php
// app/Models/Proveedor.php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Proveedor extends Model {

	// $table fija el nombre de la tabla. Sin esta línea, Eloquent aplicaría la
	// pluralización inglesa y buscaría la tabla 'proveedors'.
	protected $table = 'proveedores';

	// $fillable lista los campos que pueden asignarse masivamente
	// (protección contra mass assignment attacks).
	// Sin $fillable, un atacante podría modificar campos no previstos
	// enviando campos adicionales en la petición.
	protected $fillable = ['ruc', 'razon_social', 'limite_credito'];

	// $casts define conversiones automáticas de tipos al leer/escribir.
	// 'decimal:2' devuelve limite_credito como CADENA con 2 decimales
	// ("50000.00"), no como float: así no se pierde precisión.
	protected $casts = [
	'limite_credito' => 'decimal:2',
	];
}
