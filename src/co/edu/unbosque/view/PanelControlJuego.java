package co.edu.unbosque.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Panel lateral de control del juego. Agrupa y organiza verticalmente
 * los subpaneles de informacion del jugador, dado, ranking e historial.
 * Tambien contiene el boton para finalizar la partida anticipadamente.
 *
 * @author Estudiante
 * @version 1.0
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

    /** Boton para finalizar la partida antes de tiempo. */
    private JButton botonFinalizar;

    /**
     * Constructor de PanelControlJuego.
     * Inicializa y ensambla todos los subpaneles laterales.
     */
    public PanelControlJuego() {
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

        // Panel del historial (ocupa el espacio restante)
        panelHistorial = new PanelHistorial();
        panelHistorial.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        add(panelHistorial);

        add(Box.createVerticalStrut(5));

        // Boton de finalizar partida
        botonFinalizar = new JButton("FINALIZAR PARTIDA");
        botonFinalizar.setFont(new Font("SansSerif", Font.BOLD, 13));
        botonFinalizar.setBackground(VentanaPrincipal.COLOR_BOTON_PELIGRO);
        botonFinalizar.setForeground(Color.WHITE);
        botonFinalizar.setBorderPainted(false);
        botonFinalizar.setFocusPainted(false);
        botonFinalizar.setOpaque(true);
        botonFinalizar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        botonFinalizar.setMinimumSize(new Dimension(0, 40));
        botonFinalizar.addActionListener(e -> System.out.println("Finalizar partida (stub)"));
        add(botonFinalizar);

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
}
