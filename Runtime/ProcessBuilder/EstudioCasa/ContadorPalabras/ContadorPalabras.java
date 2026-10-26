package ProcessBuilder.EstudioCasa.ContadorPalabras;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ContadorPalabras {

    public void contarYGuardar(String ficheroEntrada, String palabraBuscada, String ficheroSalida) {
        int contador = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada))) {
           
            String linea = br.readLine();
            while (linea != null) {
                // Espacios en blanco
                String[] palabrasLinea = linea.split("\\s+");
                
                int i = 0;
                while (i < palabrasLinea.length) {
                    String palabraLimpia = palabrasLinea[i].toLowerCase();
                    if (palabraLimpia.equals(palabraBuscada.toLowerCase())) {
                        contador++;
                    }
                    i++;
                }
                
                linea = br.readLine();
            }
            
        } catch (IOException e) {
            System.err.println("Error al leer el fichero: " + e.getMessage());
        }

        // Escribimos el resultado en su archivo .txt 
        try (FileWriter escritor = new FileWriter(ficheroSalida)) {
            escritor.write(Integer.toString(contador));
        } catch (IOException e) {
            System.err.println("Error al escribir el resultado: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Faltan argumentos en el proceso hijo.");
            System.exit(1);
        }

        String ficheroEntrada = args[0];
        String palabra = args[1];
        String ficheroResultado = args[2];

        ContadorPalabras trabajador = new ContadorPalabras();
        trabajador.contarYGuardar(ficheroEntrada, palabra, ficheroResultado);
    }
}