package co.edu.unbosque.model.persistencie;

import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.MovimientoDTO;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;
import co.edu.unbosque.utils.structure.QueueImpl;
import co.edu.unbosque.utils.structure.StackImpl;

/**
 * Gestor de turnos de la partida de Escaleras y Serpientes.
 *
 * <p>Administra el orden de los jugadores usando una cola FIFO
 * ({@link QueueImpl}) y mantiene el historial de movimientos
 * de cada jugador en su propia pila LIFO ({@link StackImpl}),
 * almacenadas en una {@link MyLinkedList} indexada por orden de inscripcion.</p>
 *
 * <p>Mecanica de turnos:</p>
 * <ol>
 *   <li>Al iniciar la partida, todos los jugadores se encolan en orden.</li>
 *   <li>En cada turno se extrae el primero de la cola (jugador activo).</li>
 *   <li>Tras jugar, el jugador se reencola al final (si no gano).</li>
 *   <li>Si tiene doble turno, se reinserta al frente de la cola.</li>
 * </ol>
 *
 * <p>No se usan ciclos (for/while/do-while). Todos los recorridos son recursivos.</p>
 *
 * @author Estudiante
 * @version 1.0
 */
public class GestorTurnos {

    /**
     * Cola FIFO con los jugadores en orden de turno.
     * El primero en la cola es el jugador que debe jugar.
     */
    private QueueImpl<JugadorDTO> colaTurnos;

    /**
     * Lista de pilas de movimientos, una por jugador.
     * El indice en la lista corresponde al orden de inscripcion del jugador.
     * El indice 0 es el primer jugador inscrito, etc.
     */
    private MyLinkedList<StackImpl<MovimientoDTO>> pilasMovimientos;

    /**
     * Lista de jugadores en orden de inscripcion, usada para localizar
     * la pila correspondiente a cada jugador por nombre.
     */
    private MyLinkedList<JugadorDTO> ordenInscripcion;

    /**
     * Constructor de GestorTurnos.
     * Inicializa las estructuras internas vacias.
     */
    public GestorTurnos() {
        this.colaTurnos = new QueueImpl<JugadorDTO>();
        this.pilasMovimientos = new MyLinkedList<StackImpl<MovimientoDTO>>();
        this.ordenInscripcion = new MyLinkedList<JugadorDTO>();
    }

    /**
     * Inicializa la cola de turnos con todos los jugadores de la partida.
     * Tambien crea una pila de movimientos vacia para cada jugador.
     * Los jugadores se encolan y se registran en el mismo orden en que
     * aparecen en la lista (orden de inscripcion).
     *
     * <p>Este metodo debe llamarse una sola vez antes de iniciar la partida.</p>
     *
     * @param jugadores Lista enlazada con todos los jugadores a registrar.
     */
    public void inicializarTurnos(MyLinkedList<JugadorDTO> jugadores) {
        inicializarRecursivo(jugadores.getFirst());
    }

    /**
     * Retorna el jugador que debe jugar en el turno actual (el primero de la cola)
     * sin desencolar. Si la cola esta vacia, retorna null.
     *
     * @return El JugadorDTO del jugador activo, o null si no hay jugadores.
     */
    public JugadorDTO obtenerJugadorActual() {
        if (colaTurnos.size() == 0) {
            return null;
        }
        // Para hacer peek sin desencolar: dequeue y enqueue al frente
        JugadorDTO jugador = colaTurnos.dequeue();
        reinsertarAlFrente(jugador);
        return jugador;
    }

    /**
     * Avanza al siguiente turno: desencola al jugador actual y lo reencola
     * al final de la cola, siempre que no haya ganado la partida.
     * Si el jugador gano (posicion == 100), simplemente se descarta.
     *
     * @return El jugador que acaba de jugar su turno.
     */
    public JugadorDTO avanzarTurno() {
        if (colaTurnos.size() == 0) {
            return null;
        }
        JugadorDTO jugador = colaTurnos.dequeue();
        if (jugador.getPosicionActual() != 100) {
            colaTurnos.enqueue(jugador);
        }
        return jugador;
    }

