package co.edu.unbosque.utils.exception;

/**
 * Excepcion que se lanza cuando se intenta operar sobre una estructura
 * de datos que esta vacia (grafo sin vertices, lista sin elementos, etc.).
 *
 * @author Estudiante
 * @version 1.0
 */
public class EstructuraVaciaException extends Exception {

    /** Serial version para serializacion. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor con mensaje de error.
     *
     * @param mensaje Descripcion del error.
     */
    public EstructuraVaciaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raiz.
     *
     * @param mensaje Descripcion del error.
     * @param causa   Excepcion que origino este error.
     */
    public EstructuraVaciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
