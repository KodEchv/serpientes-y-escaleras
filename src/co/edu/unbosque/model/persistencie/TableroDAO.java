package co.edu.unbosque.model.persistencie;

import co.edu.unbosque.model.CasillaDTO;
import co.edu.unbosque.model.CasillaDTO.TipoCasilla;
import co.edu.unbosque.model.ComodinDTO;
import co.edu.unbosque.model.EscaleraDTO;
import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.MovimientoDTO;
import co.edu.unbosque.model.SerpienteDTO;
import co.edu.unbosque.utils.exception.EstructuraVaciaException;
import co.edu.unbosque.utils.exception.PartidaNoIniciadaException;
import co.edu.unbosque.utils.exception.PosicionInvalidaException;
import co.edu.unbosque.utils.structure.BreadthFirstSearch;
import co.edu.unbosque.utils.structure.Edge;
import co.edu.unbosque.utils.structure.Graph;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;
import co.edu.unbosque.utils.structure.Vertex;

/**
 * DAO que gestiona el tablero del juego Escaleras y Serpientes.
 * Construye y mantiene el grafo de 100 vertices (casillas), maneja la logica
 * de movimiento de jugadores, aplica efectos especiales y detecta el ganador.
 *
 * <p>Estructura del grafo:</p>
 * <ul>
 *   <li>Cada {@link Vertex} contiene un {@link CasillaDTO}.</li>
 *   <li>Aristas normales: casilla N a N+1, peso 1.0.</li>
 *   <li>Aristas de escalera: base a cima, peso 0.5.</li>
 *   <li>Aristas de serpiente: cabeza a cola, peso 2.0.</li>
 *   <li>Aristas de comodin: casilla a destino, peso variable segun tipo.</li>
 * </ul>
 *
 * <p>Todos los recorridos se hacen sin ciclos (for/while); se usa recursividad.</p>
 *
 * @author Estudiante
 * @version 1.0
 */
public class TableroDAO {

    /** Grafo principal del tablero (100 vertices). */
    private Graph tablero;

    /** Generador de elementos aleatorios del tablero. */
    private GeneradorTablero generador;

    /** Historial de movimientos de la partida actual. */
    private MyLinkedList<MovimientoDTO> historialMovimientos;

    /** Indica si el tablero ha sido construido e inicializado. */
    private boolean tableroInicializado;

    /**
     * Constructor de TableroDAO.
     * Crea las estructuras internas pero NO construye el tablero todavia.
     */
    public TableroDAO() {
        this.tablero = new Graph();
        this.generador = new GeneradorTablero();
        this.historialMovimientos = new MyLinkedList<MovimientoDTO>();
        this.tableroInicializado = false;
    }

    // =========================================================================
    // CONSTRUCCION DEL TABLERO
    // =========================================================================

    /**
     * Construye el tablero completo: crea los 100 vertices, genera aleatoriamente
     * serpientes, escaleras y comodines, y conecta todo con aristas.
     * Al finalizar, verifica la conectividad del tablero con BFS desde casilla 1.
     *
     * @throws PosicionInvalidaException Si la generacion aleatoria no puede cumplir
     *                                   las restricciones de posicion.
     * @throws EstructuraVaciaException  Si el grafo queda vacio tras la construccion.
     */
    public void construirTablero() throws PosicionInvalidaException, EstructuraVaciaException {
        // 1. Crear los 100 vertices
        crearVerticesRecursivo(1);

        // 2. Conectar casillas normales: N -> N+1
        conectarCasillasNormales(1);

        // 3. Generar y aplicar serpientes
        MyLinkedList<SerpienteDTO> serpientes = generador.generarSerpientes();
        aplicarSerpientes(serpientes.getFirst());

        // 4. Generar y aplicar escaleras
        MyLinkedList<EscaleraDTO> escaleras = generador.generarEscaleras();
        aplicarEscaleras(escaleras.getFirst());

        // 5. Generar y aplicar comodines
        MyLinkedList<ComodinDTO> comodines = generador.generarComodines();
        aplicarComodines(comodines.getFirst());

        // 6. Verificar conectividad con BFS desde casilla 1 hasta casilla 100
        verificarConectividad();

        tableroInicializado = true;
    }

