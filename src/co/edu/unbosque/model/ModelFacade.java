package co.edu.unbosque.model;

import java.util.Random;

import co.edu.unbosque.model.persistence.GestorColisiones;
import co.edu.unbosque.model.persistence.GestorTurnos;
import co.edu.unbosque.model.persistence.HistorialDAO;
import co.edu.unbosque.model.persistence.JugadorDAOImpl;
import co.edu.unbosque.model.persistence.RankingDAO;
import co.edu.unbosque.model.persistence.TableroDAO;
import co.edu.unbosque.utils.exception.EstructuraVaciaException;
import co.edu.unbosque.utils.exception.PartidaNoIniciadaException;
import co.edu.unbosque.utils.exception.PosicionInvalidaException;
import co.edu.unbosque.model.CasillaDTO.TipoCasilla;
import co.edu.unbosque.utils.structure.Edge;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;
import co.edu.unbosque.utils.structure.Vertex;

/**
 * Fachada del modelo del juego "Escaleras y Serpientes a lo Bosque".
 * Centraliza el acceso a todos los componentes del modelo:
 * TableroDAO, JugadorDAOImpl, GestorTurnos, GestorColisiones,
 * RankingDAO e HistorialDAO.
 *
 * <p>El Controlador interactua exclusivamente con esta clase
 *
 */
public class ModelFacade {

    /** DAO del tablero: grafo de 100 casillas. */
    private TableroDAO tableroDAO;

    /** DAO de jugadores: lista enlazada de JugadorDTO. */
    private JugadorDAOImpl jugadorDAO;

    /** Gestor de turnos: cola de jugadores activos. */
    private GestorTurnos gestorTurnos;

    /** Gestor de colisiones entre jugadores. */
    private GestorColisiones gestorColisiones;

    /** DAO del ranking: arbol AVL ordenado por puntaje. */
    private RankingDAO rankingDAO;

    /** DAO del historial global de movimientos. */
    private HistorialDAO historialDAO;

    /** Contador de veces que alguien cayo en una serpiente en la partida actual. */
    private int contadorSerpientes;

    /** Contador de veces que alguien subio por una escalera en la partida actual. */
    private int contadorEscaleras;

    /** Contador total de movimientos realizados en la partida actual. */
    private int contadorMovimientos;

    /** Ultimo valor del dado lanzado. */
    private int ultimoDado;

    /** Indica si hay una partida en curso. */
    private boolean partidaEnCurso;

    /** Generador de numeros aleatorios para el dado. */
    private Random aleatorio;

    /**
     * Constructor de ModelFacade.
     * Instancia todos los componentes del modelo listos para usarse.
     */
    public ModelFacade() {
        this.tableroDAO       = new TableroDAO();
        this.jugadorDAO       = new JugadorDAOImpl();
        this.gestorTurnos     = new GestorTurnos();
        this.gestorColisiones = new GestorColisiones();
        this.rankingDAO       = new RankingDAO();
        this.historialDAO     = new HistorialDAO();
        this.aleatorio        = new Random();
        this.partidaEnCurso   = false;
        this.ultimoDado       = 0;
        resetearContadores();
    }

    // =========================================================================
    // INICIO DE PARTIDA
    // =========================================================================

    /**
     * Inicializa una nueva partida completa:
     * <ol>
     *   <li>Resetea contadores y estado interno.</li>
     *   <li>Reconstruye todos los componentes del modelo desde cero.</li>
     *   <li>Construye el tablero llamando a tableroDAO.construirTablero().</li>
     *   <li>Crea los JugadorDTO segun los nombres recibidos y los registra.</li>
     *   <li>Inicializa GestorTurnos con la lista de jugadores.</li>
     * </ol>
     *
     * @param nombresJugadores Array con los nombres de los jugadores a registrar.
     * @throws PosicionInvalidaException Si la generacion aleatoria del tablero
     *                                   no puede cumplir las restricciones de posicion.
     * @throws EstructuraVaciaException  Si el grafo queda vacio tras la construccion.
     */
    public void iniciarPartida(String[] nombresJugadores, int cantSerpientes, int cantEscaleras)
            throws PosicionInvalidaException, EstructuraVaciaException {

        // Recrear todos los componentes para borrar estado de partida anterior
        this.tableroDAO       = new TableroDAO();
        this.jugadorDAO       = new JugadorDAOImpl();
        this.gestorTurnos     = new GestorTurnos();
        this.gestorColisiones = new GestorColisiones();
        this.rankingDAO       = new RankingDAO();
        this.historialDAO     = new HistorialDAO();
        resetearContadores();
        this.ultimoDado     = 0;
        this.partidaEnCurso = false;

        // Construir el tablero con la cantidad exacta elegida por el usuario
        tableroDAO.construirTablero(cantSerpientes, cantEscaleras);

        // Registrar jugadores
        registrarJugadoresRecursivo(nombresJugadores, 0);

        // Inicializar la cola de turnos
        gestorTurnos.inicializarTurnos(jugadorDAO.obtenerTodos());

        this.partidaEnCurso = true;
    }

