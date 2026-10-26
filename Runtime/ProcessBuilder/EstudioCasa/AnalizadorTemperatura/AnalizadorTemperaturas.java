package ProcessBuilder.EstudioCasa.AnalizadorTemperatura;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AnalizadorTemperaturas {

    public void analizarYGuardar(String ficheroEntrada, String umbralTexto, String ficheroSalida) {
        int umbral = Integer.parseInt(umbralTexto);
        int diasSuperados = 0;

        // Leo el fichero de temperaturas línea a línea
        try (BufferedReader br = new BufferedReader(new FileReader(ficheroEntrada))) {
            String linea = br.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    int temperatura = Integer.parseInt(linea.trim());
                    // Si la temperatura supera o iguala el umbral, sumamos un día
                    if (temperatura >= umbral) {
                        diasSuperados++;
                    }
                }
                linea = br.readLine();
            }
        } catch (IOException e) {
            System.err.println("Error al leer el fichero de temperaturas: " + e.getMessage());
        }

        // Escribir el resultado 
        try (FileWriter escritor = new FileWriter(ficheroSalida)) {
            escritor.write(Integer.toString(diasSuperados));
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
        String umbral = args[1];
        String ficheroSalida = args[2];

        AnalizadorTemperaturas analizador = new AnalizadorTemperaturas();
        analizador.analizarYGuardar(ficheroEntrada, umbral, ficheroSalida);
    }
    
    /* Usé los argumentos datos.txt 25 25.txt y sale 28 porque hay 28 días donde la temperatura es mayor de 25*/
}