    /**
     * Crea recursivamente los 100 vertices del tablero y los agrega al grafo.
     * Cada vertice contiene un CasillaDTO con tipo NORMAL inicial.
     *
     * @param numeroCasilla Numero de la casilla actual en la recursion (1 a 100).
     */
    private void crearVerticesRecursivo(int numeroCasilla) {
        if (numeroCasilla > 100) {
            return;
        }
        CasillaDTO casilla = new CasillaDTO(numeroCasilla, TipoCasilla.NORMAL);
        Vertex<CasillaDTO> vertice = new Vertex<CasillaDTO>(casilla);
        tablero.addVertex(vertice);
        crearVerticesRecursivo(numeroCasilla + 1);
    }

    /**
     * Conecta recursivamente cada casilla N con la casilla N+1 mediante una arista
     * de peso 1.0 (movimiento normal). La casilla 100 no tiene arista de salida normal.
     *
     * @param numeroCasilla Numero de la casilla origen actual en la recursion.
     */
    private void conectarCasillasNormales(int numeroCasilla) {
        if (numeroCasilla >= 100) {
            return;
        }
        Vertex<CasillaDTO> origen = buscarVertice(numeroCasilla);
        Vertex<CasillaDTO> destino = buscarVertice(numeroCasilla + 1);
        if (origen != null && destino != null) {
            Edge arista = new Edge(origen, destino, 1.0);
            origen.addEdge(arista);
        }
        conectarCasillasNormales(numeroCasilla + 1);
    }

    /**
     * Aplica recursivamente las serpientes al grafo: marca la casilla de la cabeza
     * con tipo SERPIENTE y agrega una arista de peso 2.0 desde la cabeza hasta la cola.
     * La arista normal (cabeza -> cabeza+1) se mantiene.
     *
     * @param nodoActual Nodo actual de la lista de serpientes.
     */
    private void aplicarSerpientes(Node<SerpienteDTO> nodoActual) {
        if (nodoActual == null) {
            return;
        }
        SerpienteDTO serpiente = nodoActual.getInfo();
        Vertex<CasillaDTO> verticesCabeza = buscarVertice(serpiente.getPosicionCabeza());
        Vertex<CasillaDTO> verticeCola = buscarVertice(serpiente.getPosicionCola());
        if (verticesCabeza != null && verticeCola != null) {
            // Cambiar el tipo de la casilla cabeza
            verticesCabeza.getInfo().setTipo(TipoCasilla.SERPIENTE);
            // Agregar arista especial de serpiente
            Edge aristaSerpiente = new Edge(verticesCabeza, verticeCola, 2.0);
            verticesCabeza.addEdge(aristaSerpiente);
        }
        aplicarSerpientes(nodoActual.getNext());
    }

    /**
     * Aplica recursivamente las escaleras al grafo: marca la casilla de la base
     * con tipo ESCALERA y agrega una arista de peso 0.5 desde la base hasta la cima.
     * La arista normal (base -> base+1) se mantiene.
     *
     * @param nodoActual Nodo actual de la lista de escaleras.
     */
    private void aplicarEscaleras(Node<EscaleraDTO> nodoActual) {
        if (nodoActual == null) {
            return;
        }
        EscaleraDTO escalera = nodoActual.getInfo();
        Vertex<CasillaDTO> verticeBase = buscarVertice(escalera.getPosicionBase());
        Vertex<CasillaDTO> verticeCima = buscarVertice(escalera.getPosicionCima());
        if (verticeBase != null && verticeCima != null) {
            // Cambiar el tipo de la casilla base
            verticeBase.getInfo().setTipo(TipoCasilla.ESCALERA);
            // Agregar arista especial de escalera
            Edge aristaEscalera = new Edge(verticeBase, verticeCima, 0.5);
            verticeBase.addEdge(aristaEscalera);
        }
        aplicarEscaleras(nodoActual.getNext());
    }

    /**
     * Aplica recursivamente los comodines al grafo: unicamente marca la casilla
     * con tipo COMODIN. No se crea ninguna arista especial porque el efecto del
     * comodin se determina en tiempo de ejecucion (dado comodin 1-6) por
     * {@link GestorComodines}. La arista normal N a N+1 permanece intacta.
     *
     * @param nodoActual Nodo actual de la lista de comodines.
     */
    private void aplicarComodines(Node<ComodinDTO> nodoActual) {
        // Caso base: lista agotada
        if (nodoActual == null) {
            return;
        }
        ComodinDTO comodin = nodoActual.getInfo();
        Vertex<CasillaDTO> verticePosicion = buscarVertice(comodin.getPosicion());
        if (verticePosicion != null) {
            // Solo marcar si la casilla aun es NORMAL (la generacion ya evita conflictos,
            // pero esta guarda evita marcados dobles en caso de condicion de borde)
            if (verticePosicion.getInfo().getTipo() == TipoCasilla.NORMAL) {
                verticePosicion.getInfo().setTipo(TipoCasilla.COMODIN);
            }
        }
        aplicarComodines(nodoActual.getNext());
    }

