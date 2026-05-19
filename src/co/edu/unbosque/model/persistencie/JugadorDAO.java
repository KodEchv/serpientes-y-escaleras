package co.edu.unbosque.model.persistencie;

import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.utils.structure.MyLinkedList;

/**
 * Interfaz DAO que define las operaciones sobre la coleccion de jugadores
 * de una partida de Escaleras y Serpientes.
 *
 * <p>Las implementaciones deben usar estructuras de datos propias del proyecto
 * y respetar la restriccion de no usar ciclos (for/while/do-while).</p>
 *
 * @author Estudiante
 * @version 1.0
 * @see JugadorDAOImpl
 */
public interface JugadorDAO {

    /**
     * Agrega un jugador a la coleccion de jugadores de la partida.
     *
     * @param jugador El JugadorDTO a agregar. No debe ser null.
     */
    void agregarJugador(JugadorDTO jugador);

    /**
     * Busca un jugador por su nombre en la coleccion.
     *
     * @param nombre Nombre del jugador a buscar.
     * @return El JugadorDTO encontrado, o null si no existe.
     */
    JugadorDTO buscarJugador(String nombre);

    /**
     * Obtiene el primer jugador que se encuentre en la casilla indicada.
     * Util para detectar colisiones entre jugadores.
     *
     * @param posicion Numero de casilla donde se busca un jugador (1 a 100).
     * @return El JugadorDTO que esta en esa casilla, o null si ninguno esta ahi.
     */
    JugadorDTO obtenerJugadorEnPosicion(int posicion);

    /**
     * Elimina un jugador de la coleccion por su nombre.
     * Si el jugador no existe, no hace nada.
     *
     * @param nombre Nombre del jugador a eliminar.
     */
    void eliminarJugador(String nombre);

    /**
     * Retorna la cantidad de jugadores registrados en la coleccion.
     *
     * @return Numero de jugadores activos.
     */
    int cantidadJugadores();

    /**
     * Verifica si la coleccion de jugadores esta vacia.
     *
     * @return true si no hay jugadores registrados, false en caso contrario.
     */
    boolean estaVacia();

    /**
     * Retorna la lista enlazada completa de todos los jugadores registrados.
     * Se usa para iterar recursivamente sobre los jugadores en otras capas.
     *
     * @return La {@link MyLinkedList} interna con todos los jugadores.
     */
    MyLinkedList<JugadorDTO> obtenerTodos();
}
