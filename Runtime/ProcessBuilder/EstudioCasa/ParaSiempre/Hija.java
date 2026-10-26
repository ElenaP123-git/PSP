package ProcessBuilder.EstudioCasa.ParaSiempre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Hija {
    public static void main(String[] args) {
        // 1. Control de argumentos obligatorios
        if (args.length < 1) {
            System.err.println("Faltan argumentos");
            System.exit(1); // Código de error 1
        }

        String fichero = args[0];
        // String extra = args[1]; (si necesitas más argumentos)

        // 2. Leer el fichero de disco con BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea = br.readLine();
            int resultado = 0; // o tu lógica de cálculo

            while (linea != null) {
                // ... Aquí haces tus cálculos línea a línea ...
                linea = br.readLine();
            }

            // 3. ENVIAR EL RESULTADO AL PADRE (OBLIGATORIO)
            // Se imprime limpio por consola. El padre lo interceptará.
            System.out.println(resultado);

        } catch (IOException e) {
            System.err.println("Error de lectura: " + e.getMessage());
            System.exit(2); // Código de error 2 si falla el archivo
        }
    }
}