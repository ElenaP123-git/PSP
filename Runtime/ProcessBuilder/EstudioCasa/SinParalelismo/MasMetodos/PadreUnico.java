package ProcessBuilder.EstudioCasa.SinParalelismo.MasMetodos;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PadreUnico {
    private static final Logger logger = LogManager.getLogger(PadreUnico.class);

    private final static String DIRECTORIO = "target\\classes";
    private final static String CLASE_HIJO = "ProcessBuilder.EstudioCasa.EjemploMetodos.ProcesadorPorPasos";

    public static void main(String[] args) {
        try {
            String[] comando = {
                "java",
                "-cp", DIRECTORIO,
                CLASE_HIJO
            };

            ProcessBuilder pb = new ProcessBuilder(comando);
            
            // Usamos inheritIO para ver los logs del hijo directamente en la consola con claridad
            pb.inheritIO(); 

            logger.info("PADRE: Lanzando el proceso hijo...");
            Process proceso = pb.start();

            // El padre se bloquea aquí esperando a que el hijo acabe todos sus métodos
            int codigoSalida = proceso.waitFor();

            logger.info("PADRE: El hijo terminó con código de salida: " + codigoSalida);

        } catch (IOException | InterruptedException e) {
            logger.error("Error al ejecutar el proceso: " + e.getMessage());
        }
    }
}