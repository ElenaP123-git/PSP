package ProcessBuilder.EstudioCasa.Numbers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LanzadorEstadisticas {
    private static final Logger logger = LogManager.getLogger(LanzadorEstadisticas.class);
    
    private static final String DIRECTORIO_CLASSES = "target\\classes";
    private static final String RUTA_FUENTE = "src\\main\\java\\ProcessBuilder\\EstudioCasa\\Ejercicios\\Numbers\\Hija.java";
    private static final String FICHERO_DATOS = "numeros.txt";

    // 1. Método para compilar la clase hija automáticamente
    public void compila() {
        try {
            String[] comando = { "javac", "-d", DIRECTORIO_CLASSES, RUTA_FUENTE };
            ProcessBuilder pb = new ProcessBuilder(comando);
            pb.inheritIO(); // Muestra los errores de compilación directamente en tu consola
            Process proceso = pb.start();
            proceso.waitFor();
            logger.info("Clase hija compilada con éxito.");
        } catch (IOException | InterruptedException e) {
            logger.error("Error al compilar: " + e.getMessage());
        }
    }

    // 2. Método para lanzar en paralelo, esperar y recoger los flujos
    public void ejecuta() {
        // Las tres operaciones que le vamos a pedir a la clase Hija
        String[] operaciones = { "max", "min", "media" };
        List<Process> listaProcesos = new ArrayList<>();

        System.out.println("=== LANZANDO PROCESOS EN PARALELO ===");

        // LANZAMIENTO EN PARALELO
        for (String operacion : operaciones) {
            try {
                String[] comando = {
                    "java",
                    "-cp",
                    DIRECTORIO_CLASSES,
                    "ProcessBuilder.EstudioCasa.Ejercicios.Hija", // Ruta completa de la clase hija
                    FICHERO_DATOS,                                // args[0]
                    operacion                                     // args[1]
                };

                ProcessBuilder pb = new ProcessBuilder(comando);
                Process hijo = pb.start(); // Arranca y sigue el bucle (paralelismo)
                listaProcesos.add(hijo);   // Guardamos el proceso en la lista

            } catch (IOException e) {
                logger.error("Error al lanzar el proceso para la operación " + operacion + ": " + e.getMessage());
            }
        }

        // ESPERAR A QUE TODOS TERMINEN (Sincronización)
        for (Process p : listaProcesos) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                logger.error("Error en la espera del proceso: " + e.getMessage());
            }
        }

        // LEER LOS RESULTADOS DE LOS FLUJOS (getInputStream) Y VALIDAR ERRORES
        System.out.println("\n=== RESULTADOS OBTENIDOS DE LOS HIJOS ===");
        int i = 0;
        for (Process p : listaProcesos) {
            String operacionActual = operaciones[i];
            i++;

            try {
                // Comprobamos si el hijo terminó correctamente (exitValue == 0)
                if (p.exitValue() == 0) {
                    // Leemos lo que el hijo escribió por System.out.println()
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                        String resultado = br.readLine();
                        System.out.println("Operación [" + operacionActual.toUpperCase() + "] -> Resultado: " + resultado);
                    }
                } else {
                    logger.error("El proceso hijo de '" + operacionActual + "' falló con código de error: " + p.exitValue());
                }
            } catch (IOException e) {
                logger.error("Error al leer el flujo del proceso: " + e.getMessage());
            }
        }
        System.out.println("=== FIN DEL PROGRAMA PADRE ===");
    }

    public static void main(String[] args) {
        LanzadorEstadisticas lanzador = new LanzadorEstadisticas();
        lanzador.compila();
        lanzador.ejecuta();
    }
}