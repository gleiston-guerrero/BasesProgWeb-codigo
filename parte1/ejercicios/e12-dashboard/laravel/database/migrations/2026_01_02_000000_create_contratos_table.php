<?php
// database/migrations/2026_01_02_000000_create_contratos_table.php
// E1.2: tabla de contratos con su vigencia (fecha_inicio, fecha_vencimiento)
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
	public function up(): void {
		Schema::create('contratos', function (Blueprint $table) {
			$table->id();
			$table->string('numero', 20)->unique();
			$table->decimal('valor', 12, 2);
			$table->foreignId('proveedor_id')->constrained('proveedores');
			$table->date('fecha_inicio');
			$table->date('fecha_vencimiento');
			$table->timestamps();
		});
	}

	public function down(): void {
		Schema::dropIfExists('contratos');
	}
};
