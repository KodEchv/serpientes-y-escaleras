package co.edu.unbosque.model;

/**
 * DTO que representa una escalera en el tablero del juego.
 * Una escalera conecta una casilla base (posicion baja) con una casilla cima
 * (posicion alta), haciendo que el jugador avance al caer en la base.
 *
 */
public class EscaleraDTO {

    /** Posicion de la base de la escalera (casilla donde cae el jugador). */
    private int posicionBase;

    /** Posicion de la cima de la escalera (casilla a la que sube el jugador). */
    private int posicionCima;

    /**
     * Constructor de EscaleraDTO.
     *
     * @param posicionBase Casilla de la base (debe ser menor que posicionCima).
     * @param posicionCima Casilla de la cima (destino del jugador al subir).
     */
    public EscaleraDTO(int posicionBase, int posicionCima) {
        this.posicionBase = posicionBase;
        this.posicionCima = posicionCima;
    }

    /**
     * Obtiene la posicion de la base de la escalera.
     *
     * @return Numero de casilla donde esta la base.
     */
    public int getPosicionBase() {
        return posicionBase;
    }

    /**
     * Establece la posicion de la base de la escalera.
     *
     * @param posicionBase Nueva posicion de la base.
     */
    public void setPosicionBase(int posicionBase) {
        this.posicionBase = posicionBase;
    }

    /**
     * Obtiene la posicion de la cima de la escalera.
     *
     * @return Numero de casilla donde esta la cima.
     */
    public int getPosicionCima() {
        return posicionCima;
    }

    /**
     * Establece la posicion de la cima de la escalera.
     *
     * @param posicionCima Nueva posicion de la cima.
     */
    public void setPosicionCima(int posicionCima) {
        this.posicionCima = posicionCima;
    }

    @Override
    public String toString() {
        return "Escalera[base=" + posicionBase + ", cima=" + posicionCima + "]";
    }
}
