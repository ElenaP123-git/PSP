package ProcessBuilder.EstudioCasa.SinParalelismo.Simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanzadorUnico {
    private static final Logger logger = LogManager.getLogger(LanzadorUnico.class);

    private final static String DIRECTORIO = "target\\classes";
    private final static String CLASE_EJECUTABLE = "ProcessBuilder.EstudioCasa.SinParalelismo.Simple.ProcesadorUnico";
    private final static String FICHERO_DATOS = "datos.txt";

    public static void main(String[] args) {
        LanzadorUnico lanzador = new LanzadorUnico();
        lanzador.ejecutarProcesoUnico();
    }

    public void ejecutarProcesoUnico() {
        try {
            // 1. Preparamos el comando único para ejecutar la clase Java con su argumento
            String[] comando = {
                "java",
                "-cp", DIRECTORIO,
                CLASE_EJECUTABLE,
                FICHERO_DATOS
            };

            ProcessBuilder pb = new ProcessBuilder(comando);
            
            logger.info("PADRE: Lanzando el proceso único...");
            
            // 2. Arrancamos el único proceso hijo
            Process proceso = pb.start();

            // 3. Leemos la salida que el hijo envía mediante System.out.println
            try (BufferedReader br = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                String linea = br.readLine();
                while (linea != null) {
                    logger.info("PADRE - Recibido del hijo: " + linea);
                    linea = br.readLine();
                }
            }

            // 4. El padre se queda esperando (bloqueado) a que el hijo termine por completo
            int exitCode = proceso.waitFor();

            // 5. Comprobamos el código de salida
            if (exitCode == 0) {
                logger.info("PADRE: El proceso hijo ha finalizado correctamente.");
            } else {
                logger.error("PADRE: El proceso hijo falló con código de salida: " + exitCode);
                
                // Opcional: Si el hijo falló, podríamos leer también su flujo de errores (stderr)
                leerErroresHijo(proceso);
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Error en la ejecución del proceso: " + e.getMessage());
        }
    }

    private void leerErroresHijo(Process proceso) {
        try (BufferedReader brError = new BufferedReader(new InputStreamReader(proceso.getErrorStream()))) {
            String error = brError.readLine();
            while (error != null) {
                logger.error("PADRE - Error capturado del hijo: " + error);
                error = brError.readLine();
            }
        } catch (IOException e) {
            logger.error("No se pudo leer el canal de errores: " + e.getMessage());
        }
    }
}