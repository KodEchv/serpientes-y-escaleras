package co.edu.unbosque.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel del menu principal del juego "Escaleras y Serpientes a lo Bosque".
 * Muestra el titulo del juego y el boton para iniciar la configuracion de partida.
 *
 */
public class PanelMenuInicio extends JPanel {

    /** Referencia a la ventana principal para navegar entre paneles. */
    private VentanaPrincipal ventana;

    /** Boton que lleva al panel de configuracion. */
    private JButton botonJugar;

    /** Etiqueta con el titulo principal del juego. */
    private JLabel labelTitulo;

    /** Etiqueta con el subtitulo del juego. */
    private JLabel labelSubtitulo;

    /** Etiqueta con el credito institucional. */
    private JLabel labelCredito;

    /**
     * Constructor de PanelMenuInicio.
     *
     * @param ventana Referencia a la ventana principal para navegacion.
     */
    public PanelMenuInicio(VentanaPrincipal ventana) {
        this.ventana = ventana;
        inicializarComponentes();
    }

    /**
     * Inicializa y configura todos los componentes visuales del panel de menu.
     */
    private void inicializarComponentes() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(VentanaPrincipal.COLOR_FONDO_OSCURO);
        setOpaque(true);

        // Espaciado superior
        add(Box.createVerticalGlue());

        // Titulo principal
        labelTitulo = new JLabel("ESCALERAS Y SERPIENTES");
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        labelTitulo.setForeground(new Color(210, 175, 60));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTitulo);

        add(Box.createVerticalStrut(10));

        // Subtitulo
        labelSubtitulo = new JLabel("A LO BOSQUE");
        labelSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        labelSubtitulo.setForeground(VentanaPrincipal.COLOR_TEXTO_CLARO);
        labelSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelSubtitulo);

        add(Box.createVerticalStrut(40));

        // Boton JUGAR
        botonJugar = new JButton("JUGAR");
        botonJugar.setFont(new Font("SansSerif", Font.BOLD, 18));
        botonJugar.setBackground(VentanaPrincipal.COLOR_BOTON_PRIMARIO);
        botonJugar.setForeground(Color.WHITE);
        botonJugar.setBorderPainted(false);
        botonJugar.setFocusPainted(false);
        botonJugar.setOpaque(true);
        botonJugar.setMaximumSize(new Dimension(160, 50));
        botonJugar.setPreferredSize(new Dimension(160, 50));
        botonJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.navegarA(VentanaPrincipal.CLAVE_CONFIG);
            }
        });
        add(botonJugar);

        add(Box.createVerticalGlue());

        // Credito institucional en la parte inferior
        labelCredito = new JLabel("Universidad El Bosque — Estructura de Datos");
        labelCredito.setFont(new Font("SansSerif", Font.PLAIN, 11));
        labelCredito.setForeground(new Color(160, 170, 160));
        labelCredito.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelCredito);

        add(Box.createVerticalStrut(20));
    }
}
