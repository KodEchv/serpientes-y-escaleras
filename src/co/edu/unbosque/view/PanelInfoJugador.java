package co.edu.unbosque.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import co.edu.unbosque.model.JugadorDTO;

/**
 * Panel que muestra la informacion del jugador cuyo turno esta activo.
 * Dibuja un circulo con el color del jugador, su nombre, posicion actual
 * y los indicadores de escudo y doble turno si los tiene activos.
 *
 */
public class PanelInfoJugador extends JPanel {

    /** Nombre del jugador activo. */
    private String nombreJugador;

    /** Posicion actual del jugador en el tablero. */
    private int posicion;

    /** Indice del jugador (0-3) para determinar su color. */
    private int indiceJugador;

    /** Indica si el jugador tiene escudo activo. */
    private boolean tieneEscudo;

    /** Indica si el jugador tiene doble turno activo. */
    private boolean tieneDobleTurno;

    /**
     * Constructor de PanelInfoJugador.
     * Inicializa el panel con valores por defecto (sin jugador activo).
     */
    public PanelInfoJugador() {
        this.nombreJugador  = "---";
        this.posicion       = 0;
        this.indiceJugador  = 0;
        this.tieneEscudo    = false;
        this.tieneDobleTurno = false;

        setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        setOpaque(true);
        setPreferredSize(new Dimension(0, 110));
    }

    /**
     * Actualiza los datos del jugador mostrado y redibuja el panel.
     *
     * @param jugador El JugadorDTO con la informacion del jugador activo.
     * @param indice  Indice del jugador (0-3) para asignarle su color.
     */
    public void actualizar(JugadorDTO jugador, int indice) {
        if (jugador == null) {
            return;
        }
        this.nombreJugador   = jugador.getNombre();
        this.posicion        = jugador.getPosicionActual();
        this.indiceJugador   = indice;
        this.tieneEscudo     = jugador.isTieneEscudo();
        this.tieneDobleTurno = jugador.isTieneDobleTurno();
        repaint();
    }

    /**
     * Dibuja la informacion del jugador activo sobre el panel.
     * Incluye el circulo de color, nombre, posicion y estados especiales.
     *
     * @param g Contexto grafico proporcionado por Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fondo del panel
        g2.setColor(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Linea de borde inferior
        g2.setColor(VentanaPrincipal.COLOR_BORDE);
        g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

        // Circulo del jugador
        Color colorJugador = (indiceJugador < VentanaPrincipal.COLORES_JUGADORES.length)
                ? VentanaPrincipal.COLORES_JUGADORES[indiceJugador]
                : Color.GRAY;
        g2.setColor(colorJugador);
        g2.fillOval(15, 20, 40, 40);

        // Borde blanco del circulo
        g2.setColor(Color.WHITE);
        g2.setStroke(new java.awt.BasicStroke(2f));
        g2.drawOval(15, 20, 40, 40);

        // Numero del jugador dentro del circulo
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(Color.WHITE);
        String numJugador = String.valueOf(indiceJugador + 1);
        java.awt.FontMetrics fm = g2.getFontMetrics();
        int nx = 15 + (40 - fm.stringWidth(numJugador)) / 2;
        g2.drawString(numJugador, nx, 46);

        // Nombre del jugador
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        g2.drawString(nombreJugador, 65, 35);

        // Posicion actual
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.setColor(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        String textoPosicion = "Casilla: " + (posicion == 0 ? "Inicio" : String.valueOf(posicion));
        g2.drawString(textoPosicion, 65, 52);

        // Indicadores de estado especial
        int xEstado = 65;
        int yEstado = 70;

        if (tieneEscudo) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.setColor(new Color(60, 140, 80));
            g2.drawString("[ESCUDO]", xEstado, yEstado);
            xEstado += 75;
        }

        if (tieneDobleTurno) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.setColor(new Color(180, 130, 20));
            g2.drawString("[x2]", xEstado, yEstado);
        }
    }
}
