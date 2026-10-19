package ProcessBuilder.EjecutoJava;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EjecutoComando {

    private static final Logger logger = LogManager.getLogger(EjecutoComando.class);
    
    private final static String directorioGenerarClasses = "target\\classes";

    public int ejecutaClase(String clase) {
        int exit = -1;
        String claseCompleta = clase;
        
        try {
            String[] comando = { "java", "-cp", directorioGenerarClasses, claseCompleta, "hola", "holita"};
            
            ProcessBuilder pb = new ProcessBuilder(comando);
            pb.inheritIO();
            
            Process proceso = pb.start();
            exit = proceso.waitFor(); 
            
            logger.debug("El proceso terminó con código {}", exit);
            
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        
        return exit;
    }

    public static void main(String[] args) {
        EjecutoComando ejecutor = new EjecutoComando();
        
        // Ejemplo de llamada al método pasando el nombre de la clase compilada
        // (por ejemplo: "unidad1.ProcesoHijo")
        int codigo = ejecutor.ejecutaClase("unidad1.ProcesoHijo");
    }
}