    /**
     * Verifica la conectividad del tablero usando BFS desde la casilla 1 hasta
     * la casilla 100. Si no hay camino, lanza excepcion.
     *
     * @throws EstructuraVaciaException Si el tablero no tiene vertices o no hay
     *                                  camino de la casilla 1 a la 100.
     */
    private void verificarConectividad() throws EstructuraVaciaException {
        if (tablero.getListOfNodes().isEmpty()) {
            throw new EstructuraVaciaException("El tablero no tiene vertices.");
        }
        Vertex<CasillaDTO> inicio = buscarVertice(1);
        Vertex<CasillaDTO> fin = buscarVertice(100);
        if (inicio == null || fin == null) {
            throw new EstructuraVaciaException("No se encontraron los vertices de inicio o fin.");
        }
        BreadthFirstSearch bfs = new BreadthFirstSearch(inicio, fin);
        boolean hayConexion = bfs.runSearch();
        if (!hayConexion) {
            throw new EstructuraVaciaException(
                "El tablero no tiene conectividad desde casilla 1 hasta casilla 100.");
        }
    }

    // =========================================================================
    // BUSQUEDA DE VERTICE POR NUMERO DE CASILLA
    // =========================================================================

    /**
     * Busca y retorna el vertice del grafo que corresponde al numero de casilla dado.
     * El recorrido es recursivo, sin ciclos.
     *
     * @param numeroCasilla Numero de casilla a buscar (1 a 100).
     * @return El Vertex que contiene la CasillaDTO con ese numero, o null si no existe.
     */
    public Vertex<CasillaDTO> buscarVertice(int numeroCasilla) {
        return buscarVerticeRecursivo(tablero.getListOfNodes().getFirst(), numeroCasilla);
    }

    /**
     * Recorre recursivamente la lista de vertices del grafo buscando el que
     * tenga el numero de casilla indicado.
     *
     * @param nodoActual    Nodo actual en la lista de vertices.
     * @param numeroCasilla Numero de casilla buscado.
     * @return El Vertex correspondiente, o null si no se encuentra.
     */
    @SuppressWarnings("unchecked")
    private Vertex<CasillaDTO> buscarVerticeRecursivo(Node<Vertex<?>> nodoActual, int numeroCasilla) {
        if (nodoActual == null) {
            return null;
        }
        Vertex<?> vertice = nodoActual.getInfo();
        if (vertice.getInfo() instanceof CasillaDTO) {
            CasillaDTO casilla = (CasillaDTO) vertice.getInfo();
            if (casilla.getNumeroCasilla() == numeroCasilla) {
                return (Vertex<CasillaDTO>) vertice;
            }
        }
        return buscarVerticeRecursivo(nodoActual.getNext(), numeroCasilla);
    }

    // =========================================================================
    // RECORRIDO RECURSIVO DEL TABLERO
    // =========================================================================

    /**
     * Recorre todo el tablero recursivamente e imprime el estado de cada casilla:
     * su numero, tipo y las aristas que tiene. Al finalizar muestra el conteo
     * de escaleras, serpientes y comodines.
     *
     * @throws EstructuraVaciaException Si el tablero no ha sido construido.
     */
    public void imprimirEstadoTablero() throws EstructuraVaciaException {
        if (!tableroInicializado) {
            throw new EstructuraVaciaException("El tablero no ha sido construido todavia.");
        }
        System.out.println("=== ESTADO DEL TABLERO ===");
        int[] contadores = {0, 0, 0}; // [escaleras, serpientes, comodines]
        recorrerTableroRecursivo(tablero.getListOfNodes().getFirst(), contadores);
        System.out.println("---");
        System.out.println("Escaleras: " + contadores[0]);
        System.out.println("Serpientes: " + contadores[1]);
        System.out.println("Comodines: " + contadores[2]);
        System.out.println("==========================");
    }

