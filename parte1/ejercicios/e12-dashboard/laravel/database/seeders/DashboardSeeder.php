<?php
// database/seeders/DashboardSeeder.php -- E1.2: datos de ejemplo
// (dos proveedores; un contrato vigente y otro vencido)
namespace Database\Seeders;

use App\Models\Contrato;
use App\Models\Proveedor;
use Illuminate\Database\Seeder;

class DashboardSeeder extends Seeder
{
	public function run(): void
	{
		$p1 = Proveedor::create(['ruc' => '1790012345001',
			'razon_social' => 'Importadora XL', 'limite_credito' => '50000.00']);
		$p2 = Proveedor::create(['ruc' => '0990012345001',
			'razon_social' => 'Aceros del Pacífico', 'limite_credito' => '20000.00']);
		Contrato::create(['numero' => 'CT-2026-001', 'valor' => '12500.50',
			'proveedor_id' => $p1->id,
			'fecha_inicio' => '2026-01-01', 'fecha_vencimiento' => '2027-12-31']);
		Contrato::create(['numero' => 'CT-2025-009', 'valor' => '3000.00',
			'proveedor_id' => $p2->id,
			'fecha_inicio' => '2025-01-01', 'fecha_vencimiento' => '2025-12-31']);
	}
}
