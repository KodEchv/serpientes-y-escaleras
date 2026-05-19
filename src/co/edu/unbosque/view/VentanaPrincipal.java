package co.edu.unbosque.view;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Ventana principal del juego "Escaleras y Serpientes a lo Bosque".
 * Gestiona la navegacion entre paneles mediante un CardLayout.
 * Define ademas todas las constantes de color y claves de navegacion
 * usadas por los demas paneles de la vista.
 *
 * @author Estudiante
 * @version 1.0
 */
public class VentanaPrincipal extends JFrame {

    // ---------------------------------------------------------------
    // Constantes de navegacion
    // ---------------------------------------------------------------

    /** Clave para el panel del menu principal. */
    public static final String CLAVE_MENU    = "MENU";

    /** Clave para el panel de configuracion de partida. */
    public static final String CLAVE_CONFIG  = "CONFIG";

    /** Clave para el panel del juego activo. */
    public static final String CLAVE_JUEGO   = "JUEGO";

    /** Clave para el panel de pantalla de ganador. */
    public static final String CLAVE_GANADOR = "GANADOR";

    // ---------------------------------------------------------------
    // Paleta de colores del proyecto
    // ---------------------------------------------------------------

    /** Color de fondo oscuro general (verde bosque oscuro). */
    public static final Color COLOR_FONDO_OSCURO      = new Color(46,  78,  57);

    /** Color de fondo de paneles principales. */
    public static final Color COLOR_FONDO_PANEL       = new Color(230, 228, 218);

    /** Color de fondo de la barra lateral. */
    public static final Color COLOR_FONDO_SIDEBAR     = new Color(200, 198, 188);

    /** Color de casilla normal (turno par). */
    public static final Color COLOR_CASILLA_NORMAL    = new Color(245, 242, 230);

    /** Color alternativo de casilla normal (turno impar). */
    public static final Color COLOR_CASILLA_PAR       = new Color(220, 218, 205);

    /** Color de casilla con serpiente. */
    public static final Color COLOR_CASILLA_SERPIENTE = new Color(160, 80, 50);

    /** Color de casilla con escalera. */
    public static final Color COLOR_CASILLA_ESCALERA  = new Color(100, 165, 90);

    /** Color de casilla con comodin. */
    public static final Color COLOR_CASILLA_COMODIN   = new Color(210, 175, 60);

    /** Color principal para textos oscuros. */
    public static final Color COLOR_TEXTO_OSCURO      = new Color(50,  45,  40);

    /** Color para textos sobre fondos oscuros. */
    public static final Color COLOR_TEXTO_CLARO       = new Color(250, 248, 240);

    /** Color para bordes y lineas del tablero. */
    public static final Color COLOR_BORDE             = new Color(120, 115, 100);

    /** Color de boton de accion principal (verde). */
    public static final Color COLOR_BOTON_PRIMARIO    = new Color(70,  120, 80);

    /** Color de boton de accion peligrosa (rojo). */
    public static final Color COLOR_BOTON_PELIGRO     = new Color(160, 65,  55);

    /** Colores asignados a cada jugador (maximo 4). */
    public static final Color[] COLORES_JUGADORES = {
        new Color(65,  120, 190),
        new Color(200, 70,  60),
        new Color(200, 140, 40),
        new Color(120, 70,  170)
    };

    // ---------------------------------------------------------------
    // Componentes internos
    // ---------------------------------------------------------------

    /** Layout principal que permite cambiar de panel. */
    private CardLayout cardLayout;

    /** Contenedor raiz que administra el CardLayout. */
    private JPanel contenedorPrincipal;

    /** Panel del menu de inicio. */
    private PanelMenuInicio panelMenu;

    /** Panel de configuracion de la partida. */
    private PanelConfiguracion panelConfig;

    /** Panel donde se desarrolla el juego. */
    private PanelJuego panelJuego;

    /** Panel que muestra al ganador al finalizar la partida. */
    private PanelGanador panelGanador;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    /**
     * Constructor de VentanaPrincipal.
     * Crea todos los paneles, configura el CardLayout y muestra el menu inicial.
     */
    public VentanaPrincipal() {
        super("Escaleras y Serpientes a lo Bosque — Universidad El Bosque");
        inicializarComponentes();
    }

    // ---------------------------------------------------------------
    // Inicializacion
    // ---------------------------------------------------------------

    /**
     * Inicializa y ensambla todos los componentes de la ventana principal.
     */
    private void inicializarComponentes() {
        setSize(1100, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout          = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);

        panelMenu    = new PanelMenuInicio(this);
        panelConfig  = new PanelConfiguracion(this);
        panelJuego   = new PanelJuego(this);
        panelGanador = new PanelGanador(this);

        contenedorPrincipal.add(panelMenu,    CLAVE_MENU);
        contenedorPrincipal.add(panelConfig,  CLAVE_CONFIG);
        contenedorPrincipal.add(panelJuego,   CLAVE_JUEGO);
        contenedorPrincipal.add(panelGanador, CLAVE_GANADOR);

        add(contenedorPrincipal);
        cardLayout.show(contenedorPrincipal, CLAVE_MENU);

        setVisible(true);
    }

    // ---------------------------------------------------------------
    // Navegacion
    // ---------------------------------------------------------------

    /**
     * Navega al panel identificado por la clave indicada.
     *
     * @param clave Una de las constantes CLAVE_MENU, CLAVE_CONFIG, CLAVE_JUEGO, CLAVE_GANADOR.
     */
    public void navegarA(String clave) {
        cardLayout.show(contenedorPrincipal, clave);
    }

    // ---------------------------------------------------------------
    // Getters de paneles (para ViewFacade)
    // ---------------------------------------------------------------

    /**
     * Retorna el panel del juego activo.
     *
     * @return PanelJuego actual.
     */
    public PanelJuego getPanelJuego() {
        return panelJuego;
    }

    /**
     * Retorna el panel de configuracion de partida.
     *
     * @return PanelConfiguracion actual.
     */
    public PanelConfiguracion getPanelConfiguracion() {
        return panelConfig;
    }

    /**
     * Retorna el panel de pantalla de ganador.
     *
     * @return PanelGanador actual.
     */
    public PanelGanador getPanelGanador() {
        return panelGanador;
    }
}
