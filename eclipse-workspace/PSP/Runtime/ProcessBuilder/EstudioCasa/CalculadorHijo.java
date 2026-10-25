package ProcessBuilder.EstudioCasa;

public class CalculadorHijo {
	
	public void calcularDoble(String argumentoTexto) {
		try {
			// Convertimos el texto recibido ("5") a número entero (5)
			int numero = Integer.parseInt(argumentoTexto);
			int resultado = numero * 2;
			
			// Enviamos el resultado hacia fuera por la tubería estándar (para que lo lea el padre)
			System.out.println(resultado);
			
		} catch (NumberFormatException e) {
			System.err.println("Error: el argumento no es un número válido");
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		if(args.length < 1) {
			System.err.println("Faltan argumentos.");
			System.exit(1); 
		}
		CalculadorHijo hijo = new CalculadorHijo();
		hijo.calcularDoble(args[0]); // Recoge el primer argumento enviado por el padre
	}
}