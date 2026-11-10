package com.empresa.proveedores.repository;

import com.empresa.proveedores.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// @Repository marca esta interfaz como componente de acceso a datos.
// JpaRepository<Proveedor, Long>:
//   - Primer tipo genérico <T>: la entidad que gestiona (Proveedor).
//   - Segundo tipo genérico <ID>: el tipo de la clave primaria (Long).
// Spring Data JPA genera automáticamente la implementación en tiempo de ejecución:
// los métodos CRUD (save, findById, findAll, delete) están disponibles sin código adicional.
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

	// Los métodos cuyo nombre sigue la convención "findBy + NombreCampo"
	// son generados automáticamente por Spring Data (query derivation).
	// Esto produce: SELECT * FROM proveedores WHERE ruc = ?
	Optional<Proveedor> findByRuc(String ruc);
}
