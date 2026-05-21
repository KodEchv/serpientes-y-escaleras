package co.edu.unbosque.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * Dialogo modal de ayuda del juego "Escaleras y Serpientes a lo Bosque".
 * Muestra la leyenda de colores del tablero y la guia de efectos de comodines.
 *
 */
public class PanelAyuda extends JDialog {

    /**
     * Constructor de PanelAyuda.
     *
     * @param padre Frame padre de la ventana principal.
     */
    public PanelAyuda(Frame padre) {
        super(padre, "Ayuda — Escaleras y Serpientes a lo Bosque", true);
        inicializarComponentes();
    }

    /**
     * Inicializa y ensambla todos los componentes del dialogo de ayuda.
     */
    private void inicializarComponentes() {
        setSize(480, 530);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        // --- Header ---
        JLabel titulo = new JLabel("GUIA DEL JUEGO", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setOpaque(true);
        titulo.setBackground(VentanaPrincipal.COLOR_FONDO_OSCURO);
        titulo.setPreferredSize(new Dimension(0, 45));
        add(titulo, BorderLayout.NORTH);

        // --- Contenido con scroll ---
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        contenido.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Seccion colores
        contenido.add(crearTituloSeccion("COLORES DEL TABLERO"));
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(crearFilaLeyenda(VentanaPrincipal.COLOR_CASILLA_NORMAL,
                "Normal", "Casilla sin efecto especial"));
        contenido.add(crearFilaLeyenda(VentanaPrincipal.COLOR_CASILLA_SERPIENTE,
                "Serpiente", "Caes aqui: bajas a la cola de la serpiente"));
        contenido.add(crearFilaLeyenda(VentanaPrincipal.COLOR_CASILLA_ESCALERA,
                "Escalera", "Caes aqui: subes a la cima de la escalera"));
        contenido.add(crearFilaLeyenda(VentanaPrincipal.COLOR_CASILLA_COMODIN,
                "Comodin", "Caes aqui: lanzas un dado especial (ver abajo)"));
        contenido.add(Box.createVerticalStrut(16));

        // Seccion comodines
        contenido.add(crearTituloSeccion("EFECTOS DE LOS COMODINES"));
        contenido.add(Box.createVerticalStrut(4));
        JLabel subComodin = new JLabel("Al caer en una casilla comodin, lanza un dado adicional (1-6):");
        subComodin.setFont(new Font("SansSerif", Font.ITALIC, 12));
        subComodin.setAlignmentX(LEFT_ALIGNMENT);
        contenido.add(subComodin);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(crearFilaComodin("1", "Escalera cercana",
                "Te mueves a la base de la escalera mas proxima"));
        contenido.add(crearFilaComodin("2", "Serpiente cercana",
                "Te mueves a la cabeza de la serpiente mas proxima"));
        contenido.add(crearFilaComodin("3", "Avanzar 10",
                "Avanzas 10 casillas (maximo casilla 100)"));
        contenido.add(crearFilaComodin("4", "Retroceder 10",
                "Retrocedes 10 casillas (minimo casilla 1)"));
        contenido.add(crearFilaComodin("5", "Avanzar x2",
                "Tu posicion se duplica (maximo casilla 100)"));
        contenido.add(crearFilaComodin("6", "Retroceder x2",
                "Tu posicion se divide a la mitad (minimo casilla 1)"));
        contenido.add(Box.createVerticalStrut(16));

        // Seccion colisiones
        contenido.add(crearTituloSeccion("COLISIONES ENTRE JUGADORES"));
        contenido.add(Box.createVerticalStrut(4));
        JLabel txtColision = new JLabel(
                "<html><body style='width:400px'>"
                + "Si caes en la misma casilla que otro jugador, ambos lanzan un dado. "
                + "El que saque el numero menor (o el recien llegado en empate) pierde: "
                + "baja a la serpiente mas cercana hacia atras, o vuelve a la casilla 1 "
                + "si no hay serpiente disponible."
                + "</body></html>");
        txtColision.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtColision.setAlignmentX(LEFT_ALIGNMENT);
        contenido.add(txtColision);

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // --- Footer: boton cerrar ---
        JButton botonCerrar = new JButton("CERRAR");
        botonCerrar.setFont(new Font("SansSerif", Font.BOLD, 13));
        botonCerrar.setBackground(VentanaPrincipal.COLOR_BOTON_PRIMARIO);
        botonCerrar.setForeground(Color.WHITE);
        botonCerrar.setBorderPainted(false);
        botonCerrar.setFocusPainted(false);
        botonCerrar.setOpaque(true);
        botonCerrar.setPreferredSize(new Dimension(130, 38));
        botonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        footer.add(botonCerrar);
        add(footer, BorderLayout.SOUTH);
    }

    /**
     * Crea una etiqueta de titulo de seccion con subrayado.
     *
     * @param texto Texto del titulo.
     * @return JLabel estilizado como titulo de seccion.
     */
    private JLabel crearTituloSeccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(VentanaPrincipal.COLOR_FONDO_OSCURO);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, VentanaPrincipal.COLOR_BORDE));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Crea una fila de la leyenda de colores con un cuadrado de color y texto.
     *
     * @param color       Color del tipo de casilla.
     * @param nombre      Nombre del tipo.
     * @param descripcion Descripcion del efecto.
     * @return JPanel con la fila de leyenda.
     */
    private JPanel crearFilaLeyenda(final Color color, String nombre, String descripcion) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
        fila.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        fila.setAlignmentX(LEFT_ALIGNMENT);

        JPanel cuadro = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(color);
                g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
                g.setColor(VentanaPrincipal.COLOR_BORDE);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
            }
        };
        cuadro.setPreferredSize(new Dimension(16, 16));
        cuadro.setOpaque(false);
        fila.add(cuadro);

        JLabel lbl = new JLabel("<html><b>" + nombre + ":</b> " + descripcion + "</html>");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        fila.add(lbl);
        return fila;
    }

    /**
     * Crea una fila del listado de efectos de comodin.
     *
     * @param dado        Valor del dado (1-6).
     * @param nombre      Nombre del efecto.
     * @param descripcion Descripcion del efecto.
     * @return JPanel con la fila del comodin.
     */
    private JPanel crearFilaComodin(String dado, String nombre, String descripcion) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
        fila.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        fila.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblDado = new JLabel(dado, SwingConstants.CENTER);
        lblDado.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblDado.setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        lblDado.setOpaque(true);
        lblDado.setBackground(VentanaPrincipal.COLOR_CASILLA_COMODIN);
        lblDado.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.COLOR_BORDE, 1));
        lblDado.setPreferredSize(new Dimension(22, 22));
        fila.add(lblDado);

        JLabel lbl = new JLabel("<html><b>" + nombre + ":</b> " + descripcion + "</html>");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        fila.add(lbl);
        return fila;
    }
}
