package co.edu.unbosque.model;

/**
 * DTO que representa un comodin en el tablero del juego Escaleras y Serpientes.
 *
 * <p>Un comodin es una casilla especial cuyo efecto <b>no esta predefinido</b>.
 * Cuando un jugador cae sobre una casilla comodin, debe lanzar un segundo dado
 * (valor 1 a 6) que determina en ese instante cual de los seis efectos se activa.
 * La resolucion del efecto es responsabilidad de {@code GestorComodines}.</p>
 *
 * <p>Efectos segun el valor del dado comodin:</p>
 * <ul>
 *   <li>1 - Mover a la base de la escalera mas cercana (adelante o atras).</li>
 *   <li>2 - Mover a la cabeza de la serpiente mas cercana (adelante o atras).</li>
 *   <li>3 - Avanzar 10 casillas (maximo casilla 100).</li>
 *   <li>4 - Retroceder 10 casillas (minimo casilla 1).</li>
 *   <li>5 - Avanzar al doble de la posicion actual (maximo casilla 100).</li>
 *   <li>6 - Retroceder a la mitad de la posicion actual, redondeado abajo (minimo casilla 1).</li>
 * </ul>
 *
 * <p>Esta clase solo almacena la posicion de la casilla comodin.
 * No tiene tipo, no tiene posicion destino precalculada ni peso de arista,
 * ya que todos esos calculos ocurren en tiempo de ejecucion.</p>
 *
 */
public class ComodinDTO implements java.io.Serializable {

    private static final long serialVersionUID = 2L;

    /** Numero de casilla donde esta ubicado el comodin (entre 2 y 99). */
    private int posicion;

    /**
     * Constructor de ComodinDTO.
     *
     * @param posicion Numero de casilla donde se encuentra el comodin (2 a 99).
     */
    public ComodinDTO(int posicion) {
        this.posicion = posicion;
    }

    /**
     * Obtiene la posicion de la casilla comodin en el tablero.
     *
     * @return Numero de casilla del comodin.
     */
    public int getPosicion() {
        return posicion;
    }

    /**
     * Establece la posicion de la casilla comodin en el tablero.
     *
     * @param posicion Nueva posicion del comodin.
     */
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return "Comodin[pos=" + posicion + "]";
    }
}
