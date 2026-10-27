package ProcessBuilder.EstudioCasa.AnalizarTiendaDefi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ProcessBuilder.EstudioCasa.Numbers.Hija;

public class LanzadorAnalisisTienda {
	private static final Logger logger = LogManager.getLogger(Hija.class);
	
	private final static String DIRECTORIO = "target\\classes"; 
	private final static String CLASE_EJECUTABLE = "ProcessBuilder.EstudioCasa.AnalizarTiendaDefi.AnalizadorTienda";
	private final static String RUTAHIJA = "src\\main\\java\\ProcessBuilder\\EstudioCasa\\AnalizarTienda\\AnalizadorTienda.java";
	private final static String FICHEROENTRADA = "temperatuta_centro.txt";
	
	public void compilar() {
		try{
			String[] comandoCompilar  = {"javac", "-d", DIRECTORIO, RUTAHIJA };
			ProcessBuilder pb = new ProcessBuilder(comandoCompilar);
			pb.inheritIO(); //con esto el padre no toma lo que le pase el hijo, solo lo muestra por consola
			Process proceso = pb.start();
			proceso.waitFor();
		} catch(IOException | InterruptedException e) {
			logger.error("Error al compilar" + e.getMessage() );
		}
	}
	
	public void ejecutar() {
		String [] productosBuscados = {"Camiseta","Mascara Ghostface", "Pantalon", "Gabardina", "Zapatos"};
		List<Process> procesos = new ArrayList<>();
		
		for(String product: productosBuscados) {
			try {
				String ficheroSalida = product + ".txt"; //ficheros que se van a crear
				
				String [] comandoEjecutar = {
						"java", 
						"-cp", DIRECTORIO,
						CLASE_EJECUTABLE,
						FICHEROENTRADA,  // arg[0] (aqui es donde le paso al metodo del hijo los args que tiene que utilizar para crear...
						product,         // arg[1]   los archivos con el nombre que el padre quiere)
						ficheroSalida    // arg[2]  Y el hijo lo lee con su BufferedReader
				};
				
				ProcessBuilder pb = new ProcessBuilder(comandoEjecutar);
				Process proceso = pb.start();
				procesos.add(proceso);
				
	 		} catch(IOException e) {
	 			logger.error("Error para lanzar el proceso para el producto " + product + " " + e.getMessage());
	 		}
		}
		
		// EL PADRE RECOGE LOS DATOS Y ESPERA A LOS HIJOS
		for(Process proc: procesos) { 
			try (BufferedReader brIs = new BufferedReader(new InputStreamReader(proc.getInputStream()))){ //El sysot del hijo sirve para que el padre pueda verlo aqui con .getInputStream() 
				String linea = brIs.readLine();
				while(linea != null) {
					logger.info("Padre - Recibido del proceso hijo --> " + linea);
					linea = brIs.readLine(); // Corregido aquí
				}
				
				int exitCode = proc.waitFor();
				if(exitCode != 0) {
					logger.error("Un proceso hijo terminó con errores, codigo salida -> " + exitCode);
				}
				
			} catch (InterruptedException | IOException  e) {
				logger.error("Error al esperar un proceso " + e.getMessage());
			}
		}
		logger.info("Todos los procesos han finalizado y los ficheros han sido creados");
	}
	
	/*El padre solo coordina:
	  en el primer bucle los lanza a todos en paralelo para que trabajen a la vez, 
	  y en el segundo bucle se queda esperando a que terminen y recogiendo los avisos.*/
}