    /**
     * Metodo recursivo que recorre la lista de vertices del grafo e imprime
     * el estado de cada casilla, acumulando los contadores por tipo.
     *
     * @param nodoActual Nodo actual en el recorrido.
     * @param contadores Arreglo de 3 enteros: [escaleras, serpientes, comodines].
     */
    private void recorrerTableroRecursivo(Node<Vertex<?>> nodoActual, int[] contadores) {
        if (nodoActual == null) {
            return;
        }
        Vertex<?> vertice = nodoActual.getInfo();
        if (vertice.getInfo() instanceof CasillaDTO) {
            CasillaDTO casilla = (CasillaDTO) vertice.getInfo();
            System.out.print("Casilla " + casilla.getNumeroCasilla()
                    + " [" + casilla.getTipo() + "]");
            imprimirAristasRecursivo(vertice.getAdyacentEdges().getFirst());
            System.out.println();
            if (casilla.getTipo() == TipoCasilla.ESCALERA) contadores[0]++;
            if (casilla.getTipo() == TipoCasilla.SERPIENTE) contadores[1]++;
            if (casilla.getTipo() == TipoCasilla.COMODIN)   contadores[2]++;
        }
        recorrerTableroRecursivo(nodoActual.getNext(), contadores);
    }

    /**
     * Imprime recursivamente las aristas de un vertice.
     *
     * @param nodoArista Nodo actual de la lista de aristas.
     */
    private void imprimirAristasRecursivo(Node<Edge> nodoArista) {
        if (nodoArista == null) {
            return;
        }
        Edge arista = nodoArista.getInfo();
        CasillaDTO destino = (CasillaDTO) arista.getDestination().getInfo();
        System.out.print(" -> " + destino.getNumeroCasilla() + "(p=" + arista.getValue() + ")");
        imprimirAristasRecursivo(nodoArista.getNext());
    }

    /**
     * Cuenta recursivamente cuantas casillas de un tipo determinado hay en el tablero.
     *
     * @param nodoActual Nodo actual en el recorrido.
     * @param tipo       Tipo de casilla a contar.
     * @return Cantidad de casillas de ese tipo.
     */
    public int contarCasillasPorTipo(Node<Vertex<?>> nodoActual, TipoCasilla tipo) {
        if (nodoActual == null) {
            return 0;
        }
        int este = 0;
        if (nodoActual.getInfo().getInfo() instanceof CasillaDTO) {
            CasillaDTO casilla = (CasillaDTO) nodoActual.getInfo().getInfo();
            este = casilla.getTipo() == tipo ? 1 : 0;
        }
        return este + contarCasillasPorTipo(nodoActual.getNext(), tipo);
    }

    // =========================================================================
    // MOVIMIENTO DE JUGADOR
    // =========================================================================

