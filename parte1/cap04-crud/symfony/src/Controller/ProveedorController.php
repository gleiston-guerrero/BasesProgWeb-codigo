<?php
// src/Controller/ProveedorController.php
namespace App\Controller;

use App\Entity\Proveedor;
use App\Repository\ProveedorRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\{JsonResponse, Request, Response};
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Validator\Validator\ValidatorInterface;

// #[Route('/api/proveedores')] en la clase establece el prefijo de URI
// para todos los métodos del controlador.
#[Route('/api/proveedores', name: 'proveedor_')]
class ProveedorController extends AbstractController {

    // Doctrine EntityManager: equivale a EntityManager de JPA en Spring.
    // Se inyecta automáticamente en el constructor (inyección de dependencias de Symfony).
    public function __construct(
        private readonly EntityManagerInterface $em,
        private readonly ProveedorRepository $repo,
        private readonly ValidatorInterface $validator
    ) {}

    // GET /api/proveedores
    #[Route('', name: 'index', methods: ['GET'])]
    public function index(): JsonResponse {
        $proveedores = $this->repo->findAll();
        return $this->json($proveedores);
    }

    // GET /api/proveedores/{id}
    #[Route('/{id}', name: 'show', methods: ['GET'])]
    public function show(Proveedor $proveedor): JsonResponse {
        // El EntityValueResolver de Doctrine busca el Proveedor por el {id} de la
        // ruta y lo inyecta como argumento; si no existe, responde 404.
        // Desde Symfony 6.2 este mecanismo es nativo y sustituye al antiguo
        // @ParamConverter de SensioFrameworkExtraBundle.
        return $this->json($proveedor);
    }

    // POST /api/proveedores
    #[Route('', name: 'create', methods: ['POST'])]
    public function create(Request $request): JsonResponse {
        $proveedor = new Proveedor();
        // json_decode(..., true) devuelve un array asociativo (no un objeto
        // stdClass); ?? [] cubre un cuerpo vacío o que no sea JSON.
        $this->aplicarDatos($proveedor, json_decode($request->getContent(), true) ?? []);

        if ($error = $this->comprobar($proveedor)) {
            return $error;              // 422 (datos inválidos) o 409 (RUC duplicado)
        }
        $this->em->persist($proveedor); // Marcar para inserción
        $this->em->flush();             // Ejecutar el INSERT

        // 201 Created con la URI del nuevo recurso en la cabecera Location
        return $this->json($proveedor, Response::HTTP_CREATED, [
            'Location' => $this->generateUrl('proveedor_show', ['id' => $proveedor->getId()]),
        ]);
    }

    // PUT /api/proveedores/{id}: reemplazo completo, con las mismas reglas que POST
    #[Route('/{id}', name: 'update', methods: ['PUT'])]
    public function update(Request $request, Proveedor $proveedor): JsonResponse {
        $this->aplicarDatos($proveedor, json_decode($request->getContent(), true) ?? []);
        if ($error = $this->comprobar($proveedor)) {
            return $error;
        }
        $this->em->flush();             // Ejecutar el UPDATE
        return $this->json($proveedor);
    }

    // Copia TODOS los campos: PUT reemplaza el recurso completo, de modo que un
    // campo ausente no conserva el valor anterior y la validación lo rechaza.
    private function aplicarDatos(Proveedor $proveedor, array $datos): void {
        $proveedor->setRuc((string)($datos['ruc'] ?? ''));
        $proveedor->setRazonSocial((string)($datos['razon_social'] ?? ''));
        // Si el JSON trae el importe como número (50000.10), json_decode ya lo
        // convirtió en float y (string) no recupera el texto ("50000.1"). Por
        // eso la API espera el importe como cadena ("50000.10"), que llega
        // intacto; #[Assert\Type('numeric')] rechaza lo que no sea un número.
        $proveedor->setLimiteCredito((string)($datos['limite_credito'] ?? '0'));
    }

    // Devuelve la respuesta de error, o null si el proveedor puede guardarse.
    private function comprobar(Proveedor $proveedor): ?JsonResponse {
        // validate() ejecuta las restricciones #[Assert\...] de la entidad.
        $errores = $this->validator->validate($proveedor);
        if (count($errores) > 0) {
            $mensajes = [];
            foreach ($errores as $error) {
                $mensajes[$error->getPropertyPath()] = $error->getMessage();
            }
            return $this->json(['errors' => $mensajes], Response::HTTP_UNPROCESSABLE_ENTITY);
        }
        // Regla de negocio: el RUC no puede pertenecer a OTRO proveedor (409).
        $otro = $this->repo->findOneBy(['ruc' => $proveedor->getRuc()]);
        if ($otro !== null && $otro->getId() !== $proveedor->getId()) {
            return $this->json(['error' => 'Ya existe un proveedor con ese RUC'],
                Response::HTTP_CONFLICT);
        }
        return null;
    }

    // DELETE /api/proveedores/{id}
    #[Route('/{id}', name: 'delete', methods: ['DELETE'])]
    public function delete(Proveedor $proveedor): JsonResponse {
        $this->em->remove($proveedor);
        $this->em->flush();
        return $this->json(null, Response::HTTP_NO_CONTENT);
    }
}
