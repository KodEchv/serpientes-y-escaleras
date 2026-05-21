package co.edu.unbosque.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import co.edu.unbosque.model.CasillaDTO;
import co.edu.unbosque.model.JugadorDTO;

/**
 * Panel principal del juego activo.
 * Combina el tablero visual de 10x10 (centro) con el panel lateral de control
 * (este), que agrupa la informacion del jugador, el dado, el ranking y el historial.
 *
 */
public class PanelJuego extends JPanel {

    /** Panel que dibuja el tablero boustrophedon. */
    private PanelTablero panelTablero;

    /** Panel lateral con dado, ranking, historial y controles. */
    private PanelControlJuego panelControlJuego;

    /**
     * Referencia a la ventana principal.
     * Se usa cuando el controlador necesite navegar desde la pantalla de juego
     * (por ejemplo, al terminar la partida y pasar al panel de ganador).
     */
    @SuppressWarnings("unused")
    private VentanaPrincipal ventana;

    /**
     * Constructor de PanelJuego.
     *
     * @param ventana Referencia a la ventana principal para navegacion.
     */
    public PanelJuego(VentanaPrincipal ventana) {
        this.ventana = ventana;
        inicializarComponentes();
    }

    /**
     * Inicializa y ensambla el tablero y el panel lateral de control.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        setOpaque(true);

        panelTablero      = new PanelTablero();
        panelControlJuego = new PanelControlJuego(ventana);
        panelControlJuego.setPreferredSize(new Dimension(265, 0));

        add(panelTablero,      BorderLayout.CENTER);
        add(panelControlJuego, BorderLayout.EAST);
    }

    /**
     * Actualiza el estado completo de la pantalla de juego con los datos del turno actual.
     * Delega la actualizacion a cada subpanel correspondiente.
     *
     * @param casillas      Array de CasillaDTO con el estado actual del tablero (indice 1-100).
     * @param jugadores     Array de todos los JugadorDTO activos en la partida.
     * @param indiceActual  Indice del jugador cuyo turno es el actual (0-3).
     * @param dadoActual    Valor del dado lanzado en este turno (0 si aun no se lanzo).
     * @param eventoActual  Descripcion textual del evento ocurrido (puede ser null o vacio).
     */
    public void actualizarEstado(CasillaDTO[] casillas, JugadorDTO[] jugadores,
                                  int indiceActual, int dadoActual, String eventoActual) {
        // Actualizar el tablero grafico
        panelTablero.actualizar(casillas, jugadores);

        // Actualizar informacion del jugador activo
        if (jugadores != null && indiceActual >= 0 && indiceActual < jugadores.length) {
            panelControlJuego.getPanelInfoJugador()
                              .actualizar(jugadores[indiceActual], indiceActual);
        }

        // Actualizar el dado
        panelControlJuego.getPanelDado().setValorDado(dadoActual);

        // Actualizar el ranking parcial
        panelControlJuego.getPanelRanking().actualizar(jugadores);

        // Agregar el evento al historial si hay texto valido
        if (eventoActual != null && !eventoActual.isEmpty()) {
            panelControlJuego.getPanelHistorial().agregarEvento(eventoActual);
        }
    }

    /**
     * Inicializa las conexiones visuales del tablero (serpientes y escaleras).
     * Debe llamarse una sola vez, justo despues de que el modelo construya el tablero,
     * antes de mostrar el panel de juego al usuario.
     *
     * @param serpientes Array int[n][2] con pares {cabeza, cola} de cada serpiente.
     * @param escaleras  Array int[n][2] con pares {base, cima} de cada escalera.
     */
    public void inicializarConexionesTablero(int[][] serpientes, int[][] escaleras) {
        panelTablero.inicializarConexiones(serpientes, escaleras);
    }

    /**
     * Retorna el panel lateral de control para acceso directo desde ViewFacade.
     *
     * @return PanelControlJuego del panel de juego.
     */
    public PanelControlJuego getPanelControlJuego() {
        return panelControlJuego;
    }

    /**
     * Retorna el panel del tablero para acceso directo si se necesita.
     *
     * @return PanelTablero del panel de juego.
     */
    public PanelTablero getPanelTablero() {
        return panelTablero;
    }
}
