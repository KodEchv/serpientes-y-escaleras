package co.edu.unbosque.utils.exception;

/**
 * Excepcion que se lanza cuando se intenta realizar una accion de juego
 * (mover jugador, lanzar dado, etc.) sin que la partida haya sido
 * inicializada correctamente.
 *
 * @author Estudiante
 * @version 1.0
 */
public class PartidaNoIniciadaException extends Exception {

    /** Serial version para serializacion. */
    private static final long serialVersionUID = 3L;

    /**
     * Constructor con mensaje de error.
     *
     * @param mensaje Descripcion del error.
     */
    public PartidaNoIniciadaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa raiz.
     *
     * @param mensaje Descripcion del error.
     * @param causa   Excepcion que origino este error.
     */
    public PartidaNoIniciadaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
