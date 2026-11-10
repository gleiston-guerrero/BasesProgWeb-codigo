#!/usr/bin/env bash
# E1.5 — Secuencia de idempotencia contra el CRUD de Spring Boot del Capítulo 4
# (parte1/cap04-crud/springboot en ejecución, recién arrancado: base H2 vacía).
# Se ejecuta desde esta carpeta: ./secuencia.sh
api=http://localhost:8080/api/proveedores
J="Content-Type: application/json"
codigo() { curl -s -o /dev/null -w "%{http_code}\n" "$@"; }

echo "1. POST:";          curl -s -i -X POST $api -H "$J" -d @proveedor.json | grep -E '^(HTTP|Location)'
echo "2. POST repetido:"; codigo -X POST $api -H "$J" -d @proveedor.json
echo "3. PUT:";           codigo -X PUT $api/1 -H "$J" -d @proveedor-put.json
echo "4. PUT repetido:";  codigo -X PUT $api/1 -H "$J" -d @proveedor-put.json
echo "5. GET colección:"; curl -s $api; echo
echo "6. DELETE:";        codigo -X DELETE $api/1
echo "7. DELETE repetido:"; codigo -X DELETE $api/1
echo "8. GET colección:"; curl -s $api; echo
