import java.io.IOException;

import org.apache.logging.log4j.LogManager; //loggers
import org.apache.logging.log4j.Logger;

public class ProcesoRntm {
	private static final Logger logger = LogManager.getLogger(ProcesoRntm.class);
	
	public static void main(String[] args) {
	    // con esto se interacciona con el SO
	    Runtime rt = Runtime.getRuntime(); 
	    
	    // "notepad, crea el fichero mifichero.txt"
	    String[] informacionProceso = {"notepad.exe", "miFichero.txt"}; //[cite: 56, 73]
	    
	    // Process: representará al proceso en ejecución
	    Process proceso; 
	    
	    try {
	        // .exec(): lanza la ejecución a partir del array
	        proceso = rt.exec(informacionProceso); 
	        
	        // proceso.waitFor(): detiene el programa padre a la espera de que el hijo termine
	        int codigoRetorno = proceso.waitFor(); 
	        
	        logger.debug(codigoRetorno); 
	        
	    } catch (IOException e) { 
	    	logger.error(e.getMessage()); 
	    } catch (InterruptedException e) { 
	    	logger.error(e.getMessage()); 
	    }
	}
}
