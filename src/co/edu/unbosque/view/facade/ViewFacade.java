package co.edu.unbosque.view.facade;

import java.awt.event.ActionListener;

import co.edu.unbosque.model.CasillaDTO;
import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.view.VentanaPrincipal;

/**
 * Fachada de la capa de vista del juego "Escaleras y Serpientes a lo Bosque".
 * Centraliza todas las operaciones de actualizacion y navegacion entre paneles,
 * de modo que el controlador no interactue directamente con los componentes Swing.
 *
 * <p>El controlador unicamente usa esta clase para:</p>
 * <ul>
 *   <li>Navegar entre paneles (menu, configuracion, juego, ganador).</li>
 *   <li>Actualizar el estado visual del tablero y los jugadores.</li>
 *   <li>Registrar listeners de los botones principales.</li>
 *   <li>Leer la configuracion ingresada por el usuario.</li>
 * </ul>
 *
 * @author Estudiante
 * @version 1.0
 */
public class ViewFacade {

    /** Referencia a la ventana principal que contiene todos los paneles. */
    private VentanaPrincipal ventana;

    /**
     * Constructor de ViewFacade.
     *
     * @param ventana Referencia a la VentanaPrincipal ya inicializada.
     */
    public ViewFacade(VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    // ---------------------------------------------------------------
    // Navegacion entre paneles
    // ---------------------------------------------------------------

    /**
     * Navega al panel del menu principal.
     */
    public void mostrarMenu() {
        ventana.navegarA(VentanaPrincipal.CLAVE_MENU);
    }

    /**
     * Navega al panel de configuracion de partida.
     */
    public void mostrarConfiguracion() {
        ventana.navegarA(VentanaPrincipal.CLAVE_CONFIG);
    }

    /**
     * Navega al panel del juego activo.
     */
    public void mostrarJuego() {
        ventana.navegarA(VentanaPrincipal.CLAVE_JUEGO);
    }

    // ---------------------------------------------------------------
    // Actualizacion del estado del juego
    // ---------------------------------------------------------------

    /**
     * Actualiza el tablero y todos los subpaneles del juego con el estado del turno actual.
     *
     * @param casillas      Array de CasillaDTO con el estado de cada casilla (indice 1-100).
     * @param jugadores     Array de JugadorDTO con el estado actual de los jugadores.
     * @param indiceActual  Indice del jugador cuyo turno esta activo.
     * @param dado          Valor del dado lanzado en este turno (0 si no se ha lanzado).
     * @param evento        Descripcion del evento ocurrido en este turno.
     */
    public void actualizarTablero(CasillaDTO[] casillas, JugadorDTO[] jugadores,
                                   int indiceActual, int dado, String evento) {
        ventana.getPanelJuego().actualizarEstado(casillas, jugadores, indiceActual, dado, evento);
    }

    /**
     * Carga la pantalla de ganador con los datos finales de la partida y navega a ella.
     *
     * @param ganador          JugadorDTO del jugador que gano.
     * @param ranking          Array de Object (RankingDTO) con el ranking final ordenado.
     * @param movimientos      Total de movimientos realizados en la partida.
     * @param serpientes       Total de veces que alguien cayo en una serpiente.
     * @param escaleras        Total de veces que alguien subio por una escalera.
     */
    public void mostrarGanador(JugadorDTO ganador, Object[] ranking,
                                int movimientos, int serpientes, int escaleras) {
        ventana.getPanelGanador().cargar(ganador, ranking, movimientos, serpientes, escaleras);
        ventana.navegarA(VentanaPrincipal.CLAVE_GANADOR);
    }

    /**
     * Inicializa las conexiones visuales del tablero una sola vez al iniciar la partida.
     * Delega la operacion al PanelJuego de la ventana principal.
     *
     * @param serpientes Array de pares {cabeza, cola} de cada serpiente en el tablero.
     * @param escaleras  Array de pares {base, cima} de cada escalera en el tablero.
     */
    public void inicializarConexionesTablero(int[][] serpientes, int[][] escaleras) {
        ventana.getPanelJuego().inicializarConexionesTablero(serpientes, escaleras);
    }

    /**
     * Habilita o deshabilita el boton de lanzar dado.
     *
     * @param habilitado true para habilitar el boton, false para deshabilitarlo.
     */
    public void habilitarBotonDado(boolean habilitado) {
        ventana.getPanelJuego()
               .getPanelControlJuego()
               .getPanelDado()
               .habilitarBoton(habilitado);
    }

    // ---------------------------------------------------------------
    // Registro de listeners externos (desde el controlador)
    // ---------------------------------------------------------------

    /**
     * Conecta un ActionListener al boton LANZAR DADO.
     *
     * @param al ActionListener del controlador que maneja el lanzamiento.
     */
    public void setListenerLanzarDado(ActionListener al) {
        ventana.getPanelJuego()
               .getPanelControlJuego()
               .getPanelDado()
               .setListenerLanzar(al);
    }

    /**
     * Conecta un ActionListener al boton FINALIZAR PARTIDA.
     *
     * @param al ActionListener del controlador que maneja la finalizacion.
     */
    public void setListenerFinalizar(ActionListener al) {
        ventana.getPanelJuego()
               .getPanelControlJuego()
               .setListenerFinalizar(al);
    }

    /**
     * Conecta un ActionListener al boton INICIAR PARTIDA del panel de configuracion.
     *
     * @param al ActionListener del controlador que inicia la partida.
     */
    public void setListenerIniciar(ActionListener al) {
        ventana.getPanelConfiguracion().setListenerIniciar(al);
    }

    // ---------------------------------------------------------------
    // Lectura de configuracion del usuario
    // ---------------------------------------------------------------

    /**
     * Retorna los nombres de los jugadores ingresados en el panel de configuracion.
     *
     * @return Array de String con los nombres (uno por jugador activo).
     */
    public String[] obtenerNombresJugadores() {
        return ventana.getPanelConfiguracion().obtenerNombresJugadores();
    }

    /**
     * Retorna la cantidad de jugadores seleccionada en el panel de configuracion.
     *
     * @return Numero de jugadores (2, 3 o 4).
     */
    public int obtenerCantidadJugadores() {
        return ventana.getPanelConfiguracion().obtenerCantidadJugadores();
    }

    /**
     * Retorna la cantidad de serpientes configurada en el panel de configuracion.
     *
     * @return Cantidad de serpientes (8 a 12).
     */
    public int obtenerCantidadSerpientes() {
        return ventana.getPanelConfiguracion().obtenerCantidadSerpientes();
    }

    /**
     * Retorna la cantidad de escaleras configurada en el panel de configuracion.
     *
     * @return Cantidad de escaleras (8 a 12).
     */
    public int obtenerCantidadEscaleras() {
        return ventana.getPanelConfiguracion().obtenerCantidadEscaleras();
    }
}