    // =========================================================================
    // DADO
    // =========================================================================

    /**
     * Lanza un dado aleatorio con valores del 1 al 6, guarda el resultado
     * internamente y lo retorna para que la vista lo muestre.
     *
     * @return Valor entero entre 1 y 6 inclusive.
     */
    public int lanzarDado() {
        ultimoDado = aleatorio.nextInt(6) + 1;
        return ultimoDado;
    }

    // =========================================================================
    // EJECUCION DEL TURNO
    // =========================================================================

    /**
     * Ejecuta el turno completo del jugador activo:
     * <ol>
     *   <li>Verifica que la partida este en curso.</li>
     *   <li>Obtiene el jugador actual de GestorTurnos.</li>
     *   <li>Si el jugador pierde turno: resetea el flag, avanza turno y retorna.</li>
     *   <li>Llama a tableroDAO.moverJugador con el ultimo dado lanzado.</li>
     *   <li>Detecta colision en el evento y la procesa si corresponde.</li>
     *   <li>Actualiza los contadores de estadisticas.</li>
     *   <li>Registra el movimiento en historialDAO y en la pila del jugador.</li>
     *   <li>Maneja el doble turno o avanza el turno segun corresponda.</li>
     * </ol>
     *
     * @return Descripcion del evento ocurrido en este turno.
     * @throws PartidaNoIniciadaException Si se llama sin una partida activa.
     * @throws PosicionInvalidaException  Si el dado genera una posicion invalida.
     */
    public String ejecutarTurno() throws PartidaNoIniciadaException, PosicionInvalidaException {
        if (!partidaEnCurso) {
            throw new PartidaNoIniciadaException();
        }

        JugadorDTO jugadorActual = gestorTurnos.obtenerJugadorActual();
        if (jugadorActual == null) {
            throw new PartidaNoIniciadaException();
        }

        // Manejo de turno perdido
        if (jugadorActual.isPierdeTurno()) {
            jugadorActual.setPierdeTurno(false);
            gestorTurnos.avanzarTurno();
            return "TURNO PERDIDO: " + jugadorActual.getNombre() + " pierde su turno";
        }

        // Mover el jugador en el tablero
        MyLinkedList<JugadorDTO> listaJugadores = jugadorDAO.obtenerTodos();
        String evento = tableroDAO.moverJugador(jugadorActual, ultimoDado, listaJugadores);

        // Detectar colision y procesarla
        if (evento.contains("COLISION")) {
            String nombreColisionado = extraerNombreColisionado(evento);
            JugadorDTO jugadorColisionado = jugadorDAO.buscarJugador(nombreColisionado);
            if (jugadorColisionado != null) {
                String resultadoColision = gestorColisiones.procesarColision(
                        jugadorActual, jugadorColisionado,
                        tableroDAO,
                        tableroDAO.getHistorialMovimientos());
                evento = evento + " | " + resultadoColision;
            }
        }

        // Actualizar contadores de estadisticas
        if (evento.contains("SERPIENTE") && !evento.contains("bloqueada")) {
            contadorSerpientes++;
        }
        if (evento.contains("ESCALERA")) {
            contadorEscaleras++;
        }
        contadorMovimientos++;

        // Registrar en el historial global y en la pila personal del jugador
        MovimientoDTO movimiento = new MovimientoDTO(
                jugadorActual.getNombre(),
                ultimoDado,
                jugadorActual.getPosicionActual(),
                jugadorActual.getPosicionActual(),
                evento
        );
        historialDAO.registrarMovimiento(movimiento);
        gestorTurnos.registrarMovimientoEnPila(jugadorActual, movimiento);

        // Gestionar continuidad del turno
        if (jugadorActual.isTieneDobleTurno()) {
            gestorTurnos.manejarDobleTurno(jugadorActual);
        } else {
            gestorTurnos.avanzarTurno();
        }

        return evento;
    }

