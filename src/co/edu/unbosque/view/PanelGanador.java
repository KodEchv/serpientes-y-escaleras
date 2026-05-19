package co.edu.unbosque.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.RankingDTO;

/**
 * Panel de pantalla final que muestra el ganador de la partida,
 * el ranking completo ordenado, estadisticas globales del juego
 * y botones para iniciar una nueva partida o salir.
 *
 * @author Estudiante
 * @version 1.0
 */
public class PanelGanador extends JPanel {

    /** Referencia a la ventana principal para navegacion. */
    private VentanaPrincipal ventana;

    /** Etiqueta con el nombre del jugador ganador. */
    private JLabel labelNombreGanador;

    /** Etiqueta con informacion de turnos del ganador. */
    private JLabel labelTurnosGanador;

    /** Panel que contiene las filas del ranking final. */
    private JPanel panelRankingFinal;

    /** Etiqueta de estadistica: total de movimientos. */
    private JLabel labelMovimientos;

    /** Etiqueta de estadistica: total de serpientes caidas. */
    private JLabel labelSerpientes;

    /** Etiqueta de estadistica: total de escaleras subidas. */
    private JLabel labelEscaleras;

    /**
     * Constructor de PanelGanador.
     *
     * @param ventana Referencia a la ventana principal.
     */
    public PanelGanador(VentanaPrincipal ventana) {
        this.ventana = ventana;
        inicializarComponentes();
    }

    /**
     * Inicializa y configura los componentes visuales del panel de ganador.
     */
    private void inicializarComponentes() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(VentanaPrincipal.COLOR_FONDO_OSCURO);
        setOpaque(true);

        add(Box.createVerticalGlue());

