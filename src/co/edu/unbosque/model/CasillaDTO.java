package co.edu.unbosque.model;

/**
 * DTO que representa una casilla del tablero del juego Escaleras y Serpientes.
 * Cada casilla tiene un numero unico del 1 al 100 y un tipo que determina
 * el comportamiento especial que puede tener dentro del juego.
 *
 * @author Estudiante
 * @version 1.0
 */
public class CasillaDTO {

    /**
     * Enumeracion de los tipos posibles de una casilla del tablero.
     */
    public enum TipoCasilla {
        /** Casilla sin efecto especial. */
        NORMAL,
        /** Casilla donde una serpiente baja al jugador. */
        SERPIENTE,
        /** Casilla donde una escalera sube al jugador. */
        ESCALERA,
        /** Casilla con efecto especial de comodin. */
        COMODIN
    }

    /** Numero de la casilla (1 a 100). */
    private int numeroCasilla;

    /** Tipo de la casilla. */
    private TipoCasilla tipo;

    /**
     * Constructor de CasillaDTO.
     *
     * @param numeroCasilla Numero de la casilla en el tablero (1 a 100).
     * @param tipo          Tipo de casilla segun el enumerado TipoCasilla.
     */
    public CasillaDTO(int numeroCasilla, TipoCasilla tipo) {
        this.numeroCasilla = numeroCasilla;
        this.tipo = tipo;
    }

    /**
     * Obtiene el numero de la casilla.
     *
     * @return El numero de la casilla (1 a 100).
     */
    public int getNumeroCasilla() {
        return numeroCasilla;
    }

    /**
     * Establece el numero de la casilla.
     *
     * @param numeroCasilla El nuevo numero de la casilla.
     */
    public void setNumeroCasilla(int numeroCasilla) {
        this.numeroCasilla = numeroCasilla;
    }

    /**
     * Obtiene el tipo de la casilla.
     *
     * @return El tipo de casilla.
     */
    public TipoCasilla getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de la casilla.
     *
     * @param tipo El nuevo tipo de casilla.
     */
    public void setTipo(TipoCasilla tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Casilla[" + numeroCasilla + ", " + tipo + "]";
    }
}
