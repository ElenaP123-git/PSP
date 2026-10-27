package ProcessBuilder.EstudioCasa.ContadorPalabras;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanzadorContadorPalabras {

	private static final Logger logger = LogManager.getLogger(LanzadorContadorPalabras.class);

	private final static String DIRECTORIO_CLASSES = "target\\classes";
	private final static String RUTA_FUENTE = "src\\main\\java\\ProcessBuilder\\EstudioCasa\\ContadorPalabras\\ContadorPalabras.java";
	private final static String FICHERO_ENTRADA = "origen.txt";

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
		String[] palabras = { "servicio", "hilo", "proceso", "multihilo", "concurrencia" };
		List<Process> listaProcesos = new ArrayList<>();

		// LANZAR TODOS LOS PROCESOS EN PARALELO
		int i = 0;
		while (i < palabras.length) {
			String palabraActual = palabras[i];
			String ficheroSalida = palabraActual + ".txt";

			try {
				String[] comandoEjecutar = { "java", "-cp", DIRECTORIO_CLASSES,
						"ProcessBuilder.EstudioCasa.ContadorPalabras.ContadorPalabras", // Ruta completa del paquete y
																						// clase
						FICHERO_ENTRADA, palabraActual, ficheroSalida };

				ProcessBuilder pb = new ProcessBuilder(comandoEjecutar);
				Process procesoHijo = pb.start();

				listaProcesos.add(procesoHijo);
				logger.info("Proceso lanzado en paralelo para la palabra: {}", palabraActual);

			} catch (IOException e) {
				logger.error("Error al lanzar el proceso para {}: {}", palabraActual, e.getMessage());
			}

			i++;
		}

		// ESPERAR A QUE TODOS TERMINEN
		int j = 0;
		while (j < listaProcesos.size()) {
			Process procesoActual = listaProcesos.get(j);
			try {
				procesoActual.waitFor();
			} catch (InterruptedException e) {
				logger.error("Error al esperar a un proceso: {}", e.getMessage());
			}
			j++;
		}

		logger.info("¡Todos los procesos hijos han finalizado su ejecución en paralelo!");
	}

	public static void main(String[] args) {
		LanzadorContadorPalabras lanzador = new LanzadorContadorPalabras();
		lanzador.compila();
		lanzador.ejecuta();
	}
}