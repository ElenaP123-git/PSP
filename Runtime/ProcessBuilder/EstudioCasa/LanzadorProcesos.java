package ProcessBuilder.EstudioCasa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanzadorProcesos {
	private static final Logger logger = LogManager.getLogger(LanzadorProcesos.class);
	
	private final static String DIRECTORIO_CLASSES = "target\\classes";
	// Ruta del archivo físico para compilarlo con javac
	private final static String RUTA_FUENTE_HIJO = "src\\main\\java\\ProcessBuilder\\EstudioCasa\\CalculadorHijo.java";
	
	public void compilarClase() {
		try {
			String[] comandoCompilar = { "javac", "-d", DIRECTORIO_CLASSES, RUTA_FUENTE_HIJO };
			
			ProcessBuilder pb = new ProcessBuilder(comandoCompilar);
			pb.inheritIO(); // Bueno para ver los errores de compilación por consola
			Process procesoCompilacion = pb.start();
			
			int codigoSalida = procesoCompilacion.waitFor();
			
			if(codigoSalida == 0) {
				logger.info("La clase hija se ha compilado correctamente");
			} else {
				logger.error("Error al compilar la clase hija. Código de salida: " + codigoSalida);
			}
		} catch (IOException | InterruptedException e) { 
			logger.error("Excepción crítica durante compilación: " + e);
		}
	}
	
	public int ejecutarClaseHija(String numeroAEnviar) {
		int resultadoLeidoDelHijo = -1;
		
		try {
			// AL EJECUTAR: Se usa java, el classpath (-cp), la clase con su paquete completo y el argumento
			String[] comandoEjecutar = {
				"java", 
				"-cp", 
				DIRECTORIO_CLASSES, 
				"ProcessBuilder.EstudioCasa.CalculadorHijo", 
				numeroAEnviar 
			};
			
			ProcessBuilder pb = new ProcessBuilder(comandoEjecutar);
			//  NO ponemos inheritIO() porque queremos aspirar el resultado con el BufferedReader
			
			Process procesoHijo = pb.start();
			
			// Aspiramos el resultado que el hijo imprimió con System.out.println
			try (BufferedReader lectorSalida = new BufferedReader(new InputStreamReader(procesoHijo.getInputStream()))){
				String lineaLeida = lectorSalida.readLine();
				if(lineaLeida != null) {
					resultadoLeidoDelHijo = Integer.parseInt(lineaLeida.trim());
				}
			}
			
			int codigoSalidaHijo = procesoHijo.waitFor(); // Esperamos a que termine el hijo
			
			if (codigoSalidaHijo != 0) {
                logger.error("El proceso hijo terminó con errores (Código: {})", codigoSalidaHijo);
            } else {
                logger.info("El proceso hijo finalizó con éxito. Resultado obtenido: {}", resultadoLeidoDelHijo);
            }
			
		} catch(IOException | InterruptedException e) {
			logger.error("Error de E/S o interrupción al ejecutar el subproceso: {}", e);
		}
		
		return resultadoLeidoDelHijo;
	} 

	public static void main(String[] args) {
		LanzadorProcesos lanzador = new LanzadorProcesos();
		
		// Primero siempre compilamos
		lanzador.compilarClase();
		
		// Después ejecutamos pasando el "5"
		int resultadoFinal = lanzador.ejecutarClaseHija("5");
        
        logger.info("El programa Padre ha recuperado el valor final: {}", resultadoFinal);
	}
}