# Capítulo 9 — Formulario Thymeleaf con validación y Post/Redirect/Get

Proyecto de ejemplo del libro *Bases de la Programación Web: Desarrollo Back-End* (Capítulo 9,
Sección «Formularios e integración con validación»). El libro muestra los dos métodos de
`SolicitudController` y el contenido de `<body>` de la plantilla; aquí está el proyecto completo:

| Archivo | Papel |
|---|---|
| `src/main/java/com/comercializadora/ejemplos/formulario/SolicitudForm.java` | Objeto de respaldo del formulario, con `@Pattern` (RUC de 13 dígitos) y `@NotBlank` (razón social) |
| `src/main/java/com/comercializadora/ejemplos/formulario/SolicitudController.java` | `GET /solicitudes/nueva` (formulario) y `POST /solicitudes` (validación y redirección) |
| `src/main/resources/templates/solicitud-form.html` | Plantilla con `th:object`, `th:field` y `th:errors` |
| `src/test/java/com/comercializadora/ejemplos/formulario/SolicitudControllerTest.java` | Tres pruebas con `MockMvc`: formulario vacío, datos inválidos (200 con mensajes) y válidos (302 con atributo *flash*) |

Dependencias (Spring Initializr, Spring Boot 4.1.2, Java 25): *Spring Web*, *Thymeleaf* y *Validation*.

## Requisitos

JDK 25 (LTS). Maven no hace falta: se incluye el *Maven Wrapper*.

## Compilar, probar y ejecutar

Linux / macOS, desde esta carpeta:

```bash
./mvnw -B verify            # 3 pruebas
./mvnw spring-boot:run      # abrir http://localhost:8080/solicitudes/nueva en el navegador
```

Windows (PowerShell):

```powershell
.\mvnw.cmd -B verify
.\mvnw.cmd spring-boot:run
```

## Probar con curl (segunda terminal)

```bash
curl -s -X POST localhost:8080/solicitudes -d "ruc=123" -d "razonSocial=" | grep -E "dígitos|obligatoria"
curl -i -X POST localhost:8080/solicitudes -d "ruc=0990000001001" -d "razonSocial=Aceros"   # 302
```

La segunda responde `302` con `Location: http://localhost:8080/solicitudes/nueva;jsessionid=...`: el
atributo *flash* se guarda en una sesión que acaba de crearse y, como el cliente aún no ha devuelto la cookie
de sesión, Tomcat añade su identificador a la URL de redirección. En Windows, la primera orden se ejecuta en Git Bash o WSL (usa `grep`); la segunda, con `curl.exe` en PowerShell.
