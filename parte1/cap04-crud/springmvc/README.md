# Capítulo 4 — CRUD de proveedores con Spring MVC clásico (Spring Framework 7.0.10, sin Spring Boot)

Proyecto del apartado «Spring MVC clásico — CRUD de proveedores» (Capítulo 4). Los paquetes `model`, `repository` y `service` son idénticos a los del proyecto `../springboot`; `config/` y `controller/` contienen los Listados WebAppInitializer, AppConfig, WebConfig y ProveedorController. El `pom.xml` (que el libro no reproduce) está aquí completo y comentado.

## Requisitos

- JDK 25.
- Maven Wrapper incluido (descarga Maven 3.10.0).
- Para desplegar el WAR: un contenedor Jakarta Servlet 6.1 (Tomcat 11.0 o Jetty 12.1). Para probar sin instalar nada se usa el plugin de Jetty incluido en el `pom.xml`.

## Ejecutar

Linux / macOS:

```bash
./mvnw verify        # compila y genera target/proveedores-mvc.war
./mvnw jetty:run     # despliega en Jetty 12.1 en http://localhost:8080/
```

Windows (PowerShell):

```powershell
.\mvnw.cmd verify
.\mvnw.cmd jetty:run
```

Otro puerto: `./mvnw jetty:run -Djetty.http.port=8081`.

Despliegue en Tomcat 11: copiar `target/proveedores-mvc.war` a `webapps/` de Tomcat; la API queda en `http://localhost:8080/proveedores-mvc/api/proveedores` (el nombre del WAR es la ruta de contexto; para usar `/`, renombrarlo a `ROOT.war`).

## Probar

Las mismas órdenes curl que en `../springboot/README.md` dan los mismos códigos (201, 200, 409, 400, 200, 204, 404). Diferencia: el cuerpo de los errores 400 es la página HTML de error de Jetty, no el JSON de Spring Boot.

## Nota

El `pom.xml` activa `<parameters>true</parameters>` en `maven-compiler-plugin`. Sin esa opción, las rutas con `{id}` fallan con un 500 («Name for argument of type [java.lang.Long] not specified ... Ensure that the compiler uses the '-parameters' flag»). El parent de Spring Boot la activa automáticamente.