    /**
     * Maneja el doble turno de un jugador: lo extrae de la cola y lo
     * reinserta al frente para que juegue de nuevo inmediatamente.
     * Luego desactiva el flag de doble turno en el jugador.
     *
     * <p>Debe llamarse justo despues de que el jugador termino su turno
     * normal si {@link JugadorDTO#isTieneDobleTurno()} retorna true.</p>
     *
     * @param jugador El jugador que tiene doble turno activo.
     */
    public void manejarDobleTurno(JugadorDTO jugador) {
        jugador.setTieneDobleTurno(false);
        reinsertarAlFrente(jugador);
    }

    /**
     * Registra un movimiento en la pila personal del jugador.
     * La pila guarda el historial individual de movimientos de cada jugador,
     * con el mas reciente en el tope (LIFO).
     *
     * @param jugador    El jugador que realizo el movimiento.
     * @param movimiento El MovimientoDTO a registrar en la pila del jugador.
     */
    public void registrarMovimientoEnPila(JugadorDTO jugador, MovimientoDTO movimiento) {
        StackImpl<MovimientoDTO> pilaJugador = buscarPilaJugador(
                ordenInscripcion.getFirst(), pilasMovimientos.getFirst(), jugador.getNombre());
        if (pilaJugador != null) {
            pilaJugador.push(movimiento);
        }
    }

    /**
     * Obtiene el ultimo movimiento registrado en la pila personal del jugador
     * (tope de la pila) sin extraerlo. Si la pila esta vacia, retorna null.
     *
     * @param jugador El jugador cuyo ultimo movimiento se quiere consultar.
     * @return El MovimientoDTO mas reciente del jugador, o null si no hay movimientos.
     */
    public MovimientoDTO obtenerUltimoMovimiento(JugadorDTO jugador) {
        StackImpl<MovimientoDTO> pilaJugador = buscarPilaJugador(
                ordenInscripcion.getFirst(), pilasMovimientos.getFirst(), jugador.getNombre());
        if (pilaJugador == null || pilaJugador.size() == 0) {
            return null;
        }
        // Peek: pop y volver a push
        MovimientoDTO ultimo = pilaJugador.pop();
        pilaJugador.push(ultimo);
        return ultimo;
    }

    /**
     * Cuenta cuantos jugadores hay actualmente en la cola de turnos.
     * El conteo es recursivo usando una cola auxiliar para preservar el orden.
     *
     * @return Numero de jugadores activos en la cola.
     */
    public int cantidadJugadoresActivos() {
        return colaTurnos.size();
    }

    /**
     * Elimina de la cola al jugador que gano la partida.
     * Recorre la cola recursivamente usando una cola auxiliar,
     * reinsertando todos los jugadores excepto el ganador.
     *
     * @param jugadorGanador El jugador que gano y debe salir de la cola.
     */
    public void eliminarJugadorGanador(JugadorDTO jugadorGanador) {
        int cantidad = colaTurnos.size();
        eliminarGanadorRecursivo(jugadorGanador.getNombre(), cantidad);
    }

    // =========================================================================
    // METODOS RECURSIVOS PRIVADOS
    // =========================================================================

    /**
     * Inicializa recursivamente la cola de turnos y las pilas de movimientos.
     * Recorre la lista de jugadores nodo a nodo, encolando cada jugador y
     * creando su pila individual de movimientos.
     *
     * @param nodo Nodo actual de la lista de jugadores.
     */
    private void inicializarRecursivo(Node<JugadorDTO> nodo) {
        if (nodo == null) {
            return;
        }
        JugadorDTO jugador = nodo.getInfo();
        colaTurnos.enqueue(jugador);
        pilasMovimientos.addLast(new StackImpl<MovimientoDTO>());
        ordenInscripcion.addLast(jugador);
        inicializarRecursivo(nodo.getNext());
    }

