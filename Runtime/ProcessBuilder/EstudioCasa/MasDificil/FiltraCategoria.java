package ProcessBuilder.EstudioCasa.MasDificil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FiltraCategoria {
    public static void main(String[] args) {
        // Comprobamos que nos llega el fichero y la categoría por argumentos
        if (args.length < 2) {
            System.exit(1);
        }

        String ficheroVentas = args[0];
        String categoriaBuscada = args[1].trim();

        int numLineas = 0;
        double importeNeto = 0.0;
        List<String> lineasFiltradas = new ArrayList<>();

        // Leemos el fichero de ventas general
        try (BufferedReader br = new BufferedReader(new FileReader(ficheroVentas))) {
            String linea = br.readLine();
            
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    // Separamos la línea por los puntos y coma (;)
                    // Formato: CODIGO; TIENDA; CATEGORIA; UNIDADES; PRECIO
                    String[] partes = linea.split(";");
                    
                    if (partes.length >= 5) {
                        String catLinea = partes[2].trim();
                        
                        // Si la categoría de la línea coincide con la que buscamos
                        if (catLinea.equalsIgnoreCase(categoriaBuscada)) {
                            lineasFiltradas.add(linea);
                            numLineas++;
                            
                            try {
                                int unidades = Integer.parseInt(partes[3].trim());
                                double precio = Double.parseDouble(partes[4].trim());
                                importeNeto += (unidades * precio);
                            } catch (NumberFormatException e) {
                                // Control por si falla algún número
                            }
                        }
                    }
                }
                linea = br.readLine();
            }
        } catch (IOException e) {
            System.exit(1);
        }

        // TRUCO DE EXAMEN: Si la categoría no tiene ventas, no se crea fichero y se sale con código 2
        if (numLineas == 0) {
            System.exit(2);
        }

        // Si hay ventas, generamos el fichero propio de la categoría (ej: Frutas.txt)
        String nombreFicheroSalida = categoriaBuscada + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFicheroSalida))) {
            for (String l : lineasFiltradas) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.exit(1);
        }

        // Enviamos el resumen al padre por su salida estándar (getInputStream del padre lo leerá)
        // Formato clave:valor -> Categoria:numLineas:importeNeto
        System.out.println(categoriaBuscada + ":" + numLineas + ":" + importeNeto);
        
        // Terminamos con éxito
        System.exit(0);
    }
}