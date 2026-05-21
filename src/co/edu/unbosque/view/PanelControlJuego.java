package co.edu.unbosque.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Panel lateral de control del juego. Agrupa y organiza verticalmente
 * los subpaneles de informacion del jugador, dado, ranking e historial.
 * Tambien contiene el boton de ayuda, el boton para finalizar la partida
 * anticipadamente y el boton para regresar al menu.
 *
 */
public class PanelControlJuego extends JPanel {

    /** Panel que muestra la informacion del jugador activo. */
    private PanelInfoJugador panelInfoJugador;

    /** Panel del dado con boton de lanzamiento. */
    private PanelDado panelDado;

    /** Panel del ranking parcial de la partida. */
    private PanelRanking panelRanking;

    /** Panel del historial de movimientos. */
    private PanelHistorial panelHistorial;

    /** Boton para abrir el dialogo de ayuda. */
    private JButton botonAyuda;

    /** Boton para finalizar la partida antes de tiempo. */
    private JButton botonFinalizar;

    /** Boton para regresar al menu principal. */
    private JButton botonRegresar;

    /**
     * Constructor de PanelControlJuego.
     * Inicializa y ensambla todos los subpaneles laterales.
     *
     * @param ventana Referencia a la ventana principal (no almacenada; el Frame
     *                padre se resuelve en tiempo de ejecucion via SwingUtilities).
     */
    public PanelControlJuego(VentanaPrincipal ventana) {
        inicializarComponentes();
    }

    /**
     * Inicializa y configura todos los componentes del panel lateral de control.
     */
    private void inicializarComponentes() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        setOpaque(true);
        setPreferredSize(new Dimension(265, 0));

        // Boton de ayuda (amarillo dorado, centrado)
        botonAyuda = new JButton("? AYUDA");
        botonAyuda.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonAyuda.setBackground(new Color(218, 165, 32));
        botonAyuda.setForeground(Color.WHITE);
        botonAyuda.setBorderPainted(false);
        botonAyuda.setFocusPainted(false);
        botonAyuda.setOpaque(true);
        botonAyuda.setPreferredSize(new Dimension(220, 32));
        botonAyuda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame framePadre = (Frame) javax.swing.SwingUtilities.getWindowAncestor(
                        PanelControlJuego.this);
                PanelAyuda dialogo = new PanelAyuda(framePadre);
                dialogo.setVisible(true);
            }
        });
        JPanel filaAyuda = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
        filaAyuda.setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        filaAyuda.setOpaque(true);
        filaAyuda.add(botonAyuda);
        add(filaAyuda);

        add(Box.createVerticalStrut(5));

        // Panel de informacion del jugador activo (altura fija 110px)
        panelInfoJugador = new PanelInfoJugador();
        panelInfoJugador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        panelInfoJugador.setMinimumSize(new Dimension(0, 110));
        add(panelInfoJugador);

        add(Box.createVerticalStrut(5));

        // Panel del dado (altura fija 140px)
        panelDado = new PanelDado();
        panelDado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        panelDado.setMinimumSize(new Dimension(0, 140));
        add(panelDado);

        add(Box.createVerticalStrut(5));

        // Panel del ranking (altura fija 120px)
        panelRanking = new PanelRanking();
        panelRanking.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        panelRanking.setMinimumSize(new Dimension(0, 120));
        add(panelRanking);

        add(Box.createVerticalStrut(5));

        // Panel del historial (altura maxima fija 180px)
        panelHistorial = new PanelHistorial();
        panelHistorial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        add(panelHistorial);

        add(Box.createVerticalStrut(5));

        // Boton de finalizar partida (rojo, centrado)
        botonFinalizar = new JButton("FINALIZAR PARTIDA");
        botonFinalizar.setFont(new Font("SansSerif", Font.BOLD, 13));
        botonFinalizar.setBackground(VentanaPrincipal.COLOR_BOTON_PELIGRO);
        botonFinalizar.setForeground(Color.WHITE);
        botonFinalizar.setBorderPainted(false);
        botonFinalizar.setFocusPainted(false);
        botonFinalizar.setOpaque(true);
        botonFinalizar.setPreferredSize(new Dimension(220, 36));
        JPanel filaFinalizar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
        filaFinalizar.setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        filaFinalizar.setOpaque(true);
        filaFinalizar.add(botonFinalizar);
        add(filaFinalizar);

        add(Box.createVerticalStrut(2));

        // Boton de regresar al menu (centrado)
        botonRegresar = new JButton("REGRESAR AL MENU");
        botonRegresar.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonRegresar.setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        botonRegresar.setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        botonRegresar.setBorderPainted(true);
        botonRegresar.setFocusPainted(false);
        botonRegresar.setOpaque(true);
        botonRegresar.setPreferredSize(new Dimension(220, 32));
        JPanel filaRegresar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
        filaRegresar.setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        filaRegresar.setOpaque(true);
        filaRegresar.add(botonRegresar);
        add(filaRegresar);

        add(Box.createVerticalStrut(5));
    }

    // ---------------------------------------------------------------
    // Getters de subpaneles
    // ---------------------------------------------------------------

    /**
     * Retorna el subpanel de informacion del jugador activo.
     *
     * @return PanelInfoJugador del control lateral.
     */
    public PanelInfoJugador getPanelInfoJugador() {
        return panelInfoJugador;
    }

    /**
     * Retorna el subpanel del dado.
     *
     * @return PanelDado del control lateral.
     */
    public PanelDado getPanelDado() {
        return panelDado;
    }

    /**
     * Retorna el subpanel del ranking parcial.
     *
     * @return PanelRanking del control lateral.
     */
    public PanelRanking getPanelRanking() {
        return panelRanking;
    }

    /**
     * Retorna el subpanel del historial de movimientos.
     *
     * @return PanelHistorial del control lateral.
     */
    public PanelHistorial getPanelHistorial() {
        return panelHistorial;
    }

    // ---------------------------------------------------------------
    // Configuracion de listeners externos
    // ---------------------------------------------------------------

    /**
     * Conecta un ActionListener al boton FINALIZAR PARTIDA.
     * Reemplaza cualquier listener previo para evitar duplicados.
     *
     * @param al ActionListener a registrar (proviene del controlador).
     */
    public void setListenerFinalizar(ActionListener al) {
        for (ActionListener listener : botonFinalizar.getActionListeners()) {
            botonFinalizar.removeActionListener(listener);
        }
        botonFinalizar.addActionListener(al);
    }

    /**
     * Conecta un ActionListener al boton REGRESAR AL MENU.
     * Reemplaza cualquier listener previo para evitar duplicados.
     *
     * @param al ActionListener a registrar (proviene del controlador).
     */
    public void setListenerRegresar(ActionListener al) {
        for (ActionListener listener : botonRegresar.getActionListeners()) {
            botonRegresar.removeActionListener(listener);
        }
        botonRegresar.addActionListener(al);
    }
}
