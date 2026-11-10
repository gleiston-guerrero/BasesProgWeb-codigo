<?php
// src/Entity/Proveedor.php
namespace App\Entity;

use App\Repository\ProveedorRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

// Los atributos PHP 8 (#[...]) reemplazan las anotaciones de bloque de comentario
// de versiones anteriores de Doctrine. Son nativos de PHP y tienen soporte del IDE.
// #[ORM\Entity] equivale a @Entity de Jakarta Persistence en Java.
#[ORM\Entity(repositoryClass: ProveedorRepository::class)]
#[ORM\Table(name: 'proveedores')]
class Proveedor {

	// #[ORM\Id] + #[ORM\GeneratedValue] equivale a @Id + @GeneratedValue en JPA.
	#[ORM\Id]
	#[ORM\GeneratedValue]
	#[ORM\Column]
	private ?int $id = null;

	// #[Assert\NotBlank]: validación Symfony — el campo no puede estar vacío.
	// #[Assert\Length]: restricción de longitud, equivale a @Size en Jakarta.
	#[ORM\Column(length: 13, unique: true)]
	#[Assert\NotBlank(message: "El RUC es obligatorio")]
	#[Assert\Length(exactly: 13, exactMessage: "El RUC debe tener exactamente 13 dígitos")]
	private string $ruc;

	#[ORM\Column(length: 200)]
	#[Assert\NotBlank]
	private string $razonSocial;

	// Doctrine devuelve las columnas decimal como string, no como float: así no
	// se pierde precisión (la coma flotante no es exacta para el dinero).
	#[ORM\Column(type: 'decimal', precision: 12, scale: 2)]
	#[Assert\Type(type: 'numeric', message: "El límite de crédito debe ser numérico")]
	#[Assert\Positive(message: "El límite de crédito debe ser mayor que cero")]
	private string $limiteCredito;

	// Getters y setters (generados por make:entity)
	public function getId(): ?int { return $this->id; }
	public function getRuc(): string { return $this->ruc; }
	public function setRuc(string $ruc): static { $this->ruc = $ruc; return $this; }
	public function getRazonSocial(): string { return $this->razonSocial; }
	public function setRazonSocial(string $r): static { $this->razonSocial = $r; return $this; }
	public function getLimiteCredito(): string { return $this->limiteCredito; }
	public function setLimiteCredito(string $l): static { $this->limiteCredito = $l; return $this; }
}
