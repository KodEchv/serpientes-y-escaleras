package co.edu.unbosque.utils.exception;

/**
 * Se lanza cuando se intenta ejecutar una accion de juego (mover jugador,
 * lanzar dado, consultar estado) sin que la partida haya sido iniciada.
 */
public class PartidaNoIniciadaException extends Exception {

    private static final long serialVersionUID = 3L;

    public PartidaNoIniciadaException() {
        super("No se puede ejecutar esta accion: la partida no ha sido iniciada.");
    }
}