    // =========================================================================
    // CONSULTAS DEL ESTADO DEL JUEGO
    // =========================================================================

    /**
     * Retorna el jugador activo actualmente (el primero en la cola de turnos).
     *
     * @return JugadorDTO del jugador que debe jugar, o null si no hay jugadores.
     */
    public JugadorDTO obtenerJugadorActual() {
        return gestorTurnos.obtenerJugadorActual();
    }

    /**
     * Retorna el indice del jugador actual dentro del arreglo de todos los jugadores.
     * La busqueda es recursiva usando el nombre del jugador activo como clave.
     *
     * @return Indice entero (base 0) del jugador actual, o -1 si no se encuentra.
     */
    public int obtenerIndiceJugadorActual() {
        JugadorDTO jugadorActual = gestorTurnos.obtenerJugadorActual();
        if (jugadorActual == null) {
            return 0;
        }
        JugadorDTO[] arreglo = obtenerTodosJugadores();
        return buscarIndiceRecursivo(arreglo, jugadorActual.getNombre(), 0);
    }

    /**
     * Retorna un array de JugadorDTO con todos los jugadores en orden de inscripcion.
     * La conversion de MyLinkedList a array se hace recursivamente.
     *
     * @return Array de JugadorDTO con todos los jugadores registrados.
     */
    public JugadorDTO[] obtenerTodosJugadores() {
        MyLinkedList<JugadorDTO> lista = jugadorDAO.obtenerTodos();
        int cantidad = lista.size();
        JugadorDTO[] arreglo = new JugadorDTO[cantidad];
        llenarArregloJugadores(lista.getFirst(), arreglo, 0);
        return arreglo;
    }

    /**
     * Retorna el array de CasillaDTO (indices 1 a 100) para que la vista
     * dibuje el tablero. La posicion 0 del arreglo queda null (no se usa).
     * El recorrido de los vertices del grafo es recursivo.
     *
     * @return Array de CasillaDTO de longitud 101 (indices 1 al 100 validos).
     */
    public CasillaDTO[] obtenerCasillasTablero() {
        CasillaDTO[] casillas = new CasillaDTO[101];
        if (tableroDAO.isTableroInicializado()) {
            llenarArregloCasillas(
                    tableroDAO.getTablero().getListOfNodes().getFirst(),
                    casillas);
        }
        return casillas;
    }

    /**
     * Verifica si alguno de los jugadores ha llegado a la casilla 100.
     *
     * @return JugadorDTO del ganador si existe, o null si nadie ha ganado.
     */
    public JugadorDTO verificarGanador() {
        if (!tableroDAO.isTableroInicializado()) {
            return null;
        }
        return tableroDAO.verificarGanador(jugadorDAO.obtenerTodos());
    }

    // =========================================================================
    // FINALIZACION DE PARTIDA
    // =========================================================================

    /**
     * Finaliza la partida actual:
     * <ol>
     *   <li>Registra a todos los jugadores en RankingDAO.</li>
     *   <li>Marca al ganador con gano=true.</li>
     *   <li>Imprime el historial y el ranking por consola.</li>
     *   <li>Marca la partida como terminada.</li>
     * </ol>
     *
     * @param ganador JugadorDTO del jugador que gano la partida.
     */
    public void finalizarPartida(JugadorDTO ganador) {
        String nombreGanador = (ganador != null) ? ganador.getNombre() : "";
        registrarJugadoresEnRanking(jugadorDAO.obtenerTodos().getFirst(), nombreGanador);
        historialDAO.imprimirHistorialCompleto();
        rankingDAO.imprimirRanking();
        this.partidaEnCurso = false;
    }

    /**
     * Retorna el ranking final ordenado del AVLTree en inorder
     * (del peor al mejor resultado).
     *
     * @return Array de Object con los RankingDTO ordenados.
     */
    public Object[] obtenerRankingFinal() {
        return rankingDAO.obtenerRankingOrdenado();
    }

    // =========================================================================
    // GETTERS DE ESTADISTICAS Y ESTADO
    // =========================================================================

    /**
     * Retorna el ultimo valor del dado lanzado.
     *
     * @return Entero entre 0 (sin lanzar) y 6.
     */
    public int getUltimoDado() {
        return ultimoDado;
    }

