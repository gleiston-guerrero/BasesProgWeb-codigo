<?php
// database/migrations/2026_01_01_000000_create_proveedores_table.php
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

// En Laravel 13 se puede usar una clase anónima para las migraciones.
// Esto evita conflictos de nombres entre migraciones.
return new class extends Migration {

	// up() define la estructura de la tabla al aplicar la migración.
	public function up(): void {
		Schema::create('proveedores', function (Blueprint $table) {
			$table->id();          // BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY
			$table->string('ruc', 13)->unique();
			$table->string('razon_social', 200);
			$table->decimal('limite_credito', 12, 2);
			$table->timestamps();  // created_at y updated_at (DATETIME)
		});
	}

	// down() revierte la migración (elimina la tabla).
	// Se ejecuta con: php artisan migrate:rollback
	public function down(): void {
		Schema::dropIfExists('proveedores');
	}
};
