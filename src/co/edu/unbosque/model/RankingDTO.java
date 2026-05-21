package co.edu.unbosque.model;

/**
 * DTO que representa la entrada de un jugador en el ranking final de la partida.
 * Implementa {@link Comparable} para ordenar por puntaje: mayor posicion final
 * es mejor; en caso de empate, gana quien uso menos turnos.
 *
 * <p>La clave de comparacion usada en el {@link co.edu.unbosque.utils.structure.AVLTree}
 * es: {@code posicionFinal * 1000 - cantidadTurnos}, de modo que mayor posicion
 * con menos turnos queda mas alto en el arbol.</p>
 *
 */
public class RankingDTO implements Comparable<RankingDTO> {

    /** Nombre del jugador registrado en el ranking. */
    private String nombreJugador;

    /** Posicion final del jugador al terminar la partida (1 a 100). */
    private int posicionFinal;

    /** Cantidad de turnos que uso el jugador durante la partida. */
    private int cantidadTurnos;

    /** Indica si el jugador gano la partida (llego a la casilla 100). */
    private boolean gano;

    /**
     * Constructor de RankingDTO.
     *
     * @param nombreJugador  Nombre del jugador.
     * @param posicionFinal  Posicion final en el tablero al acabar la partida.
     * @param cantidadTurnos Cantidad de turnos jugados.
     * @param gano           true si el jugador llego a la casilla 100 y gano.
     */
    public RankingDTO(String nombreJugador, int posicionFinal, int cantidadTurnos, boolean gano) {
        this.nombreJugador = nombreJugador;
        this.posicionFinal = posicionFinal;
        this.cantidadTurnos = cantidadTurnos;
        this.gano = gano;
    }

    /**
     * Compara este RankingDTO con otro para establecer el orden en el ranking.
     * Un jugador es "mayor" (mejor ranking) si tiene mayor posicion final;
     * en caso de empate, gana quien tiene menos turnos jugados.
     *
     * @param otro El otro RankingDTO con quien comparar.
     * @return Positivo si este jugador es mejor, negativo si es peor, 0 si son iguales.
     */
    @Override
    public int compareTo(RankingDTO otro) {
        int puntuacionPropia = this.posicionFinal * 1000 - this.cantidadTurnos;
        int puntuacionOtro = otro.posicionFinal * 1000 - otro.cantidadTurnos;
        return puntuacionPropia - puntuacionOtro;
    }

    /**
     * Calcula la clave numerica usada para insertar en el AVLTree.
     * Formula: {@code posicionFinal * 1000 - cantidadTurnos}.
     *
     * @return Clave entera para el AVLTree.
     */
    public int calcularClave() {
        return posicionFinal * 1000 - cantidadTurnos;
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
     * @param nombreJugador Nuevo nombre del jugador.
     */
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    /**
     * Obtiene la posicion final del jugador.
     *
     * @return Posicion final en el tablero.
     */
    public int getPosicionFinal() {
        return posicionFinal;
    }

    /**
     * Establece la posicion final del jugador.
     *
     * @param posicionFinal Nueva posicion final.
     */
    public void setPosicionFinal(int posicionFinal) {
        this.posicionFinal = posicionFinal;
    }

    /**
     * Obtiene la cantidad de turnos jugados.
     *
     * @return Cantidad de turnos.
     */
    public int getCantidadTurnos() {
        return cantidadTurnos;
    }

    /**
     * Establece la cantidad de turnos jugados.
     *
     * @param cantidadTurnos Nueva cantidad de turnos.
     */
    public void setCantidadTurnos(int cantidadTurnos) {
        this.cantidadTurnos = cantidadTurnos;
    }

    /**
     * Indica si el jugador gano la partida.
     *
     * @return true si el jugador gano, false en caso contrario.
     */
    public boolean isGano() {
        return gano;
    }

    /**
     * Establece si el jugador gano la partida.
     *
     * @param gano true si el jugador gano.
     */
    public void setGano(boolean gano) {
        this.gano = gano;
    }

    @Override
    public String toString() {
        String estado = gano ? "GANADOR" : "posicion " + posicionFinal;
        return "Ranking[" + nombreJugador + " | " + estado
                + " | turnos=" + cantidadTurnos + "]";
    }
}