    /**
     * Retorna el total de veces que alguien cayo en una serpiente
     * durante la partida actual.
     *
     * @return Contador de serpientes.
     */
    public int getContadorSerpientes() {
        return contadorSerpientes;
    }

    /**
     * Retorna el total de veces que alguien subio por una escalera
     * durante la partida actual.
     *
     * @return Contador de escaleras.
     */
    public int getContadorEscaleras() {
        return contadorEscaleras;
    }

    /**
     * Retorna el total de movimientos realizados en la partida actual.
     *
     * @return Contador de movimientos.
     */
    public int getContadorMovimientos() {
        return contadorMovimientos;
    }

    /**
     * Indica si hay una partida actualmente en curso.
     *
     * @return true si la partida esta activa, false en caso contrario.
     */
    public boolean isPartidaEnCurso() {
        return partidaEnCurso;
    }

    // =========================================================================
    // CONEXIONES VISUALES DEL TABLERO (serpientes y escaleras)
    // =========================================================================

    /**
     * Retorna las conexiones de serpientes como array de pares [cabeza, cola].
     * Recorre el grafo recursivamente buscando vertices cuyo tipo sea SERPIENTE
     * y extrae el destino de la arista de peso 2.0 (convencion del generador).
     *
     * <p>Se usa un arreglo temporal de tama&ntilde;o 20 (maximo posible de serpientes)
     * y un contenedor mutable {@code int[]{0}} como contador para la recursion.</p>
     *
     * @return Array int[n][2] donde cada fila es {numeroCasillaOrigen, numeroCasillaDestino}.
     *         Retorna un array vacio si el tablero no esta inicializado.
     */
    public int[][] obtenerConexionesSerpientes() {
        if (!tableroDAO.isTableroInicializado()) {
            return new int[0][2];
        }
        int[][] temporal = new int[20][2];
        int[]   contador = {0};
        recolectarConexionesRecursivo(
                tableroDAO.getTablero().getListOfNodes().getFirst(),
                TipoCasilla.SERPIENTE, 2.0, temporal, contador);
        int[][] resultado = new int[contador[0]][2];
        copiarConexionesRecursivo(temporal, resultado, 0);
        return resultado;
    }

    /**
     * Retorna las conexiones de escaleras como array de pares [base, cima].
     * Recorre el grafo recursivamente buscando vertices cuyo tipo sea ESCALERA
     * y extrae el destino de la arista de peso 0.5 (convencion del generador).
     *
     * <p>Se usa un arreglo temporal de tama&ntilde;o 20 (maximo posible de escaleras)
     * y un contenedor mutable {@code int[]{0}} como contador para la recursion.</p>
     *
     * @return Array int[n][2] donde cada fila es {numeroCasillaOrigen, numeroCasillaDestino}.
     *         Retorna un array vacio si el tablero no esta inicializado.
     */
    public int[][] obtenerConexionesEscaleras() {
        if (!tableroDAO.isTableroInicializado()) {
            return new int[0][2];
        }
        int[][] temporal = new int[20][2];
        int[]   contador = {0};
        recolectarConexionesRecursivo(
                tableroDAO.getTablero().getListOfNodes().getFirst(),
                TipoCasilla.ESCALERA, 0.5, temporal, contador);
        int[][] resultado = new int[contador[0]][2];
        copiarConexionesRecursivo(temporal, resultado, 0);
        return resultado;
    }

    // =========================================================================
    // METODOS PRIVADOS AUXILIARES Y RECURSIVOS
    // =========================================================================

