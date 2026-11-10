<?php
// Archivo: hola_mundo.php
// Ejecutar: php hola_mundo.php

// echo imprime en la salida estándar (consola o navegador según el contexto).
// PHP_EOL es la constante de salto de línea del sistema operativo actual
// (\n en Linux/Mac, \r\n en Windows).
echo "¡Hola Mundo desde PHP!" . PHP_EOL;

// Las variables en PHP siempre comienzan con $ (dólar).
// El tipado es dinámico: PHP infiere el tipo en tiempo de ejecución.
// El operador . concatena cadenas en PHP (equivale a + en Java o en Python).
$mensaje = "Este código se ejecutó sin compilación previa.";
echo $mensaje . PHP_EOL;
