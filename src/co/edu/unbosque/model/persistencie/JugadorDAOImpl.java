package co.edu.unbosque.model.persistencie;

import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;

/**
 * Implementacion concreta de {@link JugadorDAO} que usa una
 * {@link MyLinkedList} como estructura interna de almacenamiento.
 *
 * <p>Todos los recorridos se realizan mediante metodos recursivos privados
 * que reciben un {@link Node} como parametro. No se usan ciclos (for/while/do-while)
 * en ninguna operacion de esta clase.</p>
 *
 * @author Estudiante
 * @version 1.0
 * @see JugadorDAO
 */
public class JugadorDAOImpl implements JugadorDAO {

    /** Lista enlazada interna que almacena los jugadores de la partida. */
    private MyLinkedList<JugadorDTO> listaJugadores;

    /**
     * Constructor de JugadorDAOImpl.
     * Inicializa la lista enlazada vacia.
     */
    public JugadorDAOImpl() {
        this.listaJugadores = new MyLinkedList<JugadorDTO>();
    }

    /**
     * {@inheritDoc}
     * Agrega el jugador al final de la lista para respetar el orden de inscripcion.
     */
    @Override
    public void agregarJugador(JugadorDTO jugador) {
        listaJugadores.addLast(jugador);
    }

    /**
     * {@inheritDoc}
     * Delega la busqueda al metodo recursivo interno.
     */
    @Override
    public JugadorDTO buscarJugador(String nombre) {
        return buscarPorNombreRecursivo(listaJugadores.getFirst(), nombre);
    }

    /**
     * {@inheritDoc}
     * Delega la busqueda al metodo recursivo interno.
     */
    @Override
    public JugadorDTO obtenerJugadorEnPosicion(int posicion) {
        return buscarEnPosicionRecursivo(listaJugadores.getFirst(), posicion);
    }

    /**
     * {@inheritDoc}
     * Localiza el nodo anterior al del jugador y lo elimina de forma recursiva.
     */
    @Override
    public void eliminarJugador(String nombre) {
        if (listaJugadores.isEmpty()) {
            return;
        }
        // Caso especial: el primer nodo es el que se debe eliminar
        if (listaJugadores.getFirst().getInfo().getNombre().equals(nombre)) {
            listaJugadores.extract();
            return;
        }
        eliminarRecursivo(listaJugadores.getFirst(), listaJugadores.getFirst().getNext(), nombre);
    }

    /**
     * {@inheritDoc}
     * Cuenta los elementos mediante recursion.
     */
    @Override
    public int cantidadJugadores() {
        return contarRecursivo(listaJugadores.getFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean estaVacia() {
        return listaJugadores.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MyLinkedList<JugadorDTO> obtenerTodos() {
        return listaJugadores;
    }

    // =========================================================================
    // METODOS RECURSIVOS PRIVADOS
    // =========================================================================

    /**
     * Busca recursivamente un jugador por nombre recorriendo la lista enlazada.
     * Retorna el JugadorDTO del primer nodo cuyo nombre coincida, o null si
     * llega al final sin encontrarlo.
     *
     * @param nodo   Nodo actual en la recursion.
     * @param nombre Nombre del jugador buscado.
     * @return El JugadorDTO encontrado, o null si no existe.
     */
    private JugadorDTO buscarPorNombreRecursivo(Node<JugadorDTO> nodo, String nombre) {
        if (nodo == null) {
            return null;
        }
        if (nodo.getInfo().getNombre().equals(nombre)) {
            return nodo.getInfo();
        }
        return buscarPorNombreRecursivo(nodo.getNext(), nombre);
    }

    /**
     * Busca recursivamente el primer jugador que este en la casilla indicada.
     * Se usa para detectar colisiones: cuando un jugador llega a una casilla
     * donde ya hay otro jugador.
     *
     * @param nodo     Nodo actual en la recursion.
     * @param posicion Numero de casilla donde se busca un jugador.
     * @return El JugadorDTO encontrado en esa posicion, o null si no hay ninguno.
     */
    private JugadorDTO buscarEnPosicionRecursivo(Node<JugadorDTO> nodo, int posicion) {
        if (nodo == null) {
            return null;
        }
        if (nodo.getInfo().getPosicionActual() == posicion) {
            return nodo.getInfo();
        }
        return buscarEnPosicionRecursivo(nodo.getNext(), posicion);
    }

    /**
     * Elimina recursivamente el nodo que contiene al jugador con el nombre dado.
     * Recibe el nodo anterior y el nodo actual para poder reenlazar la lista.
     * Cuando encuentra el nodo a eliminar, hace que el anterior apunte al siguiente.
     *
     * @param anterior Nodo previo al nodo actual en la lista.
     * @param actual   Nodo actual que se evalua para eliminar.
     * @param nombre   Nombre del jugador a eliminar.
     */
    private void eliminarRecursivo(Node<JugadorDTO> anterior, Node<JugadorDTO> actual, String nombre) {
        if (actual == null) {
            // No se encontro el jugador en la lista
            return;
        }
        if (actual.getInfo().getNombre().equals(nombre)) {
            // Reenlazar: el anterior salta sobre el actual
            anterior.setNext(actual.getNext());
            return;
        }
        eliminarRecursivo(actual, actual.getNext(), nombre);
    }

    /**
     * Cuenta recursivamente la cantidad de nodos en la lista enlazada.
     * Caso base: nodo null retorna 0. Caso recursivo: 1 + contar el resto.
     *
     * @param nodo Nodo actual en la recursion.
     * @return Cantidad de elementos desde este nodo hasta el final.
     */
    private int contarRecursivo(Node<JugadorDTO> nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarRecursivo(nodo.getNext());
    }
}
