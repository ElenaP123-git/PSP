package ProcessBuilder.EstudioCasa.Numbers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Hija {
    private static final Logger logger = LogManager.getLogger(Hija.class);
    
    public static void main(String[] args) {
        // Control de argumentos obligatorios (fichero y operación)
        if (args.length < 2) {
            logger.error("Insuficientes argumentos");
            System.exit(1);
        }
        
        String fichero = args[0];
        String operacion = args[1];
        
        // try-with-resources: El BufferedReader se cierra solo automáticamente al terminar
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            int maximo = Integer.MIN_VALUE;
            int minimo = Integer.MAX_VALUE;
            int suma = 0;
            int contador = 0;
            
            String linea = br.readLine();
            
            // Bucle limpio de lectura línea a línea sin usar 'break'
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    int numero = Integer.parseInt(linea.trim());
                    
                    if (numero > maximo) {
                        maximo = numero;
                    }
                    if (numero < minimo) {
                        minimo = numero;
                    }
                    suma += numero;
                    contador++;
                }
                linea = br.readLine();
            }
            
            if (contador == 0) {
                logger.error("El fichero está vacío");
                System.exit(2);
            }
            
            // Evaluamos qué operación pidió el Padre y devolvemos el resultado por consola
            if (operacion.equalsIgnoreCase("max")) {
                System.out.println(maximo);
            } else {
                if (operacion.equalsIgnoreCase("min")) {
                    System.out.println(minimo);
                } else {
                    if (operacion.equalsIgnoreCase("media")) {
                        double media = (double) suma / contador;
                        System.out.println(media);
                    } else {
                        logger.error("Operación no reconocida");
                        System.exit(3);
                    }
                }
            }
            
        } catch (IOException e) {
            logger.error("Error de E/S: " + e.getMessage());
            System.exit(4);
        }
    }
}