    /**
     * Mueve un jugador segun el valor del dado. Aplica la logica completa:
     * rebote si supera 100, busqueda del vertice destino, efecto de la casilla
     * (serpiente, escalera, comodin), colision con otro jugador y registro en historial.
     *
     * <p>Cuando la casilla destino es COMODIN, se genera un dado comodin (1-6)
     * internamente y se delega el efecto a {@link GestorComodines#activarEfecto}.
     * El GestorComodines actualiza la posicion del jugador y registra en el historial.</p>
     *
     * @param jugador   Jugador que realiza el movimiento.
     * @param dado      Valor del dado principal lanzado (1 a 6).
     * @param jugadores Lista de todos los jugadores de la partida.
     * @return Descripcion del evento ocurrido en este movimiento.
     * @throws PartidaNoIniciadaException Si el tablero no ha sido construido.
     * @throws PosicionInvalidaException  Si el dado produce una posicion invalida.
     */
    public String moverJugador(JugadorDTO jugador, int dado, MyLinkedList<JugadorDTO> jugadores)
            throws PartidaNoIniciadaException, PosicionInvalidaException {
        if (!tableroInicializado) {
            throw new PartidaNoIniciadaException("El tablero no esta inicializado.");
        }
        if (dado < 1 || dado > 6) {
            throw new PosicionInvalidaException("Valor de dado invalido: " + dado);
        }

        int posAntes = jugador.getPosicionActual();
        int posNueva = posAntes + dado;

        // Regla de rebote: si supera 100, el jugador no se mueve
        if (posNueva > 100) {
            String eventoRebote = "REBOTE (necesita exactamente " + (100 - posAntes) + " para ganar)";
            registrarMovimiento(jugador.getNombre(), dado, posAntes, posAntes, eventoRebote);
            jugador.incrementarTurnos();
            return eventoRebote;
        }

        // Buscar el vertice en la nueva posicion
        Vertex<CasillaDTO> verticeDestino = buscarVertice(posNueva);
        if (verticeDestino == null) {
            throw new PosicionInvalidaException("No se encontro el vertice para la casilla " + posNueva);
        }

        CasillaDTO casillaDestino = verticeDestino.getInfo();
        String evento = "NORMAL";
        int posFinal = posNueva;

        // Aplicar efecto segun tipo de casilla
        if (casillaDestino.getTipo() == TipoCasilla.SERPIENTE) {
            int posColaSerpiente = obtenerDestinoEspecial(verticeDestino, 2.0);
            if (posColaSerpiente != -1 && !jugador.isTieneEscudo()) {
                posFinal = posColaSerpiente;
                evento = "SERPIENTE: bajo de " + posNueva + " a " + posFinal;
            } else if (jugador.isTieneEscudo()) {
                evento = "SERPIENTE bloqueada por ESCUDO";
                jugador.setTieneEscudo(false);
            }
        } else if (casillaDestino.getTipo() == TipoCasilla.ESCALERA) {
            int posCimaEscalera = obtenerDestinoEspecial(verticeDestino, 0.5);
            if (posCimaEscalera != -1) {
                posFinal = posCimaEscalera;
                evento = "ESCALERA: subio de " + posNueva + " a " + posFinal;
            }
        } else if (casillaDestino.getTipo() == TipoCasilla.COMODIN) {
            // Primero se fija la posicion del jugador en la casilla comodin,
            // luego GestorComodines la modifica segun el dado comodin
            jugador.setPosicionActual(posNueva);
            int dadoComodin = 1 + new java.util.Random().nextInt(6);
            GestorComodines gestor = new GestorComodines();
            evento = gestor.activarEfecto(jugador, dadoComodin, this, historialMovimientos);
            // GestorComodines ya actualizo jugador.posicionActual, lo leemos para posFinal
            posFinal = jugador.getPosicionActual();
        }

        // Detectar colision con otro jugador en la casilla destino final
        String eventoColision = detectarColision(jugador.getNombre(), posFinal,
                jugadores.getFirst(), "");
        if (!eventoColision.isEmpty()) {
            evento = evento + " | " + eventoColision;
        }

        // Actualizar posicion final (para SERPIENTE/ESCALERA/NORMAL; COMODIN ya lo hizo el gestor)
        jugador.setPosicionActual(posFinal);
        jugador.incrementarTurnos();

        // Registrar movimiento principal en el historial
        registrarMovimiento(jugador.getNombre(), dado, posAntes, posFinal, evento);

        return evento;
    }

    /**
     * Busca recursivamente en las aristas de un vertice la arista especial
     * que tiene el peso indicado y retorna la casilla destino.
     * Se usa para obtener el destino de serpiente (peso 2.0) o escalera (peso 0.5).
     *
     * @param vertice      Vertice del que se buscan las aristas.
     * @param pesoEspecial Peso de la arista especial buscada.
     * @return Numero de casilla destino, o -1 si no se encuentra.
     */
    private int obtenerDestinoEspecial(Vertex<CasillaDTO> vertice, double pesoEspecial) {
        return buscarAristaEspecialRecursivo(vertice.getAdyacentEdges().getFirst(), pesoEspecial);
    }

    /**
     * Recorre recursivamente las aristas de un vertice hasta encontrar la que
     * tiene el peso especificado.
     *
     * @param nodoArista   Nodo actual de la lista de aristas.
     * @param pesoEspecial Peso buscado.
     * @return Numero de casilla destino de la arista encontrada, o -1.
     */
    private int buscarAristaEspecialRecursivo(Node<Edge> nodoArista, double pesoEspecial) {
        if (nodoArista == null) {
            return -1;
        }
        Edge arista = nodoArista.getInfo();
        if (arista.getValue() == pesoEspecial) {
            CasillaDTO destino = (CasillaDTO) arista.getDestination().getInfo();
            return destino.getNumeroCasilla();
        }
        return buscarAristaEspecialRecursivo(nodoArista.getNext(), pesoEspecial);
    }

