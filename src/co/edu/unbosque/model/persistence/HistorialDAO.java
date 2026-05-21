package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.MovimientoDTO;
import co.edu.unbosque.utils.structure.DNode;
import co.edu.unbosque.utils.structure.MyDoubleLinkedList;

/**
 * DAO que gestiona el historial global de movimientos de la partida usando una
 * {@link MyDoubleLinkedList} como estructura interna.
 *
 * <p>La lista doblemente enlazada permite navegar el historial hacia adelante
 * y hacia atras mediante el cursor {@code currentPosition}, lo cual facilita
 * revisar jugadas anteriores o avanzar hacia las mas recientes.</p>
 *
 * <p>Todos los conteos son recursivos. No se usan ciclos (for/while/do-while).</p>
 *
 */
public class HistorialDAO {

    /** Lista doblemente enlazada que almacena todos los movimientos de la partida. */
    private MyDoubleLinkedList<MovimientoDTO> listaHistorial;

    /**
     * Constructor de HistorialDAO.
     * Inicializa la lista doblemente enlazada vacia.
     */
    public HistorialDAO() {
        this.listaHistorial = new MyDoubleLinkedList<MovimientoDTO>();
    }

    /**
     * Registra un nuevo movimiento al final del historial.
     * Usa {@link MyDoubleLinkedList#insert(Object)} para agregar el movimiento
     * despues del cursor actual, avanzando el cursor al nuevo nodo.
     *
     * <p>Si es el primer movimiento, se convierte en la cabeza de la lista.</p>
     *
     * @param movimiento El MovimientoDTO a registrar. No debe ser null.
     */
    public void registrarMovimiento(MovimientoDTO movimiento) {
        listaHistorial.insert(movimiento);
    }

    /**
     * Avanza el cursor del historial la cantidad de pasos indicada.
     * Delega directamente al metodo {@link MyDoubleLinkedList#forward(int)}.
     * Si se intenta avanzar mas alla del ultimo nodo, el cursor se detiene ahi.
     *
     * @param pasos Numero de posiciones a avanzar hacia el final de la lista.
     */
    public void avanzarEnHistorial(int pasos) {
        listaHistorial.forward(pasos);
    }

    /**
     * Retrocede el cursor del historial la cantidad de pasos indicada.
     * Delega directamente al metodo {@link MyDoubleLinkedList#back(int)}.
     * Si se intenta retroceder mas alla del primer nodo, el cursor se detiene ahi.
     *
     * @param pasos Numero de posiciones a retroceder hacia el inicio de la lista.
     */
    public void retrocederEnHistorial(int pasos) {
        listaHistorial.back(pasos);
    }

    /**
     * Retorna el movimiento actualmente apuntado por el cursor de la lista.
     * Si el cursor es null (lista vacia o no inicializada), retorna null.
     *
     * @return El MovimientoDTO en la posicion actual del cursor, o null si no hay.
     */
    public MovimientoDTO obtenerMovimientoActual() {
        DNode<MovimientoDTO> posicionActual = listaHistorial.getCurrentPosition();
        if (posicionActual == null) {
            return null;
        }
        return posicionActual.getInfo();
    }

    /**
     * Imprime por consola el historial completo de movimientos desde el inicio
     * hasta el final de la lista, usando la representacion {@code toString()}
     * recursiva de {@link MyDoubleLinkedList}.
     *
     * <p>Si el historial esta vacio, imprime un mensaje indicandolo.</p>
     */
    public void imprimirHistorialCompleto() {
        if (tamanioHistorial() == 0) {
            System.out.println("El historial esta vacio. Aun no se han registrado movimientos.");
            return;
        }
        System.out.println("=== HISTORIAL DE MOVIMIENTOS ===");
        System.out.println(listaHistorial.toString());
        System.out.println("================================");
    }

    /**
     * Retorna la cantidad total de movimientos registrados en el historial.
     * El conteo se realiza recursivamente desde la cabeza de la lista.
     *
     * @return Numero de movimientos en el historial.
     */
    public int tamanioHistorial() {
        return contarNodosRecursivo(listaHistorial.getHead());
    }

    /**
     * Retorna la lista doblemente enlazada interna.
     * Util para que otras clases puedan acceder directamente a la estructura
     * si necesitan recorrerla de forma personalizada.
     *
     * @return La {@link MyDoubleLinkedList} del historial.
     */
    public MyDoubleLinkedList<MovimientoDTO> getListaHistorial() {
        return listaHistorial;
    }

    // =========================================================================
    // METODOS RECURSIVOS PRIVADOS
    // =========================================================================

    /**
     * Cuenta recursivamente la cantidad de nodos en la lista doblemente enlazada
     * desde el nodo dado hasta el final.
     * Caso base: nodo null retorna 0.
     * Caso recursivo: 1 + contar el siguiente nodo.
     *
     * @param nodo Nodo actual de la lista doblemente enlazada.
     * @return Cantidad de nodos desde este nodo hasta el final de la lista.
     */
    private int contarNodosRecursivo(DNode<MovimientoDTO> nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarNodosRecursivo(nodo.getNext());
    }
}
