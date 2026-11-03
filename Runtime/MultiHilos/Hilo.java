package MultiHilos;

public class Hilo extends Thread {
    
    // Constructor que recibe el nombre del hilo y se lo manda a la clase padre (Thread)
    public Hilo(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println("Entro en el hilo: " + this.getName());
        try {
            // El hilo se duerme durante 5 segundos (simulando tarea)
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Salgo del hilo: " + this.getName());
    }
}