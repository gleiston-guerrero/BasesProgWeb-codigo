// hola_web.mts — Servidor HTTP mínimo (Node.js + TypeScript, módulo ES)
// Ejecutar: node hola_web.mts  → abrir http://localhost:3000
import { createServer } from "node:http";

const servidor = createServer((req, res) => {
    res.writeHead(200, { "Content-Type": "text/plain; charset=utf-8" });
    res.end("¡Hola Mundo desde un servidor Node.js!");
});

servidor.listen(3000, () => console.log("Escuchando en http://localhost:3000"));
