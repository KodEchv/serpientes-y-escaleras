package co.edu.unbosque.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import co.edu.unbosque.model.CasillaDTO;
import co.edu.unbosque.model.CasillaDTO.TipoCasilla;
import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.ModelFacade;
import co.edu.unbosque.model.MovimientoDTO;
import co.edu.unbosque.model.persistence.FileHandler;
import co.edu.unbosque.model.persistence.Mapeador;
import co.edu.unbosque.utils.exception.EstructuraVaciaException;
import co.edu.unbosque.utils.exception.PartidaNoIniciadaException;
import co.edu.unbosque.utils.exception.PosicionInvalidaException;
import co.edu.unbosque.utils.structure.Edge;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;
import co.edu.unbosque.utils.structure.Vertex;
import co.edu.unbosque.view.VentanaPrincipal;
import co.edu.unbosque.view.ViewFacade;

/**
 * Controlador principal del juego "Escaleras y Serpientes a lo Bosque".
 * Es el unico punto de conexion entre la capa de vista ({@link ViewFacade})
 * y la capa de modelo ({@link ModelFacade}).
 *
 * <p>Contiene toda la logica de flujo del juego: iniciar partida, lanzar dado,
 * ejecutar turno, verificar ganador y finalizar partida.
 * Accede a los DAOs y gestores a traves de los getters de ModelFacade.</p>
 */
public class Controlador {

    /** Fachada del modelo: acceso a todos los DAOs y gestores del juego. */
    private ModelFacade modelo;

    /** Fachada de la vista: acceso a todos los paneles Swing. */
    private ViewFacade vista;

    /** Indica si el dado fue lanzado en el turno actual. */
    private boolean dadoLanzado;

    /** Indica si hay una partida en curso. */
    private boolean partidaActiva;

    // =========================================================================
    // ESTADO DEL JUEGO
    // =========================================================================

    /** Generador de numeros aleatorios para el dado. */
    private Random aleatorio;

    /** Contador de veces que alguien cayo en una serpiente en la partida actual. */
    private int contadorSerpientes;

    /** Contador de veces que alguien subio por una escalera en la partida actual. */
    private int contadorEscaleras;

    /** Contador total de movimientos realizados en la partida actual. */
    private int contadorMovimientos;

    /** Ultimo valor del dado lanzado. */
    private int ultimoDado;

    /** Lista de JugadorDTO activos en la partida actual (estado de juego en memoria). */
    private MyLinkedList<JugadorDTO> jugadoresDTO;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    /**
     * Constructor de Controlador.
     * Crea las fachadas de vista y modelo, inicializa los campos internos
     * y registra los listeners de los botones principales de la interfaz.
     *
     * @param ventana La ventana principal ya creada y visible.
     */
    public Controlador() {
        this.vista         = new ViewFacade();
        this.modelo        = new ModelFacade();
        this.aleatorio     = new Random();
        this.dadoLanzado   = false;
        this.partidaActiva = false;
        this.ultimoDado    = 0;
        this.jugadoresDTO  = new MyLinkedList<>();
        FileHandler.checkFolder();
        resetearContadores();
        registrarListeners();
    }

    // =========================================================================
    // REGISTRO DE LISTENERS
    // =========================================================================