        // --- Titulo ---
        JLabel labelTitulo = new JLabel("¡TENEMOS GANADOR!");
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        labelTitulo.setForeground(new Color(210, 175, 60));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTitulo);

        add(Box.createVerticalStrut(10));

        // --- Nombre del ganador ---
        labelNombreGanador = new JLabel("---");
        labelNombreGanador.setFont(new Font("SansSerif", Font.BOLD, 36));
        labelNombreGanador.setForeground(Color.WHITE);
        labelNombreGanador.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelNombreGanador);

        add(Box.createVerticalStrut(8));

        // --- Turnos del ganador ---
        labelTurnosGanador = new JLabel("Llego a la casilla 100 en 0 turnos");
        labelTurnosGanador.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelTurnosGanador.setForeground(VentanaPrincipal.COLOR_TEXTO_CLARO);
        labelTurnosGanador.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(labelTurnosGanador);

        add(Box.createVerticalStrut(20));

        // --- Panel de ranking final ---
        panelRankingFinal = new JPanel();
        panelRankingFinal.setBackground(new Color(36, 60, 44));
        panelRankingFinal.setOpaque(true);
        panelRankingFinal.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelRankingFinal.setMaximumSize(new Dimension(450, 120));
        add(panelRankingFinal);

        add(Box.createVerticalStrut(15));

        // --- Estadisticas ---
        JPanel panelEstadisticas = new JPanel(new GridLayout(1, 3, 20, 0));
        panelEstadisticas.setBackground(VentanaPrincipal.COLOR_FONDO_OSCURO);
        panelEstadisticas.setOpaque(true);
        panelEstadisticas.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEstadisticas.setMaximumSize(new Dimension(480, 40));

        labelMovimientos = crearLabelEstadistica("Movimientos: 0");
        labelSerpientes  = crearLabelEstadistica("Serpientes: 0");
        labelEscaleras   = crearLabelEstadistica("Escaleras: 0");

        panelEstadisticas.add(labelMovimientos);
        panelEstadisticas.add(labelSerpientes);
        panelEstadisticas.add(labelEscaleras);
        add(panelEstadisticas);

        add(Box.createVerticalStrut(25));

        // --- Botones de accion ---
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBackground(VentanaPrincipal.COLOR_FONDO_OSCURO);
        panelBotones.setOpaque(true);
        panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton botonNuevaPartida = new JButton("NUEVA PARTIDA");
        botonNuevaPartida.setFont(new Font("SansSerif", Font.BOLD, 14));
        botonNuevaPartida.setBackground(VentanaPrincipal.COLOR_BOTON_PRIMARIO);
        botonNuevaPartida.setForeground(Color.WHITE);
        botonNuevaPartida.setBorderPainted(false);
        botonNuevaPartida.setFocusPainted(false);
        botonNuevaPartida.setOpaque(true);
        botonNuevaPartida.setPreferredSize(new Dimension(180, 45));
        botonNuevaPartida.setMaximumSize(new Dimension(180, 45));
        botonNuevaPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.navegarA(VentanaPrincipal.CLAVE_MENU);
            }
        });

        JButton botonSalir = new JButton("SALIR");
        botonSalir.setFont(new Font("SansSerif", Font.BOLD, 14));
        botonSalir.setBackground(VentanaPrincipal.COLOR_BOTON_PELIGRO);
        botonSalir.setForeground(Color.WHITE);
        botonSalir.setBorderPainted(false);
        botonSalir.setFocusPainted(false);
        botonSalir.setOpaque(true);
        botonSalir.setPreferredSize(new Dimension(120, 45));
        botonSalir.setMaximumSize(new Dimension(120, 45));
        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelBotones.add(botonNuevaPartida);
        panelBotones.add(Box.createHorizontalStrut(20));
        panelBotones.add(botonSalir);
        add(panelBotones);

        add(Box.createVerticalGlue());
    }

    /**
     * Crea una etiqueta de estadistica con estilo uniforme.
     *
     * @param texto Texto inicial de la etiqueta.
     * @return JLabel configurado para mostrar estadisticas.
     */
    private JLabel crearLabelEstadistica(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(VentanaPrincipal.COLOR_TEXTO_CLARO);
        return label;
    }

    /**
     * Carga los datos de la pantalla de ganador y actualiza todos los componentes.
     * El array rankingOrdenado debe contener objetos RankingDTO ordenados
     * de mejor a peor (proveniente de AVLTree.inorderArray() en orden descendente).
     *
     * @param ganador          JugadorDTO del jugador ganador.
     * @param rankingOrdenado  Array de Object (RankingDTO) con el ranking final ordenado.
     * @param totalMovimientos Cantidad total de movimientos en la partida.
     * @param totalSerpientes  Cantidad de veces que alguien cayo en una serpiente.
     * @param totalEscaleras   Cantidad de veces que alguien subio por una escalera.
     */
    public void cargar(JugadorDTO ganador, Object[] rankingOrdenado,
                       int totalMovimientos, int totalSerpientes, int totalEscaleras) {

        // Actualizar nombre y turnos del ganador
        if (ganador != null) {
            labelNombreGanador.setText(ganador.getNombre());
            labelTurnosGanador.setText(
                "Llego a la casilla 100 en " + ganador.getCantidadTurnos() + " turnos");
        }

        // Reconstruir el panel de ranking final
        panelRankingFinal.removeAll();
        int cantidadFilas = (rankingOrdenado != null) ? rankingOrdenado.length : 0;
        if (cantidadFilas > 0) {
            panelRankingFinal.setLayout(new GridLayout(cantidadFilas, 1, 0, 2));
            for (int i = 0; i < rankingOrdenado.length; i++) {
                JLabel filaLabel = construirFilaRanking(rankingOrdenado[i], i);
                panelRankingFinal.add(filaLabel);
            }
        }
        panelRankingFinal.revalidate();
        panelRankingFinal.repaint();

        // Actualizar estadisticas
        labelMovimientos.setText("Movimientos: " + totalMovimientos);
        labelSerpientes.setText("Serpientes: "   + totalSerpientes);
        labelEscaleras.setText("Escaleras: "     + totalEscaleras);
    }

    /**
     * Construye la etiqueta de una fila del ranking final a partir de un objeto RankingDTO.
     *
     * @param entrada Objeto de tipo RankingDTO (recibido como Object desde el AVLTree).
     * @param posicion Posicion en el ranking (0 = primero).
     * @return JLabel con el texto formateado de la entrada del ranking.
     */
    private JLabel construirFilaRanking(Object entrada, int posicion) {
        String texto;
        boolean esGanador = false;

        if (entrada instanceof RankingDTO) {
            RankingDTO rankingDTO = (RankingDTO) entrada;
            String estado = rankingDTO.isGano()
                    ? "GANADOR"
                    : "cas. " + rankingDTO.getPosicionFinal();
            texto = "#" + (posicion + 1) + "  " + rankingDTO.getNombreJugador()
                    + "  —  " + estado
                    + "  (" + rankingDTO.getCantidadTurnos() + " turnos)";
            esGanador = rankingDTO.isGano();
        } else {
            texto = "#" + (posicion + 1) + "  " + String.valueOf(entrada);
        }

        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setFont(new Font("Monospaced", Font.BOLD, 12));
        label.setForeground(esGanador ? new Color(210, 175, 60) : VentanaPrincipal.COLOR_TEXTO_CLARO);
        label.setOpaque(false);
        return label;
    }
}
