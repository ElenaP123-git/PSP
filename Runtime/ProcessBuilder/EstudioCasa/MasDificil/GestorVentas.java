package ProcessBuilder.EstudioCasa.MasDificil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorVentas {
    private static final String DIRECTORIO = "target\\classes";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del fichero de ventas: ");
        String ficheroVentas = scanner.nextLine().trim();

        System.out.print("Introduce las categorías separadas por comas: ");
        String entradaCategorias = scanner.nextLine().trim();
        String[] categorias = entradaCategorias.split(",");

        List<Process> procesosCategorias = new ArrayList<>();
        List<String> nombresCategorias = new ArrayList<>();

        try {
            // 1. LANZAR EL PROCESO DE DEVOLUCIONES
            String[] cmdDevoluciones = {
                "java", "-cp", DIRECTORIO, "ProcessBuilder.ExamenSupermercado.DetectaDevoluciones"
            };
            ProcessBuilder pbDev = new ProcessBuilder(cmdDevoluciones);
            Process procDevoluciones = pbDev.start();

            // INYECTAR DATOS AL HIJO: El padre lee el fichero y se lo escribe al hijo por su getOutputStream()
            try (BufferedWriter bwDev = new BufferedWriter(new OutputStreamWriter(procDevoluciones.getOutputStream()));
                 BufferedReader brFichero = new BufferedReader(new FileReader(ficheroVentas))) {
                
                String lineaFichero = brFichero.readLine();
                while (lineaFichero != null) {
                    bwDev.write(lineaFichero);
                    bwDev.newLine();
                    lineaFichero = brFichero.readLine();
                }
                bwDev.flush(); // ¡Obligatorio para vaciar el búfer y que el hijo reciba todo!
            } // Al cerrar este bloque, el hijo recibe el fin de fichero (EOF) en su System.in

            // 2. LANZAR EN PARALELO LOS HIJOS DE CATEGORÍAS
            for (String cat : categorias) {
                String categoriaLimpia = cat.trim();
                nombresCategorias.add(categoriaLimpia);

                String[] cmdCat = {
                    "java", "-cp", DIRECTORIO, "ProcessBuilder.EstudioCasa.MasDificil.FiltraCategoria",
                    ficheroVentas, categoriaLimpia
                };
                ProcessBuilder pbCat = new ProcessBuilder(cmdCat);
                Process pCat = pbCat.start(); // Lanzamos y guardamos sin hacer waitFor todavía (paralelismo real)
                procesosCategorias.add(pCat);
            }

            // 3. RECOGER DATOS DE LAS CATEGORÍAS Y EVALUAR CÓDIGOS DE SALIDA
            System.out.println("\n===== RESUMEN POR CATEGORÍA =====");
            int totalLineasFiltradas = 0;
            double importeNetoTotal = 0.0;

            for (int i = 0; i < procesosCategorias.size(); i++) {
                Process p = procesosCategorias.get(i);
                String catName = nombresCategorias.get(i);

                // Leemos lo que el hijo imprimió por su System.out
                String salidaHijo = "";
                try (BufferedReader brHijo = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                    salidaHijo = brHijo.readLine();
                }

                // Esperamos a que termine el proceso y recogemos su código de salida
                int exitCode = p.waitFor();

                // Evaluamos si el hijo devolvió código 2 (significa que no hubo ventas)
                if (exitCode == 2) {
                    System.out.println(catName + " : sin ventas (no se genera fichero)");
                } else if (exitCode == 0 && salidaHijo != null) {
                    // Formato recibido: Categoria:numLineas:importeNeto
                    String[] partes = salidaHijo.split(":");
                    if (partes.length >= 3) {
                        int nLineas = Integer.parseInt(partes[1].trim());
                        double imp = Double.parseDouble(partes[2].trim());
                        
                        totalLineasFiltradas += nLineas;
                        importeNetoTotal += imp;
                        
                        System.out.printf("%-10s : %d líneas | %.2f €%n", catName, nLineas, imp);
                    }
                }
            }

            System.out.println("Nº total de líneas filtradas: " + totalLineasFiltradas);
            System.out.printf("Importe neto total          : %.2f €%n", importeNetoTotal);

            // 4. RECOGER RESULTADOS DE DEVOLUCIONES
            String salidaDev = "";
            try (BufferedReader brDevOut = new BufferedReader(new InputStreamReader(procDevoluciones.getInputStream()))) {
                salidaDev = brDevOut.readLine();
            }
            
            // El número de devoluciones se devuelve directamente en el código de salida (exitCode)
            int numDevolucionesReal = procDevoluciones.waitFor(); 

            double importeDevuelto = 0.0;
            int totalLineasProcesadasDev = 0;
            if (salidaDev != null && !salidaDev.isEmpty()) {
                String[] partesDev = salidaDev.split(":");
                if (partesDev.length >= 2) {
                    importeDevuelto = Double.parseDouble(partesDev[0].trim());
                    totalLineasProcesadasDev = Integer.parseInt(partesDev[1].trim());
                }
            }

            System.out.println("\n===== DEVOLUCIONES =====");
            System.out.println("Nº de devoluciones          : " + numDevolucionesReal + " de " + totalLineasProcesadasDev + " líneas");
            System.out.printf("Importe devuelto            : %.2f €%n", importeDevuelto);
            
            if (totalLineasProcesadasDev > 0) {
                double porcentaje = ((double) numDevolucionesReal / totalLineasProcesadasDev) * 100;
                System.out.printf("%% de líneas que son devolución: %.2f%%%n", porcentaje);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}