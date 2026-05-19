package co.edu.unbosque.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import co.edu.unbosque.model.CasillaDTO;
import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.facade.ModelFacade;
import co.edu.unbosque.utils.exception.EstructuraVaciaException;
import co.edu.unbosque.utils.exception.PartidaNoIniciadaException;
import co.edu.unbosque.utils.exception.PosicionInvalidaException;
import co.edu.unbosque.view.VentanaPrincipal;
import co.edu.unbosque.view.facade.ViewFacade;

/**
 * Controlador principal del juego "Escaleras y Serpientes a lo Bosque".
 * Es el unico punto de conexion entre la capa de vista ({@link ViewFacade})
 * y la capa de modelo ({@link ModelFacade}).
 *
 * <p>No contiene logica de negocio: toda operacion se delega a ModelFacade.
 * Se limita a leer la configuracion de la vista, invocar el modelo y
 * refrescar la vista con el resultado.</p>
 *
 * <p>Sustituye a JuegoController. Para usar esta clase, eliminar
 * JuegoController.java del paquete co.edu.unbosque.controller.</p>
 *
 * @author Estudiante
 * @version 1.0
 */
public class Controlador {

    /** Fachada del modelo: acceso a toda la logica del juego. */
    private ModelFacade modelo;

    /** Fachada de la vista: acceso a todos los paneles Swing. */
    private ViewFacade vista;

    /**
     * Bandera que indica si el dado fue lanzado pero el turno aun no se ejecuto.
     * En esta implementacion, dado y movimiento ocurren en el mismo metodo,
     * por lo que el flag se resetea al final de accionLanzarDado().
     */
    private boolean dadoLanzado;

    /**
     * Espejo local del estado de la partida (modelo.isPartidaEnCurso()).
     * Se usa para evitar llamadas innecesarias al modelo en cada validacion.
     */
    private boolean partidaActiva;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    /**
     * Constructor de Controlador.
     * Crea las fachadas de vista y modelo, inicializa los flags internos
     * y registra los listeners de los botones principales de la interfaz.
     *
     * @param ventana La ventana principal ya creada y visible.
     */
    public Controlador(VentanaPrincipal ventana) {
        this.vista         = new ViewFacade(ventana);
        this.modelo        = new ModelFacade();
        this.dadoLanzado   = false;
        this.partidaActiva = false;
        registrarListeners();
    }

    // =========================================================================
    // REGISTRO DE LISTENERS
    // =========================================================================

