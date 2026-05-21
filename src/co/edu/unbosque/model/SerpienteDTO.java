package co.edu.unbosque.model;

/**
 * DTO que representa una serpiente en el tablero del juego.
 * Una serpiente conecta una casilla de cabeza (posicion alta) con una casilla
 * de cola (posicion baja), haciendo que el jugador retroceda al caer en la cabeza.
 *
 */
public class SerpienteDTO {

    /** Posicion de la cabeza de la serpiente (casilla donde cae el jugador). */
    private int posicionCabeza;

    /** Posicion de la cola de la serpiente (casilla a la que va el jugador). */
    private int posicionCola;

    /**
     * Constructor de SerpienteDTO.
     *
     * @param posicionCabeza Casilla de la cabeza (debe ser mayor que posicionCola).
     * @param posicionCola   Casilla de la cola (destino del jugador al caer).
     */
    public SerpienteDTO(int posicionCabeza, int posicionCola) {
        this.posicionCabeza = posicionCabeza;
        this.posicionCola = posicionCola;
    }

    /**
     * Obtiene la posicion de la cabeza de la serpiente.
     *
     * @return Numero de casilla donde esta la cabeza.
     */
    public int getPosicionCabeza() {
        return posicionCabeza;
    }

    /**
     * Establece la posicion de la cabeza de la serpiente.
     *
     * @param posicionCabeza Nueva posicion de la cabeza.
     */
    public void setPosicionCabeza(int posicionCabeza) {
        this.posicionCabeza = posicionCabeza;
    }

    /**
     * Obtiene la posicion de la cola de la serpiente.
     *
     * @return Numero de casilla donde esta la cola.
     */
    public int getPosicionCola() {
        return posicionCola;
    }

    /**
     * Establece la posicion de la cola de la serpiente.
     *
     * @param posicionCola Nueva posicion de la cola.
     */
    public void setPosicionCola(int posicionCola) {
        this.posicionCola = posicionCola;
    }

    @Override
    public String toString() {
        return "Serpiente[cabeza=" + posicionCabeza + ", cola=" + posicionCola + "]";
    }
}
