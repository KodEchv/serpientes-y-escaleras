package co.edu.unbosque.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Panel de configuracion de la partida.
 * Permite seleccionar la cantidad de jugadores, sus nombres,
 * y la cantidad de serpientes y escaleras en el tablero.
 *
 * @author Estudiante
 * @version 1.0
 */
public class PanelConfiguracion extends JPanel {

    /** Referencia a la ventana principal para navegacion. */
    private VentanaPrincipal ventana;

    // --- Seccion jugadores ---

    /** Cantidad de jugadores seleccionada (2, 3 o 4). */
    private int cantidadSeleccionada;

    /** Botones de seleccion de cantidad de jugadores (indices 0=2J, 1=3J, 2=4J). */
    private JButton[] botonesNumeroJugadores;

    /** Campos de texto para los nombres de los jugadores (indices 0–3). */
    private JTextField[] camposNombres;

    /** Etiquetas de los campos de nombre. */
    private JLabel[] labelsNombres;

    // --- Seccion tablero ---

    /** ComboBox para elegir la cantidad de serpientes. */
    private JComboBox<Integer> comboSerpientes;

    /** ComboBox para elegir la cantidad de escaleras. */
    private JComboBox<Integer> comboEscaleras;

    // --- Boton iniciar ---

    /** Boton para iniciar la partida con la configuracion seleccionada. */
    private JButton botonIniciar;

    /**
     * Constructor de PanelConfiguracion.
     *
     * @param ventana Referencia a la ventana principal.
     */
    public PanelConfiguracion(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.cantidadSeleccionada = 2;
        inicializarComponentes();
    }

    /**
     * Inicializa y ensambla todos los componentes del panel de configuracion.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        setOpaque(true);

        add(crearHeader(),     BorderLayout.NORTH);
        add(crearFormulario(), BorderLayout.CENTER);
        add(crearFooter(),     BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Header
    // ---------------------------------------------------------------

    /**
     * Crea el panel de cabecera con el titulo y el boton de volver.
     *
     * @return JPanel configurado como header.
     */
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(VentanaPrincipal.COLOR_FONDO_OSCURO);
        header.setPreferredSize(new Dimension(0, 50));
        header.setOpaque(true);

