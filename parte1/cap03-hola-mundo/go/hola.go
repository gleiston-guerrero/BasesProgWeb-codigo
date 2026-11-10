// hola.go — Go 1.27
// Ejecutar: go run hola.go
package main

import (
	"fmt"
	"runtime"
)

func main() {
	fmt.Println("¡Hola Mundo desde Go!")
	fmt.Printf("Versión Go: %s\n", runtime.Version())
}
