package ProcessBuilder;
import java.io.IOException;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EjecutoPython {
	private static final Logger logger = LogManager.getLogger(EjecutoPython.class);
	
	public static void main(String[] args) {
		
		String rutaPython = "python";
		String url = "\"C:\\Users\\Alumno\\Documents\\PythonNS\\Runtime.py\"";
	
		String[]comando = {rutaPython, url};
		
		ProcessBuilder pb = new ProcessBuilder(comando);
		
		pb.inheritIO(); //para que se muestre lo que haya dentro del archivo por consola
		
		try {
            Process proceso = pb.start();
            int codigoRetorno = proceso.waitFor();
            
            logger.debug("Python terminó con código: {}", codigoRetorno);
            
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
 	}
}
