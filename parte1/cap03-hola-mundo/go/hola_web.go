// hola_web.go — Servidor HTTP mínimo (Go, paquete net/http)
// Ejecutar: go run hola_web.go  → abrir http://localhost:3000
package main

import (
	"fmt"
	"net/http"
)

func main() {
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintln(w, "¡Hola Mundo desde un servidor Go!")
	})
	http.ListenAndServe(":3000", nil)
}
