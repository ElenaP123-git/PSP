package ProcessBuilder.EjecutoJava;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanzaComandosJava {
	
	private static final Logger logger = LogManager.getLogger(LanzaComandosJava.class);

	private final static String directorioGenerarClasses = "target\\classes";
	private final static String rutaSourceJava = "src\\main\\java\\unidad1\\ProcesoHijo.java";

	public int lanzaCompilador(String rutaFicheroFuente, String rutaParaGenerarClass) {
		ProcessBuilder pb;
		int exit = -1;
		
		try {
			String[] comando = { "javac", "-d", directorioGenerarClasses, rutaSourceJava };
			pb = new ProcessBuilder(comando);
			pb.inheritIO(); 
			Process proceso = pb.start();
			exit = proceso.waitFor(); 
			
			logger.debug("javac terminó con código {}", exit); 

		} catch (IOException | InterruptedException e) { 			
			logger.error(e.getMessage()); 		
		}
		
		return exit; 
	}
	
	public static void main(String[] args) {
		
		if (args.length > 0) {
			logger.info("He recibido {} argumentos:", args.length);
			for (int i = 0; i < args.length; i++) {
				logger.info("Argumento {}: {}", i, args[i]);
			}
		} else {
			logger.info("No he recibido ningún argumento.");
		}
		
		LanzaComandosJava l = new LanzaComandosJava();
		int codigo = l.lanzaCompilador(rutaSourceJava, directorioGenerarClasses);                 
	}
}