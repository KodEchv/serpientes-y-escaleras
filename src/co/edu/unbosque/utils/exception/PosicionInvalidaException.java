package co.edu.unbosque.utils.exception;

/**
 * Excepcion que se lanza cuando se intenta acceder o asignar una posicion
 * que no es valida dentro del tablero (menor a 1 o mayor a 100) o cuando
 * una restriccion de generacion no se puede cumplir (ej: base >= cima).
 *
 * @author Estudiante
 * @version 1.0
 */
public class PosicionInvalidaException extends Exception {

    /** Serial version para serializacion. */
    private static final long serialVersionUID = 2L;

    /**
     * Constructor con mensaje de error.
     *
     * @param mensaje Descripcion del error de posicion.
     */
    public PosicionInvalidaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raiz.
     *
     * @param mensaje Descripcion del error.
     * @param causa   Excepcion que origino este error.
     */
    public PosicionInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
