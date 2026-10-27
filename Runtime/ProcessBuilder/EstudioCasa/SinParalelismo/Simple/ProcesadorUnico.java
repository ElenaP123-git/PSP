package ProcessBuilder.EstudioCasa.SinParalelismo.Simple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcesadorUnico {
    private static final Logger logger = LogManager.getLogger(ProcesadorUnico.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            logger.error("No se ha indicado el fichero a procesar.");
            System.exit(1); // Código de error
        }

        String rutaFichero = args[0];
        int contadorLineas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea = br.readLine();
            while (linea != null) {
                contadorLineas++;
                linea = br.readLine();
            }
            
            // Enviamos el resultado al padre a través de la tubería estándar (stdout)
            System.out.println("Lineas totales:" + contadorLineas);
            System.exit(0); // Éxito

        } catch (IOException e) {
            logger.error("Error al leer el fichero: " + e.getMessage());
            System.err.println("Error: Fichero no encontrado o inaccesible");
            System.exit(2); // Código de error específico
        }
    }
}