        JLabel labelTitulo = new JLabel("CONFIGURAR PARTIDA", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        labelTitulo.setForeground(Color.WHITE);
        header.add(labelTitulo, BorderLayout.CENTER);

        JButton botonVolver = new JButton("< VOLVER");
        botonVolver.setFont(new Font("SansSerif", Font.PLAIN, 12));
        botonVolver.setBackground(VentanaPrincipal.COLOR_FONDO_OSCURO);
        botonVolver.setForeground(VentanaPrincipal.COLOR_TEXTO_CLARO);
        botonVolver.setBorderPainted(false);
        botonVolver.setFocusPainted(false);
        botonVolver.setOpaque(true);
        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.navegarA(VentanaPrincipal.CLAVE_MENU);
            }
        });
        header.add(botonVolver, BorderLayout.WEST);

        return header;
    }

    // ---------------------------------------------------------------
    // Formulario central
    // ---------------------------------------------------------------

    /**
     * Crea el panel central con las secciones de jugadores y tablero.
     *
     * @return JPanel con el formulario completo.
     */
    private JPanel crearFormulario() {
        JPanel formulario = new JPanel(new GridLayout(2, 1, 10, 10));
        formulario.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        formulario.setOpaque(true);
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        formulario.add(crearSeccionJugadores());
        formulario.add(crearSeccionTablero());

        return formulario;
    }

    /**
     * Crea la seccion de seleccion de jugadores con botones de cantidad y campos de nombre.
     *
     * @return JPanel con la seccion de jugadores.
     */
    private JPanel crearSeccionJugadores() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.COLOR_BORDE),
                "Jugadores",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                VentanaPrincipal.COLOR_TEXTO_OSCURO));

        // Fila 1: selector de cantidad
        JPanel filaCantidad = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filaCantidad.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        filaCantidad.setOpaque(true);

        JLabel labelCantidad = new JLabel("Cantidad:");
        labelCantidad.setFont(new Font("SansSerif", Font.PLAIN, 13));
        labelCantidad.setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        filaCantidad.add(labelCantidad);

        botonesNumeroJugadores = new JButton[3];
        String[] etiquetas = {"2", "3", "4"};
        for (int i = 0; i < 3; i++) {
            final int cantidad = i + 2;
            botonesNumeroJugadores[i] = new JButton(etiquetas[i]);
            botonesNumeroJugadores[i].setFont(new Font("SansSerif", Font.BOLD, 13));
            botonesNumeroJugadores[i].setFocusPainted(false);
            botonesNumeroJugadores[i].setBorderPainted(true);
            botonesNumeroJugadores[i].setPreferredSize(new Dimension(50, 30));
            botonesNumeroJugadores[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cantidadSeleccionada = cantidad;
                    actualizarSeleccionCantidad();
                    actualizarCamposNombres();
                }
            });
            filaCantidad.add(botonesNumeroJugadores[i]);
        }
        panel.add(filaCantidad);

        // Filas 2–5: campos de nombre
        camposNombres = new JTextField[4];
        labelsNombres = new JLabel[4];

        for (int i = 0; i < 4; i++) {
            JPanel filaJugador = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            filaJugador.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
            filaJugador.setOpaque(true);

            labelsNombres[i] = new JLabel("Jugador " + (i + 1) + ":");
            labelsNombres[i].setFont(new Font("SansSerif", Font.PLAIN, 13));
            labelsNombres[i].setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
            labelsNombres[i].setPreferredSize(new Dimension(80, 20));
            filaJugador.add(labelsNombres[i]);

            camposNombres[i] = new JTextField();
            camposNombres[i].setPreferredSize(new Dimension(200, 28));
            camposNombres[i].setFont(new Font("SansSerif", Font.PLAIN, 13));
            filaJugador.add(camposNombres[i]);

            panel.add(filaJugador);
        }

        // Seleccionar "2" por defecto al inicio
        actualizarSeleccionCantidad();
        actualizarCamposNombres();

        return panel;
    }

    /**
     * Crea la seccion de configuracion del tablero (cantidad de serpientes y escaleras).
     *
     * @return JPanel con la seccion de tablero.
     */
    private JPanel crearSeccionTablero() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.COLOR_BORDE),
                "Tablero",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                VentanaPrincipal.COLOR_TEXTO_OSCURO));

        // Fila serpientes
        JPanel filaSerpientes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filaSerpientes.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        filaSerpientes.setOpaque(true);

        JLabel labelSerpientes = new JLabel("Serpientes:");
        labelSerpientes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        labelSerpientes.setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        labelSerpientes.setPreferredSize(new Dimension(100, 20));
        filaSerpientes.add(labelSerpientes);

        Integer[] valoresSerpientes = {8, 9, 10, 11, 12};
        comboSerpientes = new JComboBox<>(valoresSerpientes);
        comboSerpientes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboSerpientes.setPreferredSize(new Dimension(80, 28));
        comboSerpientes.setSelectedItem(10);
        filaSerpientes.add(comboSerpientes);
        panel.add(filaSerpientes);

        // Fila escaleras
        JPanel filaEscaleras = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filaEscaleras.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        filaEscaleras.setOpaque(true);

        JLabel labelEscaleras = new JLabel("Escaleras:");
        labelEscaleras.setFont(new Font("SansSerif", Font.PLAIN, 13));
        labelEscaleras.setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        labelEscaleras.setPreferredSize(new Dimension(100, 20));
        filaEscaleras.add(labelEscaleras);

        Integer[] valoresEscaleras = {8, 9, 10, 11, 12};
        comboEscaleras = new JComboBox<>(valoresEscaleras);
        comboEscaleras.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboEscaleras.setPreferredSize(new Dimension(80, 28));
        comboEscaleras.setSelectedItem(10);
        filaEscaleras.add(comboEscaleras);
        panel.add(filaEscaleras);

        return panel;
    }

    // ---------------------------------------------------------------
    // Footer
    // ---------------------------------------------------------------

    /**
     * Crea el panel inferior con el boton de iniciar partida.
     *
     * @return JPanel configurado como footer.
     */
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        footer.setOpaque(true);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        botonIniciar = new JButton("INICIAR PARTIDA");
        botonIniciar.setFont(new Font("SansSerif", Font.BOLD, 16));
        botonIniciar.setBackground(VentanaPrincipal.COLOR_BOTON_PRIMARIO);
        botonIniciar.setForeground(Color.WHITE);
        botonIniciar.setBorderPainted(false);
        botonIniciar.setFocusPainted(false);
        botonIniciar.setOpaque(true);
        botonIniciar.setPreferredSize(new Dimension(200, 45));
        botonIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Stub: el controlador se inyecta via setListenerIniciar()
                System.out.println("Iniciar: " + cantidadSeleccionada + " jugadores");
            }
        });
        footer.add(botonIniciar);

        return footer;
    }

    // ---------------------------------------------------------------
    // Logica de seleccion de cantidad
    // ---------------------------------------------------------------

    /**
     * Actualiza el estado visual de los botones de seleccion de cantidad de jugadores.
     * El boton activo se resalta con el color primario; los demas quedan en gris claro.
     */
    private void actualizarSeleccionCantidad() {
        for (int i = 0; i < botonesNumeroJugadores.length; i++) {
            if (i + 2 == cantidadSeleccionada) {
                botonesNumeroJugadores[i].setBackground(VentanaPrincipal.COLOR_BOTON_PRIMARIO);
                botonesNumeroJugadores[i].setForeground(Color.WHITE);
            } else {
                botonesNumeroJugadores[i].setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
                botonesNumeroJugadores[i].setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
            }
        }
    }

    /**
     * Muestra u oculta los campos de nombre segun la cantidad de jugadores seleccionada.
     */
    public void actualizarCamposNombres() {
        for (int i = 0; i < 4; i++) {
            boolean visible = i < cantidadSeleccionada;
            camposNombres[i].setVisible(visible);
            labelsNombres[i].setVisible(visible);
        }
        revalidate();
        repaint();
    }

    // ---------------------------------------------------------------
    // Metodos publicos de consulta (para ViewFacade)
    // ---------------------------------------------------------------

    /**
     * Retorna los nombres ingresados para los jugadores activos.
     * Si un campo esta vacio, usa "Jugador N" como nombre por defecto.
     *
     * @return Array de String con los nombres de los jugadores.
     */
    public String[] obtenerNombresJugadores() {
        String[] nombres = new String[cantidadSeleccionada];
        for (int i = 0; i < cantidadSeleccionada; i++) {
            String nombre = camposNombres[i].getText().trim();
            nombres[i] = nombre.isEmpty() ? "Jugador " + (i + 1) : nombre;
        }
        return nombres;
    }

    /**
     * Retorna la cantidad de jugadores seleccionada.
     *
     * @return Numero de jugadores (2, 3 o 4).
     */
    public int obtenerCantidadJugadores() {
        return cantidadSeleccionada;
    }

    /**
     * Retorna la cantidad de serpientes seleccionada en el ComboBox.
     *
     * @return Cantidad de serpientes (8 a 12).
     */
    public int obtenerCantidadSerpientes() {
        return (Integer) comboSerpientes.getSelectedItem();
    }

    /**
     * Retorna la cantidad de escaleras seleccionada en el ComboBox.
     *
     * @return Cantidad de escaleras (8 a 12).
     */
    public int obtenerCantidadEscaleras() {
        return (Integer) comboEscaleras.getSelectedItem();
    }

    /**
     * Conecta un ActionListener externo al boton INICIAR PARTIDA.
     * Reemplaza cualquier listener previo existente en el boton.
     *
     * @param al ActionListener a conectar (proviene del controlador).
     */
    public void setListenerIniciar(ActionListener al) {
        // Eliminar listeners previos para evitar duplicados
        for (ActionListener listener : botonIniciar.getActionListeners()) {
            botonIniciar.removeActionListener(listener);
        }
        botonIniciar.addActionListener(al);
    }
}
