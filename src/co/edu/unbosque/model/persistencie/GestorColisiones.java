package co.edu.unbosque.model.persistencie;

import java.util.Random;

import co.edu.unbosque.model.CasillaDTO;
import co.edu.unbosque.model.CasillaDTO.TipoCasilla;
import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.MovimientoDTO;
import co.edu.unbosque.utils.structure.Edge;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;
import co.edu.unbosque.utils.structure.Vertex;

/**
 * Gestor de colisiones entre jugadores en el tablero de Escaleras y Serpientes.
 *
 * <p>Mecanica completa de colision:</p>
 * <ol>
 *   <li>JugadorA cae en la casilla donde ya esta JugadorB.</li>
 *   <li>Ambos lanzan un dado virtual (1-6).</li>
 *   <li>El que saca el numero menor es el perdedor.</li>
 *   <li>Si empatan, el recien llegado (JugadorA) es el perdedor.</li>
 *   <li>El perdedor busca la serpiente mas cercana hacia atras
 *       (cabeza de serpiente con numero mayor que sea menor a su posicion).</li>
 *   <li>Si existe: el perdedor va a la cola de esa serpiente.</li>
 *   <li>Si NO existe: el perdedor regresa a la casilla 1.</li>
 *   <li>Se registra el evento en el historial.</li>
 * </ol>
 *
 * <p>No se usan ciclos (for/while/do-while). La busqueda de serpiente
 * es completamente recursiva.</p>
 *
 * @author Estudiante
 * @version 1.0
 */
public class GestorColisiones {

    /** Generador de numeros aleatorios para los dados de colision. */
    private Random aleatorio;

    /**
     * Constructor de GestorColisiones.
     * Inicializa el generador de numeros aleatorios.
     */
    public GestorColisiones() {
        this.aleatorio = new Random();
    }

    /**
     * Orquesta la colision completa entre dos jugadores.
     * Determina el perdedor, busca la serpiente mas cercana hacia atras
     * desde la posicion del perdedor y aplica la consecuencia correspondiente.
     * Registra el evento en el historial de movimientos.
     *
     * @param jugadorA  El jugador que llego a la casilla (recien llegado).
     * @param jugadorB  El jugador que ya estaba en la casilla (residente).
     * @param tablero   El TableroDAO para consultar el grafo de casillas.
     * @param historial Lista enlazada donde se registra el evento de colision.
     * @return Descripcion del resultado de la colision como String.
     */
    public String procesarColision(JugadorDTO jugadorA, JugadorDTO jugadorB,
            TableroDAO tablero, MyLinkedList<MovimientoDTO> historial) {

        int dadoA = lanzarDadoColision();
        int dadoB = lanzarDadoColision();

        JugadorDTO perdedor = determinarPerdedor(jugadorA, jugadorB, dadoA, dadoB);

        int posicionPerdedor = perdedor.getPosicionActual();
        int posicionCabeza = buscarSerpienteMasCercanaHaciaAtras(posicionPerdedor - 1, tablero);

        String resultado;

        if (posicionCabeza != -1) {
            // Existe serpiente: ir a la cola
            int posicionCola = obtenerColaDeSerpiente(posicionCabeza, tablero);
            resultado = aplicarConsequenciaPerdedor(perdedor, posicionCabeza, posicionCola, historial);
        } else {
            // No existe serpiente: ir a casilla 1
            resultado = aplicarConsequenciaPerdedor(perdedor, -1, 1, historial);
        }

        return "COLISION [" + jugadorA.getNombre() + "(dado=" + dadoA + ") vs "
                + jugadorB.getNombre() + "(dado=" + dadoB + ")] -> Perdedor: "
                + perdedor.getNombre() + " | " + resultado;
    }

    /**
     * Lanza un dado virtual para resolver la colision.
     *
     * @return Numero aleatorio entre 1 y 6 inclusive.
     */
    public int lanzarDadoColision() {
        return aleatorio.nextInt(6) + 1;
    }

    /**
     * Determina el perdedor de la colision comparando los dados.
     * El perdedor es quien saca el numero menor. En caso de empate,
     * el recien llegado (llegante) es el perdedor.
     *
     * @param llegante      El jugador que acaba de llegar a la casilla (JugadorA).
     * @param residente     El jugador que ya estaba en la casilla (JugadorB).
     * @param dadoLlegante  Resultado del dado del jugador llegante.
     * @param dadoResidente Resultado del dado del jugador residente.
     * @return El JugadorDTO que perdio la colision.
     */
    public JugadorDTO determinarPerdedor(JugadorDTO llegante, JugadorDTO residente,
            int dadoLlegante, int dadoResidente) {
        if (dadoLlegante <= dadoResidente) {
            // Empate o llegante saca menos: llegante pierde
            return llegante;
        }
        return residente;
    }

