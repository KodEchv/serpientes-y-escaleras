package co.edu.unbosque.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import co.edu.unbosque.model.JugadorDTO;

/**
 * Panel que muestra el ranking parcial de los jugadores durante la partida.
 * Ordena a los jugadores por posicion actual de mayor a menor mediante
 * el algoritmo de burbuja implementado con recursividad.
 * Muestra hasta 4 filas, una por jugador.
 *
 * @author Estudiante
 * @version 1.0
 */
public class PanelRanking extends JPanel {

    /** Etiquetas que muestran la posicion en ranking de cada jugador. */
    private JLabel[] filasRanking;

    /** Numero maximo de jugadores soportados. */
    private static final int MAX_JUGADORES = 4;

    /**
     * Constructor de PanelRanking.
     * Inicializa el panel con filas vacias para cada jugador.
     */
    public PanelRanking() {
        inicializarComponentes();
    }

    /**
     * Inicializa y configura todos los componentes visuales del panel de ranking.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(0, 2));
        setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        setOpaque(true);
        setPreferredSize(new Dimension(0, 120));

        // Titulo del ranking
        JLabel labelTitulo = new JLabel("RANKING", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelTitulo.setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));
        add(labelTitulo, BorderLayout.NORTH);

        // Panel de filas del ranking
        JPanel panelFilas = new JPanel(new java.awt.GridLayout(MAX_JUGADORES, 1, 0, 1));
        panelFilas.setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
        panelFilas.setOpaque(true);

        filasRanking = new JLabel[MAX_JUGADORES];
        for (int i = 0; i < MAX_JUGADORES; i++) {
            filasRanking[i] = new JLabel(" ");
            filasRanking[i].setFont(new Font("Monospaced", Font.PLAIN, 11));
            filasRanking[i].setForeground(VentanaPrincipal.COLOR_TEXTO_OSCURO);
            filasRanking[i].setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 4));
            filasRanking[i].setOpaque(true);
            filasRanking[i].setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
            panelFilas.add(filasRanking[i]);
        }
        add(panelFilas, BorderLayout.CENTER);
    }

    /**
     * Actualiza el ranking con el estado actual de todos los jugadores.
     * Ordena los jugadores por posicion de mayor a menor (burbuja recursiva)
     * y actualiza las etiquetas de cada fila.
     *
     * @param jugadores Array de JugadorDTO con el estado actual de los jugadores.
     */
    public void actualizar(JugadorDTO[] jugadores) {
        if (jugadores == null || jugadores.length == 0) {
            return;
        }

        // Copiar el array para no modificar el original al ordenar
        JugadorDTO[] ordenados = new JugadorDTO[jugadores.length];
        for (int i = 0; i < jugadores.length; i++) {
            ordenados[i] = jugadores[i];
        }

        // Ordenar por posicion de mayor a menor con burbuja recursiva
        ordenarBurbuja(ordenados, ordenados.length);

        // Actualizar etiquetas de ranking
        for (int i = 0; i < MAX_JUGADORES; i++) {
            if (i < ordenados.length) {
                String posText = ordenados[i].getPosicionActual() == 0
                        ? "ini"
                        : "cas." + ordenados[i].getPosicionActual();
                String texto = "#" + (i + 1) + "  " + ordenados[i].getNombre() + " — " + posText;
                filasRanking[i].setText(texto);

                // Resaltar al lider con fondo amarillo suave
                if (i == 0) {
                    filasRanking[i].setBackground(new Color(245, 235, 180));
                    filasRanking[i].setFont(new Font("Monospaced", Font.BOLD, 11));
                } else {
                    filasRanking[i].setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
                    filasRanking[i].setFont(new Font("Monospaced", Font.PLAIN, 11));
                }
                filasRanking[i].setVisible(true);
            } else {
                filasRanking[i].setText(" ");
                filasRanking[i].setBackground(VentanaPrincipal.COLOR_FONDO_SIDEBAR);
                filasRanking[i].setVisible(true);
            }
        }
    }

    /**
     * Ordena el array de jugadores por posicion actual de mayor a menor
     * usando el algoritmo de burbuja implementado de forma recursiva.
     *
     * @param jugadores Array a ordenar.
     * @param n         Numero de elementos a considerar en esta pasada.
     */
    private void ordenarBurbuja(JugadorDTO[] jugadores, int n) {
        if (n <= 1) {
            return; // Caso base: array de un elemento ya esta ordenado
        }
        burbujaPasada(jugadores, 0, n - 1);
        ordenarBurbuja(jugadores, n - 1); // Recursion sobre el subarreglo reducido
    }

    /**
     * Realiza una pasada del algoritmo de burbuja de forma recursiva.
     * Mueve el elemento mas pequeno al final de la seccion activa.
     *
     * @param jugadores Array de jugadores.
     * @param i         Indice actual de la comparacion.
     * @param limite    Limite de la pasada actual.
     */
    private void burbujaPasada(JugadorDTO[] jugadores, int i, int limite) {
        if (i >= limite) {
            return; // Caso base: fin de la pasada
        }
        if (jugadores[i].getPosicionActual() < jugadores[i + 1].getPosicionActual()) {
            JugadorDTO temp   = jugadores[i];
            jugadores[i]      = jugadores[i + 1];
            jugadores[i + 1]  = temp;
        }
        burbujaPasada(jugadores, i + 1, limite); // Siguiente comparacion
    }
}
