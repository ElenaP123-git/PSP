package MultiHilos;

public class HiloPadre {
    
    public static void main(String[] args) {
        // Creamos las instancias de los hilos pasándoles su nombre
        Hilo hilo1 = new Hilo("hilo 1");
        Hilo hilo2 = new Hilo("hilo 2");
        
        // Arrancamos los hilos en paralelo con el método start()
        hilo1.start();
        hilo2.start();
        
        System.out.println("Hilo principal (main) ejecutándose de forma independiente...");
    }
}