    /**
     * Recorre recursivamente la lista de vertices del grafo y recolecta las
     * conexiones de los vertices cuyo tipo coincida con el indicado.
     * Para cada vertice del tipo buscado, llama a
     * {@link #buscarDestinoAristaRecursivo} para localizar la arista con el
     * peso indicado y registra el par origen-destino en el array temporal.
     *
     * <p>Caso base: {@code nodoVertice} es null — no hay mas vertices.</p>
     * <p>Caso recursivo: analiza el vertice actual y avanza al siguiente.</p>
     *
     * @param nodoVertice Nodo actual de la lista de vertices del grafo.
     * @param tipo        Tipo de casilla que se esta buscando (SERPIENTE o ESCALERA).
     * @param pesoArista  Peso de la arista que conecta al destino (2.0 o 0.5).
     * @param temporal    Array de pares de conexiones (pre-alocado en tama&ntilde;o maximo).
     * @param contador    Contenedor mutable int[1] con la cantidad de conexiones encontradas.
     */
    private void recolectarConexionesRecursivo(Node<Vertex<?>> nodoVertice,
            TipoCasilla tipo, double pesoArista, int[][] temporal, int[] contador) {
        if (nodoVertice == null) {
            return;
        }
        Vertex<?> vertice = nodoVertice.getInfo();
        if (vertice.getInfo() instanceof CasillaDTO) {
            CasillaDTO casilla = (CasillaDTO) vertice.getInfo();
            if (casilla.getTipo() == tipo && contador[0] < temporal.length) {
                int destino = buscarDestinoAristaRecursivo(
                        vertice.getAdyacentEdges().getFirst(), pesoArista);
                if (destino != -1) {
                    temporal[contador[0]][0] = casilla.getNumeroCasilla();
                    temporal[contador[0]][1] = destino;
                    contador[0]++;
                }
            }
        }
        recolectarConexionesRecursivo(nodoVertice.getNext(), tipo, pesoArista, temporal, contador);
    }

    /**
     * Busca recursivamente en la lista de aristas de un vertice aquella cuyo
     * peso coincida con el valor indicado y retorna el numero de casilla del destino.
     *
     * <p>Caso base 1: {@code nodoArista} es null — la arista no existe, retorna -1.</p>
     * <p>Caso base 2: el peso de la arista actual coincide — retorna el numero de casilla
     * del vertice destino.</p>
     * <p>Caso recursivo: avanza a la siguiente arista.</p>
     *
     * @param nodoArista Nodo actual de la lista de aristas del vertice.
     * @param peso       Peso que identifica el tipo de conexion (2.0 serpiente, 0.5 escalera).
     * @return Numero de casilla del vertice destino, o -1 si no se encuentra.
     */
    private int buscarDestinoAristaRecursivo(Node<Edge> nodoArista, double peso) {
        if (nodoArista == null) {
            return -1;
        }
        Edge arista = nodoArista.getInfo();
        if (Math.abs(arista.getValue() - peso) < 0.001) {
            Object infoDestino = arista.getDestination().getInfo();
            if (infoDestino instanceof CasillaDTO) {
                return ((CasillaDTO) infoDestino).getNumeroCasilla();
            }
        }
        return buscarDestinoAristaRecursivo(nodoArista.getNext(), peso);
    }

    /**
     * Copia recursivamente los primeros {@code destino.length} elementos del array
     * {@code origen} al array {@code destino}.
     *
     * <p>Caso base: {@code indice} es igual o mayor que la longitud de {@code destino}.</p>
     * <p>Caso recursivo: copia la fila actual y avanza al siguiente indice.</p>
     *
     * @param origen  Array fuente (puede ser mas grande que destino).
     * @param destino Array destino (ya alojado con el tama&ntilde;o exacto).
     * @param indice  Indice actual de la copia recursiva (iniciar con 0).
     */
    private void copiarConexionesRecursivo(int[][] origen, int[][] destino, int indice) {
        if (indice >= destino.length) {
            return;
        }
        destino[indice][0] = origen[indice][0];
        destino[indice][1] = origen[indice][1];
        copiarConexionesRecursivo(origen, destino, indice + 1);
    }

    /**
     * Resetea a cero los contadores de estadisticas de la partida.
     */
    private void resetearContadores() {
        contadorSerpientes  = 0;
        contadorEscaleras   = 0;
        contadorMovimientos = 0;
    }

    /**
     * Registra recursivamente los jugadores en JugadorDAOImpl
     * a partir del array de nombres recibido.
     * Caso base: indice fuera del rango del array.
     * Caso recursivo: crea el JugadorDTO, lo agrega y avanza el indice.
     *
     * @param nombres Array de nombres de los jugadores.
     * @param indice  Indice actual del array en la recursion.
     */
    private void registrarJugadoresRecursivo(String[] nombres, int indice) {
        if (indice >= nombres.length) {
            return;
        }
        JugadorDTO jugador = new JugadorDTO(nombres[indice]);
        jugadorDAO.agregarJugador(jugador);
        registrarJugadoresRecursivo(nombres, indice + 1);
    }

