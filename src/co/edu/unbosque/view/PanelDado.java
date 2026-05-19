package co.edu.unbosque.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel que muestra el dado del juego y el boton para lanzarlo.
 * Dibuja un cuadrado redondeado con el valor actual del dado.
 * Si no se ha lanzado aun, muestra un signo de interrogacion.
 *
 * @author Estudiante
 * @version 1.0
 */
public class PanelDado extends JPanel {

    /** Valor actual del dado (0 indica que aun no se ha lanzado). */
    private int valorDado;

    /** Boton para lanzar el dado. */
    private JButton botonLanzar;

    /**
     * Constructor de PanelDado.
     * Inicializa el panel con valor de dado en 0 (sin lanzar).
     */
    public PanelDado() {
        this.valorDado = 0;
        inicializarComponentes();
    }

    /**
     * Inicializa y configura todos los componentes visuales del panel del dado.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 5));
        setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        setOpaque(true);

        // --- NORTH: etiqueta de titulo ---
        JLabel labelTitulo = new JLabel("DADO", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelTitulo.setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        add(labelTitulo, BorderLayout.NORTH);

        // --- CENTER: panel de dibujo del dado ---
        JPanel panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarDado(g);
            }
        };
        panelDibujo.setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        panelDibujo.setOpaque(true);
        panelDibujo.setPreferredSize(new Dimension(0, 100));
        add(panelDibujo, BorderLayout.CENTER);

        // --- SOUTH: boton lanzar ---
        botonLanzar = new JButton("LANZAR DADO");
        botonLanzar.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonLanzar.setBackground(VentanaPrincipal.COLOR_BOTON_PRIMARIO);
        botonLanzar.setForeground(Color.WHITE);
        botonLanzar.setBorderPainted(false);
        botonLanzar.setFocusPainted(false);
        botonLanzar.setOpaque(true);
        add(botonLanzar, BorderLayout.SOUTH);
    }

    /**
     * Dibuja el cuadrado redondeado del dado con el valor actual.
     * Si el valor es 0, muestra "?" en lugar del numero.
     *
     * @param g Contexto grafico proporcionado por Swing.
     */
    private void dibujarDado(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int panelAncho  = g.getClipBounds() != null ? g.getClipBounds().width  : getWidth();
        int panelAlto   = g.getClipBounds() != null ? g.getClipBounds().height : getHeight();
        int x           = (panelAncho - 80) / 2;
        int y           = (panelAlto  - 80) / 2;

        // Relleno del dado
        g2.setColor(new Color(240, 238, 225));
        g2.fillRoundRect(x, y, 80, 80, 20, 20);

        // Borde del dado
        g2.setColor(VentanaPrincipal.COLOR_BORDE);
        g2.setStroke(new java.awt.BasicStroke(2f));
        g2.drawRoundRect(x, y, 80, 80, 20, 20);

        // Numero o signo de interrogacion
        g2.setFont(new Font("SansSerif", Font.BOLD, 36));
        String texto = (valorDado == 0) ? "?" : String.valueOf(valorDado);
        Color colorTexto = (valorDado == 0)
                ? new Color(160, 155, 140)
                : VentanaPrincipal.COLOR_TEXTO_OSCURO;
        g2.setColor(colorTexto);

        // Centrar el texto dentro del cuadrado
        java.awt.FontMetrics fm = g2.getFontMetrics();
        int textoAncho = fm.stringWidth(texto);
        int textoAlto  = fm.getAscent();
        g2.drawString(texto, x + (80 - textoAncho) / 2, y + (80 + textoAlto) / 2 - 4);
    }

    // ---------------------------------------------------------------
    // Metodos publicos
    // ---------------------------------------------------------------

    /**
     * Actualiza el valor mostrado en el dado y redibuja el panel.
     *
     * @param valor Nuevo valor del dado (1-6). Usar 0 para mostrar "?".
     */
    public void setValorDado(int valor) {
        this.valorDado = valor;
        repaint();
    }

    /**
     * Habilita o deshabilita el boton de lanzar dado.
     *
     * @param b true para habilitar el boton, false para deshabilitarlo.
     */
    public void habilitarBoton(boolean b) {
        botonLanzar.setEnabled(b);
    }

    /**
     * Conecta un ActionListener externo al boton LANZAR DADO.
     * Util para que el controlador reaccione al evento de lanzamiento.
     *
     * @param al ActionListener a registrar en el boton.
     */
    public void setListenerLanzar(ActionListener al) {
        for (ActionListener listener : botonLanzar.getActionListeners()) {
            botonLanzar.removeActionListener(listener);
        }
        botonLanzar.addActionListener(al);
    }
}
