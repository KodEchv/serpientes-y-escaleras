package co.edu.unbosque.utils.exception;

/**
 * Se lanza cuando se intenta operar sobre una estructura de datos vacia:
 * grafo sin vertices, lista sin elementos, tablero no construido, etc.
 */
public class EstructuraVaciaException extends Exception {

    private static final long serialVersionUID = 1L;

    public EstructuraVaciaException() {
        super("No se puede operar: la estructura esta vacia o no ha sido inicializada.");
    }
}
