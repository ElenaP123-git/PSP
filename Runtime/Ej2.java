import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ej2 {

    private static final Logger logger = LogManager.getLogger(Ej2.class);

    public static void main(String[] args) {

        logger.debug("--- LISTADO CON PROCESSBUILDER ---");
        
        ProcessBuilder pb = new ProcessBuilder("tasklist"); 
        
        // inheritIO(): redirige automáticamente la salida del hijo a la consola del padre
        pb.inheritIO(); //con esto se ahorra el .getInputStream()
        
        try {
            // .start(): empieza proceso
            Process proceso = pb.start(); 
            
            // waitFor(): espera a que el comando termine (obligado a capturar InterruptedException)
            proceso.waitFor(); 
            
        } catch (IOException | InterruptedException e) { 
            logger.error(e.getMessage());
        }

        
        logger.debug("--- LISTADO CON RUNTIME ---");
        
        Runtime rt = Runtime.getRuntime();
        try {
            // .exec(): ejecuta tasklist
            Process pRuntime = rt.exec("tasklist"); 
            
            // InputStream: para leer bytes 
            // transferTo(): pasa los bytes del proceso a la pantalla 
            
            InputStream input = pRuntime.getInputStream(); 
            input.transferTo(System.out); 
            
            pRuntime.waitFor(); 
            
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}