    /**
     * Busca la cabeza de serpiente mas cercana hacia atras desde la posicion dada.
     * Recorre recursivamente las casillas desde {@code posicion} hasta 1,
     * verificando si cada casilla es de tipo SERPIENTE en el grafo.
     * Retorna el numero de la primera casilla SERPIENTE encontrada, o -1 si no hay.
     *
     * @param posicion Casilla desde la cual se empieza a buscar hacia atras.
     *                 La primera llamada debe ser {@code posicionPerdedor - 1}.
     * @param tablero  El TableroDAO para consultar el tipo de cada casilla.
     * @return Numero de casilla donde esta la cabeza de la serpiente mas cercana,
     *         o -1 si no existe ninguna serpiente entre la posicion y la casilla 1.
     */
    public int buscarSerpienteMasCercanaHaciaAtras(int posicion, TableroDAO tablero) {
        return buscarSerpienteRecursivo(posicion, tablero);
    }

    /**
     * Aplica la consecuencia al jugador perdedor de la colision.
     * Si {@code posicionSerpienteCabeza} es -1, el perdedor va a la casilla 1.
     * Si existe la serpiente, el perdedor va a {@code posicionSerpienteCola}.
     * Registra el movimiento en el historial.
     *
     * @param perdedor              El jugador que perdio la colision.
     * @param posicionSerpienteCabeza Casilla de la cabeza de la serpiente (-1 si no hay).
     * @param posicionSerpienteCola   Casilla de destino: cola de serpiente o 1.
     * @param historial             Lista donde se registra el evento.
     * @return Descripcion de la consecuencia aplicada.
     */
    public String aplicarConsequenciaPerdedor(JugadorDTO perdedor,
            int posicionSerpienteCabeza, int posicionSerpienteCola,
            MyLinkedList<MovimientoDTO> historial) {

        int posAntes = perdedor.getPosicionActual();
        String descripcion;

        if (posicionSerpienteCabeza == -1) {
            // No habia serpiente: ir al inicio
            perdedor.setPosicionActual(1);
            descripcion = "sin serpiente disponible, regresa a casilla 1";
        } else {
            // Hay serpiente: ir a la cola
            perdedor.setPosicionActual(posicionSerpienteCola);
            descripcion = "serpiente en casilla " + posicionSerpienteCabeza
                    + ", baja a casilla " + posicionSerpienteCola;
        }

        String evento = "COLISION-PERDEDOR: " + perdedor.getNombre() + " " + descripcion;

        MovimientoDTO movimiento = new MovimientoDTO(
                perdedor.getNombre(),
                0,
                posAntes,
                perdedor.getPosicionActual(),
                evento
        );
        historial.addLast(movimiento);

        return descripcion;
    }

    // =========================================================================
    // METODOS RECURSIVOS PRIVADOS
    // =========================================================================

    /**
     * Metodo recursivo que busca hacia atras la cabeza de una serpiente.
     * Caso base: {@code posicionActual} menor que 1 — retorna -1 (no encontro serpiente).
     * Caso recursivo: si la casilla actual es SERPIENTE, retorna su numero;
     * si no, llama recursivamente con {@code posicionActual - 1}.
     *
     * @param posicionActual Casilla actual evaluada en la recursion.
     * @param tablero        El TableroDAO para consultar el tipo de casilla.
     * @return Numero de la casilla cabeza de serpiente encontrada, o -1.
     */
    private int buscarSerpienteRecursivo(int posicionActual, TableroDAO tablero) {
        if (posicionActual < 1) {
            return -1;
        }
        Vertex<CasillaDTO> vertice = tablero.buscarVertice(posicionActual);
        if (vertice != null && vertice.getInfo().getTipo() == TipoCasilla.SERPIENTE) {
            return posicionActual;
        }
        return buscarSerpienteRecursivo(posicionActual - 1, tablero);
    }

    /**
     * Obtiene el numero de casilla de la cola de una serpiente dado el numero
     * de casilla de su cabeza. Busca recursivamente en las aristas del vertice
     * la arista de peso 2.0 (arista de serpiente) y retorna su destino.
     *
     * @param posicionCabeza Numero de casilla donde esta la cabeza de la serpiente.
     * @param tablero        El TableroDAO para consultar el grafo.
     * @return Numero de casilla de la cola de la serpiente, o 1 si no se encuentra.
     */
    private int obtenerColaDeSerpiente(int posicionCabeza, TableroDAO tablero) {
        Vertex<CasillaDTO> vertice = tablero.buscarVertice(posicionCabeza);
        if (vertice == null) {
            return 1;
        }
        return buscarAristaSerpienteRecursivo(vertice.getAdyacentEdges().getFirst());
    }

    /**
     * Busca recursivamente en la lista de aristas de un vertice la arista
     * de peso 2.0, que corresponde a la conexion cabeza-cola de una serpiente.
     * Retorna el numero de casilla destino de esa arista.
     *
     * @param nodoArista Nodo actual de la lista de aristas del vertice.
     * @return Numero de casilla de la cola de la serpiente, o 1 si no se encuentra.
     */
    private int buscarAristaSerpienteRecursivo(Node<Edge> nodoArista) {
        if (nodoArista == null) {
            return 1;
        }
        Edge arista = nodoArista.getInfo();
        if (arista.getValue() == 2.0) {
            CasillaDTO destino = (CasillaDTO) arista.getDestination().getInfo();
            return destino.getNumeroCasilla();
        }
        return buscarAristaSerpienteRecursivo(nodoArista.getNext());
    }
}
