// Archivo: HolaMundo.java
// Compilar: javac HolaMundo.java
// Ejecutar: java HolaMundo

// En Java, todo el código ejecutable vive dentro de una clase.
// El nombre de la clase DEBE coincidir exactamente con el nombre del archivo.
public class HolaMundo {

    // main() es el punto de entrada de cualquier programa Java.
    // En esta forma clásica, la JVM exige que main sea public y static:
    // public: accesible desde cualquier lugar (la JVM debe poder llamarlo).
    // static: pertenece a la clase, no a una instancia (la JVM lo llama sin crear objeto).
    // void: no devuelve ningún valor.
    // String[] args: array de cadenas con los argumentos de línea de comandos.
    public static void main(String[] args) {

        // System.out.println() imprime en la consola estándar con salto de línea.
        // System: clase que representa el sistema en ejecución.
        // out: flujo de salida estándar (stdout).
        // println: "print line" = imprimir y avanzar a la siguiente línea.
        System.out.println("¡Hola Mundo desde Java 25 LTS!");

        // Runtime.version() devuelve la versión de la JVM actual.
        System.out.println("JVM: " + Runtime.version());
    }
}