    /**
     * Conecta los ActionListener de los tres botones principales de la interfaz:
     * INICIAR PARTIDA, LANZAR DADO y FINALIZAR PARTIDA.
     * Se usan clases anonimas en lugar de lambdas para compatibilidad con Java 7.
     */
    private void registrarListeners() {

        // Listener del boton INICIAR PARTIDA (panel de configuracion)
        vista.setListenerIniciar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionIniciarPartida();
            }
        });

        // Listener del boton LANZAR DADO (panel de control del juego)
        vista.setListenerLanzarDado(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionLanzarDado();
            }
        });

        // Listener del boton FINALIZAR PARTIDA (panel de control del juego)
        vista.setListenerFinalizar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionFinalizarPartida();
            }
        });
    }

    // =========================================================================
    // ACCIONES PRINCIPALES
    // =========================================================================

    /**
     * Accion del boton INICIAR PARTIDA.
     * Lee los nombres desde la vista, valida que no esten vacios,
     * inicializa la partida en el modelo, habilita el boton de dado,
     * refresca la vista con el estado inicial y navega al panel de juego.
     */
    private void accionIniciarPartida() {
        String[] nombres = vista.obtenerNombresJugadores();

        // Validar que todos los nombres sean no vacios antes de iniciar
        if (!validarNombres(nombres, 0)) {
            System.err.println("[Controlador] Error: hay nombres de jugadores vacios. "
                    + "Completar todos los campos antes de iniciar.");
            return;
        }

        try {
            modelo.iniciarPartida(nombres);
        } catch (PosicionInvalidaException ex) {
            System.err.println("[Controlador] Error al construir el tablero: " + ex.getMessage());
            return;
        } catch (EstructuraVaciaException ex) {
            System.err.println("[Controlador] Error: estructura de datos vacia: " + ex.getMessage());
            return;
        }

        // Inicializar conexiones visuales (serpientes y escaleras) en el tablero
        vista.inicializarConexionesTablero(
                modelo.obtenerConexionesSerpientes(),
                modelo.obtenerConexionesEscaleras()
        );

        dadoLanzado   = false;
        partidaActiva = true;

        // Habilitar el boton de dado para el primer turno
        vista.habilitarBotonDado(true);

        // Mostrar estado inicial en el panel de juego
        JugadorDTO primerJugador = modelo.obtenerJugadorActual();
        String mensajeInicio = "¡Partida iniciada! Turno de: "
                + (primerJugador != null ? primerJugador.getNombre() : "");
        actualizarVista(mensajeInicio);

        // Navegar al panel del juego
        vista.mostrarJuego();
    }

    /**
     * Accion del boton LANZAR DADO.
     * Lanza el dado, muestra su valor inmediatamente en la vista,
     * ejecuta el turno completo del jugador activo, verifica si hay
     * ganador y actualiza la vista con el resultado del movimiento.
     */
    private void accionLanzarDado() {
        // Guardar de no actuar si la partida no esta activa
        if (!partidaActiva) {
            return;
        }

        // Si el dado ya fue lanzado y el turno no se proceso, no lanzar de nuevo.
        // En esta implementacion dado+movimiento ocurren juntos, por lo que
        // este caso no deberia ocurrir en condiciones normales.
        if (dadoLanzado) {
            return;
        }

        // Paso 1: lanzar el dado y mostrar su valor antes del movimiento
        int dado = modelo.lanzarDado();
        CasillaDTO[]  casillasAntes  = modelo.obtenerCasillasTablero();
        JugadorDTO[]  jugadoresAntes = modelo.obtenerTodosJugadores();
        int           indiceAntes    = modelo.obtenerIndiceJugadorActual();
        vista.actualizarTablero(casillasAntes, jugadoresAntes, indiceAntes, dado,
                "Dado: " + dado);

        // Paso 2: ejecutar el turno en el modelo
        String evento;
        try {
            evento = modelo.ejecutarTurno();
        } catch (PartidaNoIniciadaException ex) {
            System.err.println("[Controlador] Error: " + ex.getMessage());
            dadoLanzado = false;
            return;
        } catch (PosicionInvalidaException ex) {
            System.err.println("[Controlador] Error de posicion en el turno: " + ex.getMessage());
            dadoLanzado = false;
            return;
        }

        // Paso 3: verificar si hay ganador tras el movimiento
        JugadorDTO ganador = modelo.verificarGanador();
        if (ganador != null) {
            cerrarPartidaConGanador(ganador);
            dadoLanzado = false;
            return;
        }

        // Paso 4: actualizar la vista con el estado post-movimiento
        JugadorDTO siguiente = modelo.obtenerJugadorActual();
        String mensajeTurno  = evento + " | Turno de: "
                + (siguiente != null ? siguiente.getNombre() : "");
        actualizarVista(mensajeTurno);

        // Resetear el flag para el siguiente turno
        dadoLanzado = false;
    }

    /**
     * Accion del boton FINALIZAR PARTIDA.
     * Determina el lider actual (jugador con mayor posicion en el tablero)
     * usando recursividad y cierra la partida con ese jugador como referencia.
     */
    private void accionFinalizarPartida() {
        if (!partidaActiva) {
            return;
        }

        JugadorDTO[] jugadores = modelo.obtenerTodosJugadores();
        if (jugadores == null || jugadores.length == 0) {
            return;
        }

        // Determinar el lider actual de forma recursiva
        JugadorDTO lider = determinarLiderRecursivo(jugadores, 1, jugadores[0]);
        cerrarPartidaConGanador(lider);
    }

    // =========================================================================
    // METODOS AUXILIARES PRIVADOS
    // =========================================================================

    /**
     * Finaliza la partida con el jugador indicado como ganador:
     * <ol>
     *   <li>Llama a modelo.finalizarPartida para registrar el ranking.</li>
     *   <li>Deshabilita el boton de dado.</li>
     *   <li>Navega a la pantalla de ganador con las estadisticas finales.</li>
     * </ol>
     *
     * @param ganador JugadorDTO del jugador que gano o lidera al finalizar.
     */
    private void cerrarPartidaConGanador(JugadorDTO ganador) {
        modelo.finalizarPartida(ganador);
        partidaActiva = false;
        vista.habilitarBotonDado(false);
        vista.mostrarGanador(
                ganador,
                modelo.obtenerRankingFinal(),
                modelo.getContadorMovimientos(),
                modelo.getContadorSerpientes(),
                modelo.getContadorEscaleras()
        );
    }

    /**
     * Refresca todos los paneles del juego con el estado actual del modelo.
     * Consulta casillas, jugadores, indice del jugador activo y ultimo dado
     * directamente desde el modelo para garantizar coherencia.
     *
     * @param evento Descripcion del evento ocurrido en el ultimo turno.
     */
    private void actualizarVista(String evento) {
        CasillaDTO[] casillas  = modelo.obtenerCasillasTablero();
        JugadorDTO[] jugadores = modelo.obtenerTodosJugadores();
        int          indice    = modelo.obtenerIndiceJugadorActual();
        int          dado      = modelo.getUltimoDado();
        vista.actualizarTablero(casillas, jugadores, indice, dado, evento);
    }

    // =========================================================================
    // METODOS RECURSIVOS
    // =========================================================================

    /**
     * Valida recursivamente que todos los nombres del array sean no nulos
     * y no vacios (despues de aplicar trim()).
     *
     * <p>Caso base: indice igual o mayor que la longitud del array — todos
     * los nombres fueron validados, retorna true.</p>
     * <p>Caso recursivo: si el nombre en la posicion actual es nulo o vacio,
     * retorna false; de lo contrario avanza al siguiente indice.</p>
     *
     * @param nombres Array de nombres ingresados por el usuario.
     * @param indice  Posicion actual del array en la recursion (iniciar con 0).
     * @return true si todos los nombres son validos, false si alguno esta vacio.
     */
    private boolean validarNombres(String[] nombres, int indice) {
        // Caso base: se revisaron todos los nombres sin encontrar invalidos
        if (indice >= nombres.length) {
            return true;
        }
        // Si el nombre actual es nulo o esta en blanco, la validacion falla
        if (nombres[indice] == null || nombres[indice].trim().isEmpty()) {
            return false;
        }
        // Avanzar al siguiente nombre
        return validarNombres(nombres, indice + 1);
    }

    /**
     * Determina recursivamente el jugador con mayor posicion en el tablero.
     * Se usa para seleccionar al lider cuando el usuario finaliza la partida
     * de forma anticipada.
     *
     * <p>Caso base: indice igual o mayor que la longitud del array — retorna
     * el lider acumulado hasta ese punto.</p>
     * <p>Caso recursivo: compara la posicion del jugador en la posicion actual
     * con la del lider actual; el que tenga mayor posicion se convierte en
     * el nuevo lider para la siguiente llamada.</p>
     *
     * @param jugadores   Array completo de jugadores de la partida.
     * @param indice      Posicion actual en el recorrido recursivo (iniciar con 1).
     * @param liderActual JugadorDTO con la mayor posicion encontrada hasta ahora.
     * @return JugadorDTO del jugador con la posicion mas alta en el tablero.
     */
    private JugadorDTO determinarLiderRecursivo(JugadorDTO[] jugadores,
                                                 int indice,
                                                 JugadorDTO liderActual) {
        // Caso base: se recorrieron todos los jugadores
        if (indice >= jugadores.length) {
            return liderActual;
        }
        // Determinar quien es el nuevo lider entre el actual y el candidato
        JugadorDTO candidato = jugadores[indice];
        if (candidato != null
                && candidato.getPosicionActual() > liderActual.getPosicionActual()) {
            return determinarLiderRecursivo(jugadores, indice + 1, candidato);
        }
        return determinarLiderRecursivo(jugadores, indice + 1, liderActual);
    }
}
