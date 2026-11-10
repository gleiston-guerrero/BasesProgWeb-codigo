# Capítulo 3 — Primeros programas en Java, Python, PHP, TypeScript y Go

Programas «¡Hola Mundo!» del Capítulo 3 del libro *Bases de la Programación Web: Desarrollo Back-End*.
Cada archivo es **completo** e idéntico al listado del libro que se indica.

| Carpeta / archivo | Listado del libro | Requisito |
| --- | --- | --- |
| `java/HolaMundo.java` | ¡Hola Mundo! en Java 25 (forma clásica) | JDK 25 (también JDK 21) |
| `java/HolaMundo25.java` | ¡Hola Mundo! con clase implícita (JEP 512) | JDK 25 |
| `python/hola_mundo.py` | ¡Hola Mundo! en Python | Python 3.15 (sirve cualquier 3.x) |
| `php/hola_mundo.php` | ¡Hola Mundo! en PHP | PHP 8.5 |
| `typescript/hola.ts` | ¡Hola Mundo! en TypeScript/Node.js | Node.js 24.12 o superior |
| `typescript/hola_web.mts` | Servidor web mínimo en TypeScript/Node.js | Node.js 24.12 o superior |
| `go/hola.go` | ¡Hola Mundo! en Go | Go 1.27 (sirve cualquier Go 1.x reciente) |
| `go/hola_web.go` | Servidor web mínimo en Go | Go 1.27 |

La instalación de cada lenguaje se describe en el Capítulo 3 (listados de instalación de Java, Python y PHP).

## Ejecutar

Todas las órdenes se lanzan desde la subcarpeta indicada. Son iguales en Windows (PowerShell), Linux y
macOS, salvo donde se indica.

```bash
# Java (carpeta java/)
javac HolaMundo.java        # genera HolaMundo.class
java HolaMundo
java HolaMundo25.java       # se ejecuta sin compilar antes

# Python (carpeta python/)
python3.15 hola_mundo.py    # Windows: python hola_mundo.py

# PHP (carpeta php/)
php hola_mundo.php

# TypeScript (carpeta typescript/)
node hola.ts
node hola_web.mts           # y abrir http://localhost:3000 (Ctrl+C para detener)

# Go (carpeta go/): cada archivo es un programa independiente, se ejecuta por nombre
go run hola.go
go run hola_web.go          # y abrir http://localhost:3000 (Ctrl+C para detener)
```

## Probar los servidores web con curl

```bash
curl http://localhost:3000   # Windows PowerShell: curl.exe http://localhost:3000
```

## Salidas esperadas

```text
$ java HolaMundo
¡Hola Mundo desde Java 25 LTS!
JVM: 25.0.x+...             (la versión exacta depende del JDK instalado)

$ java HolaMundo25.java
¡Hola Mundo con clase implícita de Java 25!
Sin public class, sin static: el lenguaje se simplifica.

$ python3.15 hola_mundo.py
¡Hola Mundo desde Python!
Intérprete: Python 3.15.x   (la versión exacta instalada)
Ejecutado directamente (no importado como módulo).

$ php hola_mundo.php
¡Hola Mundo desde PHP!
Este código se ejecutó sin compilación previa.
```

Si la consola muestra `?` en lugar de `¡` o de las vocales acentuadas, la terminal no usa UTF-8
(en Linux, compruébese que `locale` muestra un valor `*.UTF-8`; en Windows, úsese Windows Terminal).
