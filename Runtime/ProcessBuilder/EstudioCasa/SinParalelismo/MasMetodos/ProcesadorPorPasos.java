package ProcessBuilder.EstudioCasa.SinParalelismo.MasMetodos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcesadorPorPasos {
    private static final Logger logger = LogManager.getLogger(ProcesadorPorPasos.class);

    public static void main(String[] args) {
        logger.info("HIJO: Arrancando el proceso...");

        // El hijo ejecuta sus métodos de forma secuencial (uno tras otro)
        hacerPasoUno();
        hacerPasoDos();
        hacerPasoTres();

        logger.info("HIJO: ¡He terminado todos los pasos!");
    }

    public static void hacerPasoUno() {
        logger.info("HIJO -> Ejecutando Método 1: Cargando configuración...");
        // Simulamos trabajo
    }

    public static void hacerPasoDos() {
        logger.info("HIJO -> Ejecutando Método 2: Procesando datos internos...");
        // Simulamos trabajo
    }

    public static void hacerPasoTres() {
        logger.info("HIJO -> Ejecutando Método 3: Guardando resultados...");
        // Simulamos trabajo
    }
}