package ProcessBuilder.EstudioCasa.AnalizarTiendaDefi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnalizadorTienda {
    private static final Logger logger = LogManager.getLogger(AnalizadorTienda.class);
    
    public static void main(String[] args) {
        if(args.length < 3) {
            logger.error("Argumentos insuficientes");
            System.exit(1);
        }
        String ficheroEntrada = args[0];
        String productoABuscar = args[1];
        String ficheroResultados = args[2];
        
        try {
            AnalizadorTienda analizaT = new AnalizadorTienda();
            int totalUnidades = analizaT.contarUnidadesProducto(ficheroEntrada, productoABuscar);
            analizaT.escribirResultadosFichero(ficheroResultados, productoABuscar, totalUnidades);
            System.out.println("total:" + totalUnidades); // Formato clave:valor para que lo lea el padre
            
        } catch(FileNotFoundException e) {
            logger.error("Fichero no encontrado: " + e.getMessage());
            System.err.println("Error: Fichero no encontrado");
            System.exit(2);
        } catch(IOException e) {
            logger.error("Error de E/S: " + e.getMessage());
            System.exit(3);
        }
    }
    
    public int contarUnidadesProducto(String rutaFichero, String productoBuscado) throws IOException {
        int acumulado = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea = br.readLine();
            while (linea != null) {
                if(!linea.trim().isEmpty()) {
                    String[] partes = linea.split(":");
                    if(partes.length == 2) { 
                        String prod = partes[0].trim(); 
                        int unidades = Integer.parseInt(partes[1].trim()); 
                        
                        if(prod.equalsIgnoreCase(productoBuscado)) {
                            acumulado += unidades;
                        }
                    }
                }
                linea = br.readLine(); 
            }
        } 
        return acumulado;
    }
    
    public void escribirResultadosFichero(String rutaFichero, String producto, int total) throws IOException {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero))) {
            bw.write("Producto: " + producto + " - Total unidades: " + total);
            bw.newLine(); // Esto añade un salto de línea al fichero de texto
        } 
    }
    
    /*El hijo trabaja de forma aislada: recibe sus argumentos, 
     lee el archivo grande de temperaturas, 
     calcula las unidades de su producto, 
     crea su fichero de texto con el resultado y se despide.*/
}