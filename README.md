# Bases de la Programación Web: Desarrollo Back-End — código del libro

Repositorio compañero del libro *Bases de la Programación Web: Desarrollo Back-End de Aplicaciones Web*, de Gleiston Cicerón Guerrero Ulloa (Universidad Técnica Estatal de Quevedo). Contiene el código completo de todos los ejemplos, prácticas y soluciones, construido y probado con las versiones vigentes en noviembre de 2026.

Cada listado del libro indica el archivo y la carpeta de este repositorio a la que pertenece. Si el libro muestra un fragmento, el archivo completo está aquí.

## Versiones de referencia (noviembre de 2026)

| Tecnología | Versión |
|---|---|
| Java | 25 LTS |
| Spring Boot | 4.1.2 (Spring Framework 7.0, Spring Security 7.1) |
| Python | 3.15 |
| Django / DRF | 6.1.3 / 3.18.3 |
| FastAPI | 0.162.2 |
| PHP | 8.5 |
| Laravel | 13.40.0 |
| Symfony | 8.1.11 |
| PostgreSQL | 18 |

## Organización

| Carpeta | Contenido |
|---|---|
| `parte1/cap02-https` | HTTPS en Spring Boot (Sección 2.4.1) |
| `parte1/cap03-hola-mundo` | ¡Hola Mundo! en Java, Python, PHP, TypeScript y Go |
| `parte1/cap04-crud` | El mismo CRUD en Spring Boot, Spring MVC, Django, FastAPI, Laravel y Symfony; Práctica 4 |
| `parte1/cap05-enrutamiento` | Enrutamiento en Spring Boot, Django, FastAPI y Laravel; Práctica 5 |
| `parte1/ejercicios` | Soluciones de los ejercicios de la Parte I |
| `parte2/sgpc`, `parte2/ejercicios`, `parte2/ejemplos` | SGPC al final de la Parte II (con E2.1–E2.4), soluciones de E2.5 y E2.6 y ejemplos independientes de los capítulos 8 y 9 |
| `parte3/sgpc`, `parte3/ejercicios` | SGPC al final de la Parte III y soluciones de sus ejercicios |
| `parte4/sgpc`, `parte4/ejercicios` | SGPC final (Flyway, perfiles, Docker Compose, pruebas) y soluciones |

Cada carpeta tiene su propio `README.md` con los requisitos y las órdenes para Windows (PowerShell) y para Linux/macOS.

## Uso rápido

```bash
git clone https://github.com/gleiston-guerrero/BasesProgWeb-codigo.git
cd BasesProgWeb-codigo/parte4/sgpc
./mvnw verify          # en Windows: .\mvnw.cmd verify
```

Los proyectos del SGPC de las Partes III y IV necesitan la variable de entorno `SGPC_JWT_SECRET` (véase el README de cada carpeta).