    /**
     * Reinsertar un jugador al frente de la cola de turnos.
     * Dado que {@link QueueImpl} solo permite encolar al final, se usa
     * una cola auxiliar: se desencolan todos, se inserta el jugador al frente
     * y se reencolan los demas detras de el.
     *
     * @param jugador El jugador a insertar al frente de la cola.
     */
    private void reinsertarAlFrente(JugadorDTO jugador) {
        int cantidad = colaTurnos.size();
        // Extraer todos los jugadores actuales en una cola auxiliar
        QueueImpl<JugadorDTO> colaAuxiliar = new QueueImpl<JugadorDTO>();
        extraerTodosRecursivo(cantidad, colaAuxiliar);
        // Encolar primero al jugador que va al frente
        colaTurnos.enqueue(jugador);
        // Reencolar los demas detras
        reencolarRecursivo(colaAuxiliar.size(), colaAuxiliar);
    }

    /**
     * Extrae recursivamente todos los jugadores de la cola principal
     * y los encola en la cola auxiliar, preservando el orden original.
     *
     * @param restantes    Cantidad de jugadores que quedan por extraer.
     * @param colaAuxiliar Cola auxiliar donde se guardan los jugadores extraidos.
     */
    private void extraerTodosRecursivo(int restantes, QueueImpl<JugadorDTO> colaAuxiliar) {
        if (restantes == 0) {
            return;
        }
        colaAuxiliar.enqueue(colaTurnos.dequeue());
        extraerTodosRecursivo(restantes - 1, colaAuxiliar);
    }

    /**
     * Reencola recursivamente todos los jugadores de la cola auxiliar
     * de vuelta a la cola principal de turnos.
     *
     * @param restantes    Cantidad de jugadores que quedan por reencoalr.
     * @param colaAuxiliar Cola auxiliar con los jugadores a trasladar.
     */
    private void reencolarRecursivo(int restantes, QueueImpl<JugadorDTO> colaAuxiliar) {
        if (restantes == 0) {
            return;
        }
        colaTurnos.enqueue(colaAuxiliar.dequeue());
        reencolarRecursivo(restantes - 1, colaAuxiliar);
    }

    /**
     * Busca recursivamente la pila de movimientos del jugador con el nombre dado.
     * Recorre en paralelo la lista de jugadores inscritos y la lista de pilas,
     * comparando nombres para encontrar la pila correspondiente.
     *
     * @param nodoJugador Nodo actual de la lista de jugadores inscritos.
     * @param nodoPila    Nodo actual de la lista de pilas.
     * @param nombre      Nombre del jugador cuya pila se busca.
     * @return La {@link StackImpl} del jugador, o null si no se encontro.
     */
    private StackImpl<MovimientoDTO> buscarPilaJugador(
            Node<JugadorDTO> nodoJugador,
            Node<StackImpl<MovimientoDTO>> nodoPila,
            String nombre) {
        if (nodoJugador == null || nodoPila == null) {
            return null;
        }
        if (nodoJugador.getInfo().getNombre().equals(nombre)) {
            return nodoPila.getInfo();
        }
        return buscarPilaJugador(nodoJugador.getNext(), nodoPila.getNext(), nombre);
    }

    /**
     * Elimina recursivamente de la cola al jugador ganador.
     * Desencola jugador por jugador; si el nombre no coincide con el ganador,
     * lo reencola al final. Si coincide, lo descarta.
     *
     * @param nombreGanador Nombre del jugador ganador a eliminar.
     * @param restantes     Cantidad de jugadores que quedan por evaluar.
     */
    private void eliminarGanadorRecursivo(String nombreGanador, int restantes) {
        if (restantes == 0) {
            return;
        }
        JugadorDTO jugador = colaTurnos.dequeue();
        if (!jugador.getNombre().equals(nombreGanador)) {
            colaTurnos.enqueue(jugador);
        }
        eliminarGanadorRecursivo(nombreGanador, restantes - 1);
    }
}
