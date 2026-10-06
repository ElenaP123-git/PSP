
import java.io.IOException;
	import org.apache.logging.log4j.LogManager;
	import org.apache.logging.log4j.Logger;
	
public class Ej1 {
	    private static final Logger logger = LogManager.getLogger(Ej1.class);

	    public static void main(String[] args) {
	        
	        String rutaChrome = "C:\\Program Files\\Google\\Chrome";
	        String url = "https://www.google.com";

	        
	        Runtime rt = Runtime.getRuntime(); 
	        
	        String[] comandoRuntime = { rutaChrome, url }; 
	        
	        try {
	            // .exec(): ejecuta Chrome pasando la URL como argumento
	            Process procesoRuntime = rt.exec(comandoRuntime); 
	            logger.debug("Chrome abierto con Runtime");
	        } catch (IOException e) {
	            logger.error(e.getMessage()); 
	        }

	        // CON PROCESSBUILDER (indica cómo se va a ejecutar el proceso antes de crearlo - PROCESS controla el proceso una vez lanzado)
	        
	        String[] comandoPB = { rutaChrome, url }; 
	        
	        ProcessBuilder pb = new ProcessBuilder(comandoPB); 
	        try {
	            // .start(): crea e inicia el nuevo proceso en el sistema
	            Process procesoPB = pb.start(); 
	            logger.debug("Chrome abierto con ProcessBuilder");
	        } catch (IOException e) {
	            logger.error(e.getMessage());
	        }
	    }
	}

