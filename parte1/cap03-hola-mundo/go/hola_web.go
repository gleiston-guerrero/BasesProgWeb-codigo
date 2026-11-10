// hola_web.go — Servidor HTTP mínimo (Go, paquete net/http)
// Ejecutar: go run hola_web.go  → abrir http://localhost:3000
package main

import (
	"fmt"
	"log"
	"net/http"
)

func main() {
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintln(w, "¡Hola Mundo desde un servidor Go!")
	})
	// Solo regresa si falla (p. ej., puerto ocupado): log.Fatal muestra el error
	log.Fatal(http.ListenAndServe(":3000", nil))
}
