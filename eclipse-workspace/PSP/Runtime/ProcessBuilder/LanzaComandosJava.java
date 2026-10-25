package ProcessBuilder;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanzaComandosJava {
	
    private static final Logger logger = LogManager.getLogger(LanzaComandosJava.class);

    private final static String directorioGenerarClasses = "target\\classes";
    private final static String rutaSourceJava = "src\\main\\java\\unidad1\\ProcesoHijo.java";

    public int lanzaCompilador(String rutaFicheroFuente, String rutaParaGenerarClass) {
        ProcessBuilder pb;
        int exit = -1; // Declaramos 'exit' fuera del try para poder retornarla luego
        
        try {
            String[] comando = { "javac", "-d", directorioGenerarClasses, rutaSourceJava };
            pb = new ProcessBuilder(comando);
            pb.inheritIO(); // La salida y los errores se ven por consola
            
            Process proceso = pb.start();
            exit = proceso.waitFor(); // Esperamos a que javac termine
            
            logger.debug("javac terminó con código {}", exit);

        } catch (IOException | InterruptedException e) {            
            logger.error(e.getMessage());
        }
        
        return exit;
    }
	
    public static void main(String[] args) {
        LanzaComandosJava l = new LanzaComandosJava();
        int codigo = l.lanzaCompilador(rutaSourceJava, directorioGenerarClasses);                 
    } 
}