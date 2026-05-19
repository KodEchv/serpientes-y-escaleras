package co.edu.unbosque.model;

/**
 * DTO que representa un movimiento realizado por un jugador durante la partida.
 * Se usa para registrar el historial de movimientos del juego.
 *
 * @author Estudiante
 * @version 1.0
 */
public class MovimientoDTO {

    /** Nombre del jugador que realizo el movimiento. */
    private String nombreJugador;

    /** Valor del dado lanzado. */
    private int dado;

    /** Posicion del jugador antes del movimiento. */
    private int posAntes;

    /** Posicion del jugador despues del movimiento (incluyendo efectos). */
    private int posDespues;

    /** Descripcion del evento ocurrido (NORMAL, SERPIENTE, ESCALERA, COMODIN, REBOTE). */
    private String evento;

    /**
     * Constructor de MovimientoDTO.
     *
     * @param nombreJugador Nombre del jugador.
     * @param dado          Valor del dado lanzado.
     * @param posAntes      Posicion antes del movimiento.
     * @param posDespues    Posicion despues del movimiento.
     * @param evento        Descripcion del evento.
     */
    public MovimientoDTO(String nombreJugador, int dado, int posAntes, int posDespues, String evento) {
        this.nombreJugador = nombreJugador;
        this.dado = dado;
        this.posAntes = posAntes;
        this.posDespues = posDespues;
        this.evento = evento;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return Nombre del jugador.
     */
    public String getNombreJugador() {
        return nombreJugador;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param nombreJugador Nuevo nombre.
     */
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    /**
     * Obtiene el valor del dado lanzado.
     *
     * @return Valor del dado (1 a 6).
     */
    public int getDado() {
        return dado;
    }

    /**
     * Establece el valor del dado.
     *
     * @param dado Nuevo valor del dado.
     */
    public void setDado(int dado) {
        this.dado = dado;
    }

    /**
     * Obtiene la posicion antes del movimiento.
     *
     * @return Posicion anterior.
     */
    public int getPosAntes() {
        return posAntes;
    }

    /**
     * Establece la posicion antes del movimiento.
     *
     * @param posAntes Nueva posicion anterior.
     */
    public void setPosAntes(int posAntes) {
        this.posAntes = posAntes;
    }

    /**
     * Obtiene la posicion despues del movimiento.
     *
     * @return Posicion resultante.
     */
    public int getPosDespues() {
        return posDespues;
    }

    /**
     * Establece la posicion despues del movimiento.
     *
     * @param posDespues Nueva posicion resultante.
     */
    public void setPosDespues(int posDespues) {
        this.posDespues = posDespues;
    }

    /**
     * Obtiene la descripcion del evento del movimiento.
     *
     * @return Descripcion del evento.
     */
    public String getEvento() {
        return evento;
    }

    /**
     * Establece la descripcion del evento.
     *
     * @param evento Nueva descripcion del evento.
     */
    public void setEvento(String evento) {
        this.evento = evento;
    }

    @Override
    public String toString() {
        return "Movimiento[" + nombreJugador + " | dado=" + dado
                + " | " + posAntes + "->" + posDespues
                + " | " + evento + "]";
    }
}
