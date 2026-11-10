# Archivo: hola_mundo.py
# Ejecutar: python3.15 hola_mundo.py   (en Windows: python hola_mundo.py)

import sys  # Módulo sys: información sobre el intérprete de Python

# print() es la función estándar de salida en Python.
# A diferencia de Java, no es un método de un objeto System:
# es una función global disponible sin importar nada.
print("¡Hola Mundo desde Python!")

# sys.version contiene la cadena completa de versión del intérprete;
# split()[0] se queda solo con el número de versión (por ejemplo, 3.15.2).
print(f"Intérprete: Python {sys.version.split()[0]}")

# La convención if __name__ == "__main__" distingue ejecución directa
# de importación como módulo. Si este archivo es importado por otro,
# este bloque NO se ejecuta.
if __name__ == "__main__":
    print("Ejecutado directamente (no importado como módulo).")
