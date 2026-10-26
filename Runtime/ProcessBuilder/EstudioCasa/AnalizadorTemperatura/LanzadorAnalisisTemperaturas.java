package ProcessBuilder.EstudioCasa.AnalizadorTemperatura;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanzadorAnalisisTemperaturas {

    private static final Logger logger = LogManager.getLogger(LanzadorAnalisisTemperaturas.class);
    
    private final static String DIRECTORIO_CLASSES = "target\\classes";
    private final static String RUTA_FUENTE = "src\\main\\java\\ProcessBuilder\\EstudioCasa\\AnalizadorTemperaturas.java";
    private final static String FICHERO_TEMPERATURAS = "datos.txt"; 

    public void compila() {
        try {
            String[] comandoCompilar = { "javac", "-d", DIRECTORIO_CLASSES, RUTA_FUENTE };
            ProcessBuilder pb = new ProcessBuilder(comandoCompilar);
            pb.inheritIO();
            Process proceso = pb.start();
            proceso.waitFor();
        } catch (IOException | InterruptedException e) {
            logger.error("Error al compilar: {}", e.getMessage());
        }
    }

    public void ejecuta() {
        int[] umbrales = {10, 20, 25, 30, 35};
        List<Process> listaProcesos = new ArrayList<>();
        
        // PROCESOS EN PARALELO
        for (int umbralActual : umbrales) {
            String ficheroSalida = umbralActual + ".txt"; 
            
            try {
                String[] comandoEjecutar = { 
                    "java", 
                    "-cp", 
                    DIRECTORIO_CLASSES, 
                    "ProcessBuilder.EstudioCasa.AnalizadorTemperaturas", 
                    FICHERO_TEMPERATURAS, 
                    Integer.toString(umbralActual), 
                    ficheroSalida 
                };
                
                ProcessBuilder pb = new ProcessBuilder(comandoEjecutar);
                Process procesoHijo = pb.start(); 
                listaProcesos.add(procesoHijo);
                
            } catch (IOException e) {
                logger.error("Error al lanzar el proceso para el umbral {}: {}", umbralActual, e.getMessage());
            }
        }

        // ESPERAR A QUE TODOS TERMINEN 
        for (Process p : listaProcesos) {
            try {
                p.waitFor(); 
            } catch (InterruptedException e) {
                logger.error("Error al esperar a un proceso: {}", e.getMessage());
            }
        }

        // LEER LOS FICHEROS Y MOSTRAR RESUMEN 
        System.out.println("Se han analizado todas las temperaturas.");
        System.out.println("Resultados:");

        int sumaTotalDias = 0;
        
        for (int umbralActual : umbrales) {
            String ficheroResultado = umbralActual + ".txt";
            
            try (BufferedReader br = new BufferedReader(new FileReader(ficheroResultado))) {
                String lineaLeida = br.readLine();
                if (lineaLeida != null) {
                    int dias = Integer.parseInt(lineaLeida.trim());
                    System.out.println("Umbral " + umbralActual + " → " + dias + " días");
                    sumaTotalDias += dias;
                }
            } catch (IOException e) {
                logger.error("Error al leer el fichero de resultado {}: {}", ficheroResultado, e.getMessage());
            }
        }

        System.out.println("Suma total de días contados en todos los procesos: " + sumaTotalDias);
    } 

    public static void main(String[] args) {
        LanzadorAnalisisTemperaturas lanzador = new LanzadorAnalisisTemperaturas();
        lanzador.compila();
        lanzador.ejecuta();
    }
} 
