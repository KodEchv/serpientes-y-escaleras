package co.edu.unbosque.utils.exception;

/**
 * Se lanza cuando una posicion es invalida en el tablero (fuera del rango 1-100)
 * o cuando la generacion aleatoria no puede colocar un elemento sin conflictos.
 */
public class PosicionInvalidaException extends Exception {

    private static final long serialVersionUID = 2L;

    public PosicionInvalidaException() {
        super("La posicion es invalida: fuera del rango permitido o no se pudo colocar el elemento.");
    }
}
