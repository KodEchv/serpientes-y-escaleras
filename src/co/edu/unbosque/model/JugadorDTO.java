package co.edu.unbosque.model;

/**
 * DTO que representa un jugador dentro del juego Escaleras y Serpientes.
 * Almacena el estado actual del jugador durante una partida.
 *
 * @author Estudiante
 * @version 1.0
 */
public class JugadorDTO {

    /** Nombre del jugador. */
    private String nombre;

    /** Posicion actual del jugador en el tablero (1 a 100, 0 = no ha iniciado). */
    private int posicionActual;

    /** Indica si el jugador tiene escudo activo (proteccion contra serpientes). */
    private boolean tieneEscudo;

    /** Indica si el jugador tiene doble turno pendiente. */
    private boolean tieneDobleTurno;

    /** Indica si el jugador pierde su proximo turno (por ROBAR_TURNO de otro). */
    private boolean pierdeTurno;

    /** Numero de turnos que ha jugado el jugador. */
    private int cantidadTurnos;

    /**
     * Constructor de JugadorDTO.
     *
     * @param nombre Nombre del jugador.
     */
    public JugadorDTO(String nombre) {
        this.nombre = nombre;
        this.posicionActual = 0;
        this.tieneEscudo = false;
        this.tieneDobleTurno = false;
        this.pierdeTurno = false;
        this.cantidadTurnos = 0;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return Nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param nombre Nuevo nombre del jugador.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la posicion actual del jugador en el tablero.
     *
     * @return Numero de casilla actual (0 si aun no ha iniciado).
     */
    public int getPosicionActual() {
        return posicionActual;
    }

    /**
     * Establece la posicion actual del jugador.
     *
     * @param posicionActual Nueva posicion del jugador.
     */
    public void setPosicionActual(int posicionActual) {
        this.posicionActual = posicionActual;
    }

    /**
     * Indica si el jugador tiene escudo activo.
     *
     * @return true si tiene escudo, false en caso contrario.
     */
    public boolean isTieneEscudo() {
        return tieneEscudo;
    }

    /**
     * Establece el estado del escudo del jugador.
     *
     * @param tieneEscudo true para activar el escudo, false para desactivarlo.
     */
    public void setTieneEscudo(boolean tieneEscudo) {
        this.tieneEscudo = tieneEscudo;
    }

    /**
     * Indica si el jugador tiene doble turno pendiente.
     *
     * @return true si tiene doble turno, false en caso contrario.
     */
    public boolean isTieneDobleTurno() {
        return tieneDobleTurno;
    }

    /**
     * Establece el estado del doble turno del jugador.
     *
     * @param tieneDobleTurno true para activar el doble turno.
     */
    public void setTieneDobleTurno(boolean tieneDobleTurno) {
        this.tieneDobleTurno = tieneDobleTurno;
    }

    /**
     * Indica si el jugador pierde su proximo turno.
     *
     * @return true si pierde el turno, false en caso contrario.
     */
    public boolean isPierdeTurno() {
        return pierdeTurno;
    }

    /**
     * Establece si el jugador pierde su proximo turno.
     *
     * @param pierdeTurno true para hacer que el jugador pierda su turno.
     */
    public void setPierdeTurno(boolean pierdeTurno) {
        this.pierdeTurno = pierdeTurno;
    }

    /**
     * Obtiene la cantidad de turnos jugados.
     *
     * @return Numero de turnos jugados.
     */
    public int getCantidadTurnos() {
        return cantidadTurnos;
    }

    /**
     * Establece la cantidad de turnos jugados.
     *
     * @param cantidadTurnos Nuevo valor de turnos jugados.
     */
    public void setCantidadTurnos(int cantidadTurnos) {
        this.cantidadTurnos = cantidadTurnos;
    }

    /**
     * Incrementa en uno el contador de turnos jugados.
     */
    public void incrementarTurnos() {
        this.cantidadTurnos++;
    }

    @Override
    public String toString() {
        return "Jugador[" + nombre + ", pos=" + posicionActual + ", turnos=" + cantidadTurnos + "]";
    }
}
