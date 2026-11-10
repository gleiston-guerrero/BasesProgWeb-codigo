# Capítulo 7 — Beans, ciclo de vida, inyección y estereotipos (ejemplos fuera del SGPC)

Proyecto de ejemplo del Capítulo 7 de *Bases de la Programación Web: Desarrollo Back-End*
(Parte II). No forma parte del proyecto integrador SGPC: ilustra los conceptos de las
secciones 7.1 (contenedor y ciclo de vida de los beans) y 7.2 (estereotipos).

| Archivo | Listado del libro | Sección |
|---|---|---|
| `beans/ValidadorRuc.java`, `beans/RegistroProveedores.java` | Dos beans y su dependencia | 7.1.1 |
| `ComponentesApplication.java` | Consulta del contenedor | 7.1.1 |
| `ciclo/BeanObservado.java`, `ciclo/TrazadorPostProcessor.java` | Ciclo de vida observado | 7.1.2 |
| `web/EstadoV1Controller.java`, `web/EstadoV2Controller.java` | `@Controller` frente a `@RestController` | 7.2.4 |
| `config/RelojConfig.java`, `config/CalculadoraVencimientos.java` | Un bean declarado con `@Bean` | 7.2.5 |

Pruebas (`src/test/java`): `ComponentesApplicationTests` (los beans son *singletons*),
`EstadoControllersTest` (las dos versiones del controlador devuelven el mismo JSON, con `MockMvc`)
y `CalculadoraVencimientosTest` (reloj fijo con `Clock.fixed`).

Generado con Spring Initializr: Maven, Java 25, Spring Boot 4.1.2, *Group* `com.comercializadora`,
*Artifact* `componentes`, dependencia *Spring Web*. El `application.properties` desactiva Tomcat
(`spring.main.web-application-type=none`) y deja en la consola solo los mensajes del ejemplo.

## Requisitos

- JDK 25 (`java -version` debe mostrar 25.0.x).
- No hace falta instalar Maven: se incluye el Maven Wrapper (`mvnw`, `mvnw.cmd`).

## Ejecutar

Desde esta carpeta:

```bash
# Linux / macOS
./mvnw -q spring-boot:run
```

```powershell
# Windows (PowerShell)
.\mvnw.cmd -q spring-boot:run
```

Salida esperada (se omiten las dos líneas iniciales de versión y perfil; los tiempos de arranque varían):

```
1. Instanciación: constructor
2. Inyección de dependencias: setValidador
3. Interfaz Aware: setBeanName("beanObservado")
4. BeanPostProcessor: antes de la inicialización
5. Inicialización: método @PostConstruct
6. Inicialización: afterPropertiesSet()
7. BeanPostProcessor: después de la inicialización
Started ComponentesApplication in 1.515 seconds (process running for 2.175)
¿Misma instancia? true
Nombre del bean: [registroProveedores]
RUC aceptado: 0990000001001
RUC rechazado: 099-123
8. En uso: el bean atiende llamadas
9. Destrucción: método @PreDestroy
```

## Probar

```bash
./mvnw -B verify        # Linux / macOS
.\mvnw.cmd -B verify    # Windows (PowerShell)
```

Las tres clases de prueba deben terminar sin fallos (`Tests run: 3, Failures: 0, Errors: 0`).
Este proyecto no expone una API en un puerto, por lo que no se prueba con curl.

## Experimento de la Sección 7.2.1

Para ver el diagnóstico de Spring Boot cuando falta un bean, mueva `ValidadorRuc.java` a un paquete
`com.comercializadora.otro` (fuera de `com.comercializadora.componentes`), ajuste su línea `package`
y los `import` de `RegistroProveedores` y `BeanObservado`, y ejecute de nuevo: la aplicación se
detiene con `APPLICATION FAILED TO START` y el mensaje
`Parameter 0 of constructor in ...RegistroProveedores required a bean of type
'com.comercializadora.otro.ValidadorRuc' that could not be found.`
