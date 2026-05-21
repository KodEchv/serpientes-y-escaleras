package co.edu.unbosque.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Panel que muestra el historial de movimientos y eventos ocurridos durante la partida.
 * Los eventos se agregan cronologicamente y el panel hace scroll automatico
 * hacia el evento mas reciente.
 *
 */
public class PanelHistorial extends JPanel {

    /** Area de texto donde se muestran los eventos del historial. */
    private JTextArea areaHistorial;

    /**
     * Constructor de PanelHistorial.
     * Inicializa el panel con el area de texto y el scroll pane.
     */
    public PanelHistorial() {
        inicializarComponentes();
    }

    /**
     * Inicializa y configura todos los componentes visuales del panel de historial.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 2));
        setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        setOpaque(true);

        // Titulo del historial
        JLabel labelTitulo = new JLabel("HISTORIAL", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelTitulo.setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));
        add(labelTitulo, BorderLayout.NORTH);

        // Area de texto para los eventos
        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaHistorial.setBackground(new Color(245, 242, 230));
        areaHistorial.setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        areaHistorial.setLineWrap(true);
        areaHistorial.setWrapStyleWord(true);
        areaHistorial.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        // Scroll pane con barra vertical automatica
        JScrollPane scrollPane = new JScrollPane(areaHistorial);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.COLOR_BORDE));
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Agrega un nuevo evento al historial y desplaza la vista al final del texto.
     *
     * @param evento Texto del evento a registrar (un turno, una serpiente, una escalera, etc.).
     */
    public void agregarEvento(String evento) {
        if (evento == null || evento.isEmpty()) {
            return;
        }
        areaHistorial.append(evento + "\n");
        // Auto-scroll al ultimo evento
        areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
    }

    /**
     * Limpia todo el contenido del historial.
     * Util al iniciar una nueva partida.
     */
    public void limpiar() {
        areaHistorial.setText("");
    }
}
