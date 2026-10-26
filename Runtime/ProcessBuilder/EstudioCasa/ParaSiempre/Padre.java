package ProcessBuilder.EstudioCasa.ParaSiempre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Padre {

    private static final String DIR_CLASSES = "target\\classes";
    private static final String RUTA_FUENTE = "src\\main\\java\\mi\\paquete\\Hija.java";

    // 1. Método para compilar automáticamente
    public void compila() {
        try {
            String[] comando = { "javac", "-d", DIR_CLASSES, RUTA_FUENTE };
            ProcessBuilder pb = new ProcessBuilder(comando);
            pb.inheritIO(); // Muestra los errores de compilación en tu consola
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 2. Método para ejecutar en paralelo y gestionar la escritura/lectura
    public void ejecuta() {
        String[] elementos = { "opcion1", "opcion2", "opcion3" }; // O tus umbrales/parámetros
        List<Process> listaProcesos = new ArrayList<>();

        // LANZAMIENTO EN PARALELO (Bucle sin waitFor dentro)
        for (String elemento : elementos) {
            try {
                // Si el hijo escribe en un fichero, le pasamos el nombre del fichero de salida como argumento.
                // Por ejemplo, usando el propio nombre del elemento: "opcion1.txt"
                String ficheroSalida = elemento + ".txt";

                String[] comando = {
                    "java",
                    "-cp",
                    DIR_CLASSES,
                    "mi.paquete.Hija", // Ruta completa de la clase hija
                    "datos.txt",       // Argumento 0 (fichero de entrada)
                    elemento,          // Argumento 1 (parámetro variable)
                    ficheroSalida      // Argumento 2 (fichero donde el hijo va a escribir)
                };

                ProcessBuilder pb = new ProcessBuilder(comando);
                Process hijo = pb.start(); // Arranca y sigue el bucle
                listaProcesos.add(hijo);   // Guardamos en la lista

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // ESPERAR A QUE TODOS TERMINEN (Sincronización obligatoria antes de leer ficheros)
        for (Process p : listaProcesos) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // LEER LOS RESULTADOS DEL DISCO (o de la consola si prefieres) Y COMPROBAR ERRORES
        int i = 0;
        for (Process p : listaProcesos) {
            String elementoActual = elementos[i];
            String ficheroResultado = elementoActual + ".txt";
            i++;

            try {
                if (p.exitValue() == 0) { // Si el hijo terminó bien (exit(0))
                    
                    // OPCIÓN A: Leer lo que el hijo escribió en su fichero de salida (FileWriter)
                    try (BufferedReader br = new BufferedReader(new FileReader(ficheroResultado))) {
                        String contenidoFichero = br.readLine();
                        System.out.println("Resultado leído del fichero [" + ficheroResultado + "]: " + contenidoFichero);
                    }
                    
                    /* 
                    // OPCIÓN B (por si usas tuberías): Leer lo que imprimió por consola (getInputStream)
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                        String resultadoHijo = br.readLine();
                        System.out.println("Resultado recibido por consola: " + resultadoHijo);
                    }
                    */

                } else {
                    System.err.println("Un proceso hijo falló con código: " + p.exitValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Padre lanzador = new Padre();
        lanzador.compila();
        lanzador.ejecuta();
    }
}