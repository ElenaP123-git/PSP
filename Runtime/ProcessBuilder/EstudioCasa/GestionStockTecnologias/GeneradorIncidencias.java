package ProcessBuilder.EstudioCasa.GestionStockTecnologias;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GeneradorIncidencias {
    private static final Logger logger = LogManager.getLogger(GeneradorIncidencias.class);

    private final static String DIRECTORIO = "target\\classes";
    private final static String CLASE_HIJO_HW = "ProcessBuilder.EstudioCasa.GestionStockTecnologias.FiltroHardware";
    private final static String CLASE_HIJO_SW = "ProcessBuilder.EstudioCasa.GestionStockTecnologias.FiltroSoftware";
    private final static String FICHERO_ENTRADA = "incidencias.txt";

    public static void main(String[] args) {
        GeneradorIncidencias lanzador = new GeneradorIncidencias();
        lanzador.ejecutarProcesosEnParalelo();
    }

    public void ejecutarProcesosEnParalelo() {
        List<Process> procesos = new ArrayList<>();

        try {
            // 1. Definir comandos para ambos hijos
            String[] comandoHW = {
                "java", "-cp", DIRECTORIO, CLASE_HIJO_HW, FICHERO_ENTRADA, "hardware.txt"
            };
            
            String[] comandoSW = {
                "java", "-cp", DIRECTORIO, CLASE_HIJO_SW, FICHERO_ENTRADA, "software.txt"
            };

            // 2. LANZAR EN PARALELO (sin esperar entre un .start() y otro)
            ProcessBuilder pbHW = new ProcessBuilder(comandoHW);
            Process procesoHW = pbHW.start();
            procesos.add(procesoHW);

            ProcessBuilder pbSW = new ProcessBuilder(comandoSW);
            Process procesoSW = pbSW.start();
            procesos.add(procesoSW);

            logger.info("PADRE: Todos los procesos hijos han sido lanzados en paralelo.");

            // 3. RECOGER DATOS Y ESPERAR A LOS HIJOS
            for (Process proc : procesos) {
                // Leer salida estándar (getInputStream)
                try (BufferedReader brOut = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                    String linea = brOut.readLine();
                    while (linea != null) {
                        logger.info("PADRE (stdout) -> " + linea);
                        linea = brOut.readLine();
                    }
                }

                // Leer errores si los hubiera (getErrorStream)
                try (BufferedReader brErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                    String error = brErr.readLine();
                    while (error != null) {
                        logger.warn("PADRE (stderr - Error del hijo) -> " + error);
                        error = brErr.readLine();
                    }
                }

                // Esperar a que el hijo muera y comprobar código de salida
                int exitCode = proc.waitFor();
                if (exitCode != 0) {
                    logger.error("Un proceso hijo finalizó con código de error: " + exitCode);
                }
            }

            logger.info("PADRE: Ejecución completada con éxito. Ficheros generados.");

        } catch (IOException | InterruptedException e) {
            logger.error("Error general en el proceso padre: " + e.getMessage());
        }
    }
}