# SGPC — estado final del libro (Parte IV y Apéndice A)

Proyecto integrador **Sistema de Gestión de Proveedores y Contratos (SGPC)** tal como queda al
terminar el libro *Bases de la Programación Web: Desarrollo Back-End*: el proyecto de
`../../parte3/sgpc` más los capítulos 14 a 16 y el Apéndice A.

| Capítulo / práctica | Qué añade |
|---|---|
| Cap. 14 y Práctica 13 | Caché (`CacheConfig`, `@Cacheable`/`@CacheEvict` en `ProveedorService`), Caffeine |
| Cap. 15 y Práctica 14 | `Categoria` y `@ManyToMany` en `Contrato`; `JOIN FETCH`; `InformeService`, `InformeController` (`/api/informes/cartera` y `/conteo`) |
| Cap. 16 y Práctica 15 | `application-prod.properties`, Flyway (`db/migration/V1__esquema_inicial.sql`), Actuator y `JwtClaveHealthIndicator`, `compose.yaml`, `.env.example` |
| Apéndice A | Pruebas de `src/test` y perfil `src/test/resources/application-test.properties`; `static/cliente.html` |

Las soluciones de los ejercicios E4.1 a E4.4 están en `../ejercicios`.

## Requisitos (versiones comprobadas el 10-11-2026)

- JDK 25 (LTS). Maven no hace falta: se usa el *wrapper* incluido (`mvnw`, Maven 3.10.0).
- Spring Boot 4.1.2 (gestiona Hibernate 7.4, Flyway 12.4, Caffeine 3.2, driver PostgreSQL 42.7).
- Para el despliegue: Docker Engine con Docker Compose v2 (probado con Docker 29.9 y Compose 5.7)
  e imagen `postgres:18`.

## Desarrollo (H2 en memoria)

La clave del JWT se toma de la variable de entorno `SGPC_JWT_SECRET` (al menos 32 bytes).

Linux / macOS (Bash), en esta carpeta:

```bash
export SGPC_JWT_SECRET="$(openssl rand -base64 32)"
./mvnw verify            # 16 pruebas: Tests run: 16, Failures: 0 ... BUILD SUCCESS
./mvnw spring-boot:run
```

Windows (PowerShell), en esta carpeta:

```powershell
$b = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b)
$env:SGPC_JWT_SECRET = [Convert]::ToBase64String($b)
.\mvnw.cmd verify
.\mvnw.cmd spring-boot:run
```

Usuarios de desarrollo (los crea `UsuariosIniciales`, nunca con el perfil `prod`):
`admin` / `admin-clave-dev` (ADMIN) y `gestor` / `gestor-clave-dev` (GESTOR).

Prueba rápida (Bash; en PowerShell, `curl.exe` y el JSON desde un archivo):

```bash
H='Content-Type: application/json'
TOKEN=$(curl -s -X POST localhost:8080/auth/login -H "$H" \
  -d '{"username":"gestor","password":"gestor-clave-dev"}' | sed 's/.*"token":"\([^"]*\)".*/\1/')
curl -i -X POST localhost:8080/api/proveedores -H "Authorization: Bearer $TOKEN" -H "$H" \
  -d '{"ruc":"0990000001001","razonSocial":"Aceros Litoral S.A.","limiteCredito":15000}'   # 201
curl -s localhost:8080/api/informes/cartera/conteo -H "Authorization: Bearer $TOKEN"     # 200
curl -s localhost:8080/actuator/health   # {"groups":["liveness","readiness"],"status":"UP"}
```

Para la Práctica 13 se quita el `#` de la última línea de `application.properties`
(`spring.cache.caffeine.spec=maximumSize=100,expireAfterWrite=30s`).

## Despliegue con Docker Compose (Práctica 15)

```bash
cp .env.example .env          # PowerShell: Copy-Item .env.example .env  — y editar los valores
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=sgpc:1.0
docker compose up -d
curl -i localhost:8080/actuator/health     # 503 mientras arranca; luego 200 UP
```

Alta del primer administrador (la tabla `usuarios` está vacía en producción):

```bash
htpasswd -n -B -C 10 admin            # o: docker run --rm -it httpd:2.4 htpasswd -n -B -C 10 admin
docker compose exec db psql -U sgpc_app -d sgpc
# INSERT INTO usuarios (username, password, rol) VALUES ('admin', '<resumen sin "admin:">', 'ADMIN');
```

Después, `POST /auth/login` con `admin` devuelve un token y `/actuator/health` con ese token
muestra `db` y `jwtClave` en `UP`. `docker compose down` + `docker compose up -d` conserva los
datos (volumen `datos_sgpc`); `docker compose down -v` los borra.

## Resultados comprobados (10-11-2026)

- `./mvnw verify` con JDK 25.0.4: 16 pruebas, 0 fallos (Flyway aplica `V1` sobre H2 también en `@DataJpaTest`).
- `spring-boot:build-image`: imagen `sgpc:1.0` con BellSoft Liberica JRE 25.0.5 (Paketo `builder-noble-java-tiny`).
- Compose con `postgres:18`: Flyway migra el esquema `public` a la versión 1, Hibernate lo valida,
  login del administrador 200, salud detallada `UP`, datos persistentes tras `down`/`up`.
- Prácticas 13 y 14: recuentos de sentencias 1/2/1/1 y 7 → 1, como indica el libro.
