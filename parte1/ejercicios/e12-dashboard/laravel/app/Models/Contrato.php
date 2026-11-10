<?php
// app/Models/Contrato.php -- E1.2
namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Contrato extends Model {
	// 'contratos' es también el plural inglés de Contrato, pero se declara
	// para mantener la misma convención que Proveedor
	protected $table = 'contratos';

	protected $fillable = ['numero', 'valor', 'proveedor_id',
		'fecha_inicio', 'fecha_vencimiento'];

	protected $casts = [
		'valor' => 'decimal:2',
		'fecha_inicio' => 'date',
		'fecha_vencimiento' => 'date',
	];
}