    /**
     * Conecta los ActionListener de los botones principales de la interfaz.
     */
    private void registrarListeners() {

        vista.getPanelConfiguracion().setListenerIniciar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionIniciarPartida();
            }
        });

        vista.getPanelJuego().getPanelControlJuego().getPanelDado()
                .setListenerLanzar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionLanzarDado();
            }
        });

        vista.getPanelJuego().getPanelControlJuego().setListenerFinalizar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionFinalizarPartida();
            }
        });

        vista.getPanelJuego().getPanelControlJuego().setListenerRegresar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.getVentana().navegarA(VentanaPrincipal.CLAVE_MENU);
            }
        });
    }

    // =========================================================================
    // ACCIONES PRINCIPALES
    // =========================================================================

    /**
     * Accion del boton INICIAR PARTIDA.
     * Valida los nombres, inicializa la partida en el modelo,
     * configura la vista y navega al panel de juego.
     */
    private void accionIniciarPartida() {
        if (!vista.getPanelConfiguracion().validarNombres()) {
            return;
        }

        String[] nombres       = vista.getPanelConfiguracion().obtenerNombresJugadores();
        int cantSerpientes     = vista.getPanelConfiguracion().obtenerCantidadSerpientes();
        int cantEscaleras      = vista.getPanelConfiguracion().obtenerCantidadEscaleras();

        try {
            iniciarPartida(nombres, cantSerpientes, cantEscaleras);
        } catch (PosicionInvalidaException ex) {
            System.err.println("[Controlador] Error al construir el tablero: " + ex.getMessage());
            return;
        } catch (EstructuraVaciaException ex) {
            System.err.println("[Controlador] Error: estructura de datos vacia: " + ex.getMessage());
            return;
        }

        vista.getPanelJuego().inicializarConexionesTablero(
                obtenerConexionesSerpientes(),
                obtenerConexionesEscaleras()
        );

        dadoLanzado   = false;
        partidaActiva = true;
        vista.getPanelJuego().getPanelControlJuego().getPanelDado().habilitarBoton(true);

        JugadorDTO primerJugador = obtenerJugadorActual();
        String mensajeInicio = "¡Partida iniciada! Turno de: "
                + (primerJugador != null ? primerJugador.getNombre() : "");
        actualizarVista(mensajeInicio);

        vista.getVentana().navegarA(VentanaPrincipal.CLAVE_JUEGO);
    }

    /**
     * Accion del boton LANZAR DADO.
     * Lanza el dado, muestra su valor, ejecuta el turno del jugador activo,
     * verifica si hay ganador y actualiza la vista con el resultado.
     */
    private void accionLanzarDado() {
        if (!partidaActiva || dadoLanzado) {
            return;
        }

        int dado = lanzarDado();
        CasillaDTO[]  casillasAntes  = obtenerCasillasTablero();
        JugadorDTO[]  jugadoresAntes = obtenerTodosJugadores();
        int           indiceAntes    = obtenerIndiceJugadorActual();
        vista.getPanelJuego().actualizarEstado(casillasAntes, jugadoresAntes,
                indiceAntes, dado, "Dado: " + dado);

        String evento;
        try {
            evento = ejecutarTurno();
        } catch (PartidaNoIniciadaException ex) {
            System.err.println("[Controlador] Error: " + ex.getMessage());
            dadoLanzado = false;
            return;
        } catch (PosicionInvalidaException ex) {
            System.err.println("[Controlador] Error de posicion en el turno: " + ex.getMessage());
            dadoLanzado = false;
            return;
        }

        JugadorDTO ganador = verificarGanador();
        if (ganador != null) {
            cerrarPartidaConGanador(ganador);
            dadoLanzado = false;
            return;
        }

        JugadorDTO siguiente = obtenerJugadorActual();
        String mensajeTurno  = evento + " | Turno de: "
                + (siguiente != null ? siguiente.getNombre() : "");
        actualizarVista(mensajeTurno);

        dadoLanzado = false;
    }

    /**
     * Accion del boton FINALIZAR PARTIDA.
     * Determina el lider actual usando recursividad y cierra la partida.
     */
    private void accionFinalizarPartida() {
        if (!partidaActiva) {
            return;
        }

        JugadorDTO[] jugadores = obtenerTodosJugadores();
        if (jugadores == null || jugadores.length == 0) {
            return;
        }

        JugadorDTO lider = determinarLiderRecursivo(jugadores, 1, jugadores[0]);
        cerrarPartidaConGanador(lider);
    }

    // =========================================================================
    // LOGICA DE JUEGO
    // =========================================================================

    /**
     * Inicializa una nueva partida completa:
     * recrea todos los componentes del modelo desde cero, construye el tablero,
     * registra los jugadores e inicializa la cola de turnos.
     *
     * @param nombresJugadores Array con los nombres de los jugadores.
     * @param cantSerpientes   Cantidad exacta de serpientes a colocar.
     * @param cantEscaleras    Cantidad exacta de escaleras a colocar.
     * @throws PosicionInvalidaException Si el tablero no puede cumplir las restricciones.
     * @throws EstructuraVaciaException  Si el grafo queda vacio tras la construccion.
     */
    private void iniciarPartida(String[] nombresJugadores, int cantSerpientes, int cantEscaleras)
            throws PosicionInvalidaException, EstructuraVaciaException {
        // Recrear la fachada para borrar el estado de la partida anterior
        modelo = new ModelFacade();
        resetearContadores();
        ultimoDado     = 0;
        partidaActiva  = false;

        // Inicializar lista de jugadores DTO para el estado de juego en memoria
        jugadoresDTO = new MyLinkedList<>();

        // Construir el tablero con la cantidad exacta elegida por el usuario
        modelo.getTableroDAO().construirTablero(cantSerpientes, cantEscaleras);

        // Registrar jugadores
        registrarJugadoresRecursivo(nombresJugadores, 0);

        // Inicializar la cola de turnos
        modelo.getGestorTurnos().inicializarTurnos(jugadoresDTO);

        partidaActiva = true;
    }

    /**
     * Lanza un dado aleatorio (1 a 6), guarda el resultado y lo retorna.
     *
     * @return Valor entero entre 1 y 6 inclusive.
     */
    private int lanzarDado() {
        ultimoDado = aleatorio.nextInt(6) + 1;
        return ultimoDado;
    }

    /**
     * Ejecuta el turno completo del jugador activo:
     * verifica turno perdido, mueve al jugador, procesa colisiones,
     * actualiza contadores, registra en el historial y avanza el turno.
     *
     * @return Descripcion del evento ocurrido en este turno.
     * @throws PartidaNoIniciadaException Si se llama sin una partida activa.
     * @throws PosicionInvalidaException  Si el dado genera una posicion invalida.
     */
    private String ejecutarTurno() throws PartidaNoIniciadaException, PosicionInvalidaException {
        if (!partidaActiva) {
            throw new PartidaNoIniciadaException();
        }

        JugadorDTO jugadorActual = modelo.getGestorTurnos().obtenerJugadorActual();
        if (jugadorActual == null) {
            throw new PartidaNoIniciadaException();
        }

        // Manejo de turno perdido
        if (jugadorActual.isPierdeTurno()) {
            jugadorActual.setPierdeTurno(false);
            modelo.getGestorTurnos().avanzarTurno();
            return "TURNO PERDIDO: " + jugadorActual.getNombre() + " pierde su turno";
        }

        // Mover el jugador en el tablero
        MyLinkedList<JugadorDTO> listaJugadores = jugadoresDTO;
        String evento = modelo.getTableroDAO().moverJugador(jugadorActual, ultimoDado, listaJugadores);

        // Detectar colision y procesarla
        if (evento.contains("COLISION")) {
            String nombreColisionado = extraerNombreColisionado(evento);
            JugadorDTO jugadorColisionado = buscarJugadorDTO(nombreColisionado);
            if (jugadorColisionado != null) {
                String resultadoColision = modelo.getGestorColisiones().procesarColision(
                        jugadorActual, jugadorColisionado,
                        modelo.getTableroDAO(),
                        modelo.getTableroDAO().getHistorialMovimientos());
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
        modelo.getMovimientoDAO().registrarMovimiento(movimiento);
        modelo.getGestorTurnos().registrarMovimientoEnPila(jugadorActual, movimiento);

        // Gestionar continuidad del turno
        if (jugadorActual.isTieneDobleTurno()) {
            modelo.getGestorTurnos().manejarDobleTurno(jugadorActual);
        } else {
            modelo.getGestorTurnos().avanzarTurno();
        }

        return evento;
    }

    /**
     * Retorna el jugador activo actualmente (el primero en la cola de turnos).
     *
     * @return JugadorDTO del jugador que debe jugar, o null si no hay jugadores.
     */
    private JugadorDTO obtenerJugadorActual() {
        return modelo.getGestorTurnos().obtenerJugadorActual();
    }

    /**
     * Retorna el indice del jugador actual dentro del arreglo de todos los jugadores.
     * La busqueda es recursiva usando el nombre del jugador activo como clave.
     *
     * @return Indice entero (base 0) del jugador actual, o -1 si no se encuentra.
     */
    private int obtenerIndiceJugadorActual() {
        JugadorDTO jugadorActual = modelo.getGestorTurnos().obtenerJugadorActual();
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
    private JugadorDTO[] obtenerTodosJugadores() {
        int cantidad = jugadoresDTO.size();
        JugadorDTO[] arreglo = new JugadorDTO[cantidad];
        llenarArregloJugadores(jugadoresDTO.getFirst(), arreglo, 0);
        return arreglo;
    }

    /**
     * Retorna el array de CasillaDTO (indices 1 a 100) para que la vista
     * dibuje el tablero.
     *
     * @return Array de CasillaDTO de longitud 101 (indices 1 al 100 validos).
     */
    private CasillaDTO[] obtenerCasillasTablero() {
        CasillaDTO[] casillas = new CasillaDTO[101];
        if (modelo.getTableroDAO().isTableroInicializado()) {
            llenarArregloCasillas(
                    modelo.getTableroDAO().getTablero().getListOfNodes().getFirst(),
                    casillas);
        }
        return casillas;
    }

    /**
     * Verifica si alguno de los jugadores ha llegado a la casilla 100.
     *
     * @return JugadorDTO del ganador si existe, o null si nadie ha ganado.
     */
    private JugadorDTO verificarGanador() {
        if (!modelo.getTableroDAO().isTableroInicializado()) {
            return null;
        }
        return modelo.getTableroDAO().verificarGanador(jugadoresDTO);
    }

    /**
     * Finaliza la partida actual:
     * registra a todos los jugadores en RankingDAO, marca al ganador,
     * imprime el historial y el ranking, y marca la partida como terminada.
     *
     * @param ganador JugadorDTO del jugador que gano la partida.
     */
    private void finalizarPartida(JugadorDTO ganador) {
        String nombreGanador = (ganador != null) ? ganador.getNombre() : "";
        registrarJugadoresEnRanking(jugadoresDTO.getFirst(), nombreGanador);
        modelo.getMovimientoDAO().imprimirHistorialCompleto();
        modelo.getRankingDAO().imprimirRanking();
        partidaActiva = false;
    }

    /**
     * Retorna el ranking final ordenado del AVLTree en inorder.
     *
     * @return Array de Object con los RankingDTO ordenados.
     */
    private Object[] obtenerRankingFinal() {
        return modelo.getRankingDAO().obtenerRankingOrdenado();
    }

    /**
     * Retorna las conexiones de serpientes como array de pares [cabeza, cola].
     *
     * @return Array int[n][2] con cada conexion {origen, destino}.
     */
    private int[][] obtenerConexionesSerpientes() {
        if (!modelo.getTableroDAO().isTableroInicializado()) {
            return new int[0][2];
        }
        int[][] temporal = new int[20][2];
        int[]   contador = {0};
        recolectarConexionesRecursivo(
                modelo.getTableroDAO().getTablero().getListOfNodes().getFirst(),
                TipoCasilla.SERPIENTE, 2.0, temporal, contador);
        int[][] resultado = new int[contador[0]][2];
        copiarConexionesRecursivo(temporal, resultado, 0);
        return resultado;
    }

    /**
     * Retorna las conexiones de escaleras como array de pares [base, cima].
     *
     * @return Array int[n][2] con cada conexion {origen, destino}.
     */
    private int[][] obtenerConexionesEscaleras() {
        if (!modelo.getTableroDAO().isTableroInicializado()) {
            return new int[0][2];
        }
        int[][] temporal = new int[20][2];
        int[]   contador = {0};
        recolectarConexionesRecursivo(
                modelo.getTableroDAO().getTablero().getListOfNodes().getFirst(),
                TipoCasilla.ESCALERA, 0.5, temporal, contador);
        int[][] resultado = new int[contador[0]][2];
        copiarConexionesRecursivo(temporal, resultado, 0);
        return resultado;
    }

    // =========================================================================
    // METODOS AUXILIARES PRIVADOS
    // =========================================================================

    /**
     * Finaliza la partida con el jugador indicado como ganador.
     *
     * @param ganador JugadorDTO del jugador que gano o lidera al finalizar.
     */
    private void cerrarPartidaConGanador(JugadorDTO ganador) {
        finalizarPartida(ganador);
        vista.getPanelJuego().getPanelControlJuego().getPanelDado().habilitarBoton(false);
        vista.getPanelGanador().cargar(
                ganador,
                obtenerRankingFinal(),
                contadorMovimientos,
                contadorSerpientes,
                contadorEscaleras
        );
        vista.getVentana().navegarA(VentanaPrincipal.CLAVE_GANADOR);
    }

    /**
     * Refresca todos los paneles del juego con el estado actual del modelo.
     *
     * @param evento Descripcion del evento ocurrido en el ultimo turno.
     */
    private void actualizarVista(String evento) {
        CasillaDTO[] casillas  = obtenerCasillasTablero();
        JugadorDTO[] jugadores = obtenerTodosJugadores();
        int          indice    = obtenerIndiceJugadorActual();
        vista.getPanelJuego().actualizarEstado(casillas, jugadores, indice, ultimoDado, evento);
    }

    // =========================================================================
    // METODOS RECURSIVOS
    // =========================================================================

    /**
     * Registra recursivamente los jugadores en JugadorDAO a partir del array
     * de nombres recibido.
     * Caso base: indice fuera del rango del array.
     *
     * @param nombres Array de nombres de los jugadores.
     * @param indice  Indice actual del array en la recursion.
     */
    private void registrarJugadoresRecursivo(String[] nombres, int indice) {
        if (indice >= nombres.length) {
            return;
        }
        JugadorDTO jugadorDTO = new JugadorDTO(nombres[indice]);
        jugadoresDTO.addLast(jugadorDTO);
        modelo.getJugadorDAO().crear(Mapeador.jugadorDTOAEntidad(jugadorDTO));
        registrarJugadoresRecursivo(nombres, indice + 1);
    }

    /**
     * Llena recursivamente un array de JugadorDTO a partir de la lista enlazada.
     * Caso base: nodo null o indice fuera del rango del arreglo.
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
     * Caso base: nodo null.
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
     * con gano=true.
     * Caso base: nodo null.
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
        modelo.getRankingDAO().registrarJugador(jugador, gano);
        registrarJugadoresEnRanking(nodo.getNext(), nombreGanador);
    }

    /**
     * Extrae el nombre del jugador con quien se produjo la colision a partir
     * del texto del evento. El evento contiene la subcadena "COLISION con NombreX".
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

    /**
     * Recorre recursivamente la lista de vertices del grafo y recolecta las
     * conexiones de los vertices cuyo tipo coincida con el indicado.
     * Caso base: nodoVertice es null.
     *
     * @param nodoVertice Nodo actual de la lista de vertices del grafo.
     * @param tipo        Tipo de casilla que se esta buscando.
     * @param pesoArista  Peso de la arista que conecta al destino.
     * @param temporal    Array de pares de conexiones (pre-alocado).
     * @param contador    Contenedor mutable int[1] con la cantidad encontrada.
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
     * peso coincida con el valor indicado y retorna el numero de casilla destino.
     * Caso base 1: nodoArista es null — retorna -1.
     * Caso base 2: peso coincide — retorna numero de casilla destino.
     *
     * @param nodoArista Nodo actual de la lista de aristas del vertice.
     * @param peso       Peso que identifica el tipo de conexion.
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
     * Copia recursivamente los primeros destino.length elementos del array origen
     * al array destino.
     * Caso base: indice igual o mayor que la longitud de destino.
     *
     * @param origen  Array fuente (puede ser mas grande que destino).
     * @param destino Array destino (ya alojado con el tamanio exacto).
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
     * Determina recursivamente el jugador con mayor posicion en el tablero.
     * Caso base: indice igual o mayor que la longitud del array.
     * Caso recursivo: compara posicion del candidato con el lider actual.
     *
     * @param jugadores   Array completo de jugadores de la partida.
     * @param indice      Posicion actual en el recorrido recursivo (iniciar con 1).
     * @param liderActual JugadorDTO con la mayor posicion encontrada hasta ahora.
     * @return JugadorDTO del jugador con la posicion mas alta en el tablero.
     */
    private JugadorDTO determinarLiderRecursivo(JugadorDTO[] jugadores,
                                                 int indice,
                                                 JugadorDTO liderActual) {
        if (indice >= jugadores.length) {
            return liderActual;
        }
        JugadorDTO candidato = jugadores[indice];
        if (candidato != null
                && candidato.getPosicionActual() > liderActual.getPosicionActual()) {
            return determinarLiderRecursivo(jugadores, indice + 1, candidato);
        }
        return determinarLiderRecursivo(jugadores, indice + 1, liderActual);
    }

    /**
     * Busca un JugadorDTO por nombre en la lista activa jugadoresDTO.
     *
     * @param nombre Nombre del jugador a buscar.
     * @return JugadorDTO encontrado, o null si no existe.
     */
    private JugadorDTO buscarJugadorDTO(String nombre) {
        return buscarJugadorDTORecursivo(jugadoresDTO.getFirst(), nombre);
    }

    /**
     * Recorre recursivamente jugadoresDTO buscando el jugador con el nombre dado.
     * Caso base: nodo null — retorna null.
     *
     * @param nodo   Nodo actual de la lista.
     * @param nombre Nombre del jugador buscado.
     * @return JugadorDTO encontrado, o null si no existe.
     */
    private JugadorDTO buscarJugadorDTORecursivo(Node<JugadorDTO> nodo, String nombre) {
        if (nodo == null) {
            return null;
        }
        if (nodo.getInfo().getNombre().equals(nombre)) {
            return nodo.getInfo();
        }
        return buscarJugadorDTORecursivo(nodo.getNext(), nombre);
    }

    /**
     * Resetea a cero los contadores de estadisticas de la partida.
     */
    private void resetearContadores() {
        contadorSerpientes  = 0;
        contadorEscaleras   = 0;
        contadorMovimientos = 0;
    }
}
