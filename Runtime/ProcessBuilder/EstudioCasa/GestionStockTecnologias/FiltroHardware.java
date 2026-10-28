package ProcessBuilder.EstudioCasa.GestionStockTecnologias;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FiltroHardware {
    private static final Logger logger = LogManager.getLogger(FiltroHardware.class);

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Argumentos insuficientes para FiltroHardware.");
            System.exit(1);
        }

        String ficheroEntrada = args[0];
        String ficheroSalida = args[1];

        int totalLineas = 0;
        int lineasHardware = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada));
             BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroSalida))) {

            // 1. SALTAR LA PRIMERA LÍNEA (TÍTULOS / CABECERA)
            String cabecera = br.readLine();
            if (cabecera != null) {
                logger.info("Cabecera ignorada: " + cabecera);
            }

            String linea = br.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    totalLineas++;
                    
                    // Formato esperado: Codigo;Descripcion;Departamento;Prioridad
                    String[] partes = linea.split(";");
                    if (partes.length >= 3) {
                        String departamento = partes[2].trim();
                        
                        if (departamento.equalsIgnoreCase("Hardware")) {
                            bw.write(linea);
                            bw.newLine();
                            lineasHardware++;
                        }
                    }
                }
                linea = br.readLine();
            }

            // 2. Comunicar resultados al padre mediante clave:valor
            System.out.println("hardware_encontradas:" + lineasHardware);
            System.out.println("hardware_totales:" + totalLineas);
            System.exit(0);

        } catch (IOException e) {
            logger.error("Error de E/S en FiltroHardware: " + e.getMessage());
            System.err.println("Error procesando fichero de hardware: " + e.getMessage());
            System.exit(2);
        }
    }
}