    /**
     * Detecta recursivamente si otro jugador ya esta en la casilla destino.
     * Si hay colision, retorna una descripcion del evento.
     *
     * @param nombreJugadorActivo Nombre del jugador que se mueve.
     * @param posDestino          Casilla a la que llega el jugador.
     * @param nodoActual          Nodo actual de la lista de jugadores.
     * @param acumulado           Texto acumulado de colisiones encontradas.
     * @return Descripcion de la colision, o cadena vacia si no hay.
     */
    private String detectarColision(String nombreJugadorActivo, int posDestino,
            Node<JugadorDTO> nodoActual, String acumulado) {
        if (nodoActual == null) {
            return acumulado;
        }
        JugadorDTO otro = nodoActual.getInfo();
        if (!otro.getNombre().equals(nombreJugadorActivo)
                && otro.getPosicionActual() == posDestino) {
            String colision = "COLISION con " + otro.getNombre() + " en casilla " + posDestino;
            return detectarColision(nombreJugadorActivo, posDestino,
                    nodoActual.getNext(), acumulado.isEmpty() ? colision : acumulado + " | " + colision);
        }
        return detectarColision(nombreJugadorActivo, posDestino, nodoActual.getNext(), acumulado);
    }

    // =========================================================================
    // DETECCION DE GANADOR
    // =========================================================================

    /**
     * Recorre recursivamente la lista de jugadores y retorna el primero que
     * haya llegado a la casilla 100 (ganador). Retorna null si nadie ha ganado.
     *
     * @param nodoActual Nodo actual de la lista de jugadores.
     * @return El JugadorDTO ganador, o null si no hay ganador aun.
     */
    public JugadorDTO detectarGanador(Node<JugadorDTO> nodoActual) {
        if (nodoActual == null) {
            return null;
        }
        JugadorDTO jugador = nodoActual.getInfo();
        if (jugador.getPosicionActual() == 100) {
            return jugador;
        }
        return detectarGanador(nodoActual.getNext());
    }

    /**
     * Verifica si hay un ganador en la lista de jugadores dada.
     *
     * @param jugadores Lista de jugadores de la partida.
     * @return El jugador ganador si existe, null en caso contrario.
     */
    public JugadorDTO verificarGanador(MyLinkedList<JugadorDTO> jugadores) {
        return detectarGanador(jugadores.getFirst());
    }

    // =========================================================================
    // HISTORIAL
    // =========================================================================

    /**
     * Registra un movimiento en el historial de la partida.
     *
     * @param nombreJugador Nombre del jugador.
     * @param dado          Valor del dado lanzado.
     * @param posAntes      Posicion antes del movimiento.
     * @param posDespues    Posicion despues del movimiento.
     * @param evento        Descripcion del evento.
     */
    private void registrarMovimiento(String nombreJugador, int dado,
            int posAntes, int posDespues, String evento) {
        MovimientoDTO movimiento = new MovimientoDTO(nombreJugador, dado, posAntes, posDespues, evento);
        historialMovimientos.addLast(movimiento);
    }

    /**
     * Imprime recursivamente todo el historial de movimientos de la partida.
     *
     * @param nodoActual Nodo actual del historial.
     * @param turno      Numero de turno actual.
     */
    public void imprimirHistorial(Node<MovimientoDTO> nodoActual, int turno) {
        if (nodoActual == null) {
            return;
        }
        System.out.println("Turno " + turno + ": " + nodoActual.getInfo());
        imprimirHistorial(nodoActual.getNext(), turno + 1);
    }

    // =========================================================================
    // GETTERS
    // =========================================================================

    /**
     * Retorna el grafo del tablero.
     *
     * @return El grafo con los 100 vertices.
     */
    public Graph getTablero() {
        return tablero;
    }

    /**
     * Retorna el historial de movimientos de la partida actual.
     *
     * @return Lista enlazada con todos los movimientos registrados.
     */
    public MyLinkedList<MovimientoDTO> getHistorialMovimientos() {
        return historialMovimientos;
    }

    /**
     * Indica si el tablero ha sido construido e inicializado.
     *
     * @return true si el tablero esta listo, false en caso contrario.
     */
    public boolean isTableroInicializado() {
        return tableroInicializado;
    }
}