    /**
     * Llena recursivamente un array de JugadorDTO a partir de la lista enlazada.
     * Caso base: nodo null o indice fuera del rango del arreglo.
     * Caso recursivo: asigna el info del nodo y avanza al siguiente.
     *
     * @param nodo    Nodo actual de la lista enlazada de jugadores.
     * @param arreglo Array destino donde se copian los jugadores.
     * @param indice  Posicion actual del arreglo en la recursion.
     */
    private void llenarArregloJugadores(Node<JugadorDTO> nodo,
                                         JugadorDTO[] arreglo,
                                         int indice) {
        if (nodo == null || indice >= arreglo.length) {
            return;
        }
        arreglo[indice] = nodo.getInfo();
        llenarArregloJugadores(nodo.getNext(), arreglo, indice + 1);
    }

    /**
     * Busca recursivamente el indice de un jugador en el arreglo dado su nombre.
     * Caso base: indice fuera de rango — retorna -1 (no encontrado).
     * Caso recursivo: compara el nombre y avanza si no coincide.
     *
     * @param jugadores Array de jugadores donde se busca.
     * @param nombre    Nombre del jugador a localizar.
     * @param indice    Posicion actual del arreglo en la recursion.
     * @return Indice del jugador en el arreglo, o -1 si no se encuentra.
     */
    private int buscarIndiceRecursivo(JugadorDTO[] jugadores, String nombre, int indice) {
        if (indice >= jugadores.length) {
            return -1;
        }
        if (jugadores[indice] != null && jugadores[indice].getNombre().equals(nombre)) {
            return indice;
        }
        return buscarIndiceRecursivo(jugadores, nombre, indice + 1);
    }

    /**
     * Llena recursivamente el array de CasillaDTO recorriendo los vertices del grafo.
     * Cada CasillaDTO se ubica en el indice igual a su numeroCasilla.
     * Caso base: nodo null.
     * Caso recursivo: extrae la CasillaDTO del vertice, la ubica en el arreglo
     * y continua con el siguiente nodo.
     *
     * @param nodoVertice Nodo actual de la lista de vertices del grafo.
     * @param casillas    Array destino de longitud 101.
     */
    private void llenarArregloCasillas(Node<Vertex<?>> nodoVertice, CasillaDTO[] casillas) {
        if (nodoVertice == null) {
            return;
        }
        Vertex<?> vertice = nodoVertice.getInfo();
        if (vertice.getInfo() instanceof CasillaDTO) {
            CasillaDTO casilla = (CasillaDTO) vertice.getInfo();
            int numero = casilla.getNumeroCasilla();
            if (numero >= 1 && numero <= 100) {
                casillas[numero] = casilla;
            }
        }
        llenarArregloCasillas(nodoVertice.getNext(), casillas);
    }

    /**
     * Registra recursivamente todos los jugadores en el RankingDAO al finalizar
     * la partida. El jugador cuyo nombre coincida con el del ganador se registra
     * con gano=true; los demas con gano=false.
     * Caso base: nodo null — no hay mas jugadores que registrar.
     *
     * @param nodo          Nodo actual de la lista de jugadores.
     * @param nombreGanador Nombre del jugador ganador de la partida.
     */
    private void registrarJugadoresEnRanking(Node<JugadorDTO> nodo, String nombreGanador) {
        if (nodo == null) {
            return;
        }
        JugadorDTO jugador = nodo.getInfo();
        boolean gano = jugador.getNombre().equals(nombreGanador);
        rankingDAO.registrarJugador(jugador, gano);
        registrarJugadoresEnRanking(nodo.getNext(), nombreGanador);
    }

    /**
     * Extrae el nombre del jugador con quien se produjo la colision a partir
     * del texto del evento. El evento contiene la subcadena "COLISION con NombreX".
     * Si no se puede extraer el nombre, retorna una cadena vacia.
     *
     * @param evento Texto del evento retornado por tableroDAO.moverJugador.
     * @return Nombre del jugador colisionado, o cadena vacia si no se encuentra.
     */
    private String extraerNombreColisionado(String evento) {
        String marcador = "COLISION con ";
        int inicio = evento.indexOf(marcador);
        if (inicio == -1) {
            return "";
        }
        int desdeNombre = inicio + marcador.length();
        int finNombre = evento.indexOf(" en casilla", desdeNombre);
        if (finNombre == -1) {
            return evento.substring(desdeNombre).trim();
        }
        return evento.substring(desdeNombre, finNombre).trim();
    }
}
