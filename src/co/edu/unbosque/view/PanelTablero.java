package co.edu.unbosque.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import co.edu.unbosque.util.GeneradorImagenes;

import co.edu.unbosque.model.CasillaDTO;
import co.edu.unbosque.model.CasillaDTO.TipoCasilla;
import co.edu.unbosque.model.JugadorDTO;

/**
 * Panel que dibuja el tablero 10x10 del juego "Escaleras y Serpientes a lo Bosque".
 * Renderiza cada casilla con su color segun tipo, los numeros de casilla,
 * las conexiones visuales de serpientes y escaleras con imagenes PNG rotadas,
 * los circulos de jugadores y la leyenda de colores en la parte inferior.
 *
 * <p>El recorrido de casillas sigue el patron boustrophedon: la fila inferior
 * (casillas 1-10) va de izquierda a derecha, la siguiente de derecha a izquierda,
 * y asi alternando hasta llegar a la casilla 100 en la esquina superior izquierda.</p>
 *
 * <p>Las conexiones entre casillas se dibujan usando {@link AffineTransform} para
 * rotar y escalar la imagen PNG entre los centros de las dos casillas conectadas.
 * Si la imagen no esta disponible, se dibuja un rectangulo redondeado de color
 * como fallback visual.</p>
 *
 * @author Estudiante
 * @version 1.0
 */
public class PanelTablero extends JPanel {

    /** Array de casillas del tablero (indice 0 no se usa; casillas validas: 1-100). */
    private CasillaDTO[] casillas;

    /** Array de jugadores activos en la partida. */
    private JugadorDTO[] jugadores;

    /**
     * Imagen PNG para representar visualmente las serpientes.
     * Se carga desde archivos/imagenes/serpiente.png al construir el panel.
     * Puede ser null si el archivo no existe; en ese caso se usa el fallback de color.
     */
    private BufferedImage imagenSerpiente;

    /**
     * Imagen PNG para representar visualmente las escaleras.
     * Se carga desde archivos/imagenes/escalera.png al construir el panel.
     * Puede ser null si el archivo no existe; en ese caso se usa el fallback de color.
     */
    private BufferedImage imagenEscalera;

    /**
     * Array de pares [cabeza, cola] de cada serpiente en el tablero.
     * Se inicializa una sola vez desde {@link #inicializarConexiones}.
     * Puede ser null si la partida aun no inicio.
     */
    private int[][] conexionesSerpientes;

    /**
     * Array de pares [base, cima] de cada escalera en el tablero.
     * Se inicializa una sola vez desde {@link #inicializarConexiones}.
     * Puede ser null si la partida aun no inicio.
     */
    private int[][] conexionesEscaleras;

    /** Altura reservada en la parte inferior para la leyenda de colores. */
    private static final int ALTURA_LEYENDA = 30;

    /**
     * Constructor de PanelTablero.
     * Inicializa el panel sin datos (tablero vacio).
     * Intenta cargar las imagenes PNG desde archivos/imagenes/.
     * Si no existen, genera las imagenes programaticamente con GeneradorImagenes.
     */
    public PanelTablero() {
        setBackground(VentanaPrincipal.COLOR_FONDO_PANEL);
        setOpaque(true);
        this.casillas             = null;
        this.jugadores            = null;
        this.conexionesSerpientes = null;
        this.conexionesEscaleras  = null;

        // Serpiente: intentar PNG del usuario, si no existe usar imagen generada
        try {
            imagenSerpiente = ImageIO.read(new File("archivos/imagenes/serpiente.png"));
        } catch (Exception ex) {
            imagenSerpiente = GeneradorImagenes.generarSerpiente();
        }

        // Escalera: intentar PNG del usuario, si no existe usar imagen generada
        try {
            imagenEscalera = ImageIO.read(new File("archivos/imagenes/escalera.png"));
        } catch (Exception ex) {
            imagenEscalera = GeneradorImagenes.generarEscalera();
        }
    }

    // ---------------------------------------------------------------
    // Actualizacion de datos
    // ---------------------------------------------------------------

    /**
     * Actualiza los datos del tablero y solicita un redibujado completo.
     *
     * @param casillas  Array de CasillaDTO con la informacion de cada casilla (indice 1-100).
     * @param jugadores Array de JugadorDTO con el estado actual de los jugadores.
     */
    public void actualizar(CasillaDTO[] casillas, JugadorDTO[] jugadores) {
        this.casillas  = casillas;
        this.jugadores = jugadores;
        repaint();
    }

    /**
     * Inicializa las conexiones visuales del tablero (serpientes y escaleras).
     * Debe llamarse una sola vez, justo despues de que el modelo construya el tablero.
     * Guarda los pares de casillas conectadas y solicita un redibujado.
     *
     * @param serpientes Array int[n][2] donde cada fila es {cabeza, cola} de la serpiente.
     * @param escaleras  Array int[n][2] donde cada fila es {base, cima} de la escalera.
     */
    public void inicializarConexiones(int[][] serpientes, int[][] escaleras) {
        this.conexionesSerpientes = serpientes;
        this.conexionesEscaleras  = escaleras;
        repaint();
    }

    // ---------------------------------------------------------------
    // Pintura principal
    // ---------------------------------------------------------------

    /**
     * Dibuja el tablero completo en el siguiente orden:
     * <ol>
     *   <li>Fondo del panel.</li>
     *   <li>Cuadricula de 100 casillas con colores segun tipo.</li>
     *   <li>Conexiones de serpientes y escaleras (imagenes PNG rotadas o fallback).</li>
     *   <li>Circulos de jugadores sobre las casillas.</li>
     *   <li>Leyenda de colores en la franja inferior.</li>
     * </ol>
     *
     * @param g Contexto grafico proporcionado por Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,     RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // PASO 1 — Fondo del panel
        g2.setColor(VentanaPrincipal.COLOR_FONDO_PANEL);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Calcular dimensiones de la cuadricula
        int areaTablero = getHeight() - ALTURA_LEYENDA;
        int tamCelda    = areaTablero / 10;
        int offsetX     = (getWidth() - tamCelda * 10) / 2;
        int offsetY     = 5;

        // PASO 2 — Dibujar las 100 casillas
        dibujarCasillas(g2, tamCelda, offsetX, offsetY);

        // PASO 3 — Dibujar conexiones de serpientes y escaleras
        dibujarConexiones(g2, tamCelda, offsetX, offsetY);

        // PASO 4 — Dibujar los circulos de jugadores
        if (jugadores != null) {
            dibujarJugadores(g2, tamCelda, offsetX, offsetY);
        }

        // PASO 5 — Leyenda en la parte inferior
        dibujarLeyenda(g2);
    }

    // ---------------------------------------------------------------
    // Dibujo de casillas
    // ---------------------------------------------------------------

    /**
     * Dibuja todas las casillas del tablero con su color segun tipo y su numero.
     *
     * @param g2       Contexto grafico 2D.
     * @param tamCelda Tamano en pixeles de cada celda cuadrada.
     * @param offsetX  Desplazamiento horizontal para centrar el tablero.
     * @param offsetY  Desplazamiento vertical superior.
     */
    private void dibujarCasillas(Graphics2D g2, int tamCelda, int offsetX, int offsetY) {
        Font fuenteNumero = new Font("SansSerif", Font.PLAIN, 9);
        g2.setFont(fuenteNumero);

        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                int numeroCasilla = obtenerNumeroCasilla(fila, col);
                int x = offsetX + col * tamCelda;
                int y = offsetY + fila * tamCelda;

                // Determinar color de la casilla
                Color colorCasilla = obtenerColorCasilla(numeroCasilla, fila, col);

                // Rellenar casilla
                g2.setColor(colorCasilla);
                g2.fillRect(x, y, tamCelda, tamCelda);

                // Borde de la casilla
                g2.setColor(VentanaPrincipal.COLOR_BORDE);
                g2.drawRect(x, y, tamCelda, tamCelda);

                // Numero de casilla
                g2.setColor(VentanaPrincipal.COLOR_TEXTO_OSCURO);
                g2.drawString(String.valueOf(numeroCasilla), x + 3, y + 14);
            }
        }
    }

    /**
     * Determina el color de relleno de una casilla segun su tipo.
     * Si el array de casillas es null, aplica el patron de ajedrez normal/par.
     *
     * @param numeroCasilla Numero de la casilla (1-100).
     * @param fila          Indice de fila en la cuadricula (0-9, 0=arriba).
     * @param col           Indice de columna (0-9).
     * @return Color correspondiente al tipo de la casilla.
     */
    private Color obtenerColorCasilla(int numeroCasilla, int fila, int col) {
        if (casillas == null || numeroCasilla < 1 || numeroCasilla > 100) {
            // Tablero vacio: patron ajedrez
            return ((fila + col) % 2 == 0)
                    ? VentanaPrincipal.COLOR_CASILLA_NORMAL
                    : VentanaPrincipal.COLOR_CASILLA_PAR;
        }

        TipoCasilla tipo = casillas[numeroCasilla].getTipo();

        if (tipo == TipoCasilla.SERPIENTE) {
            return VentanaPrincipal.COLOR_CASILLA_SERPIENTE;
        } else if (tipo == TipoCasilla.ESCALERA) {
            return VentanaPrincipal.COLOR_CASILLA_ESCALERA;
        } else if (tipo == TipoCasilla.COMODIN) {
            return VentanaPrincipal.COLOR_CASILLA_COMODIN;
        } else {
            return ((fila + col) % 2 == 0)
                    ? VentanaPrincipal.COLOR_CASILLA_NORMAL
                    : VentanaPrincipal.COLOR_CASILLA_PAR;
        }
    }

    // ---------------------------------------------------------------
    // Dibujo de conexiones (serpientes y escaleras)
    // ---------------------------------------------------------------

    /**
     * Punto de entrada para dibujar todas las conexiones visuales del tablero.
     * Delega el recorrido a los metodos recursivos de serpientes y escaleras.
     *
     * @param g2       Contexto grafico 2D.
     * @param tamCelda Tamano en pixeles de cada celda.
     * @param offsetX  Desplazamiento horizontal del tablero.
     * @param offsetY  Desplazamiento vertical del tablero.
     */
    private void dibujarConexiones(Graphics2D g2, int tamCelda, int offsetX, int offsetY) {
        dibujarTodasSerpientes(g2, tamCelda, offsetX, offsetY, 0);
        dibujarTodasEscaleras(g2, tamCelda, offsetX, offsetY, 0);
    }

    /**
     * Dibuja recursivamente todas las conexiones de serpientes del tablero.
     * Caso base: {@code conexionesSerpientes} es null o {@code i} supera su longitud.
     * Caso recursivo: dibuja la serpiente en el indice {@code i} y avanza.
     *
     * @param g2       Contexto grafico 2D.
     * @param tamCelda Tamano en pixeles de cada celda.
     * @param offsetX  Desplazamiento horizontal del tablero.
     * @param offsetY  Desplazamiento vertical del tablero.
     * @param i        Indice actual del array de conexiones de serpientes.
     */
    private void dibujarTodasSerpientes(Graphics2D g2, int tamCelda,
            int offsetX, int offsetY, int i) {
        if (conexionesSerpientes == null || i >= conexionesSerpientes.length) {
            return;
        }
        dibujarConexionImagen(
                g2, imagenSerpiente,
                conexionesSerpientes[i][0], conexionesSerpientes[i][1],
                tamCelda, offsetX, offsetY,
                new Color(160, 80, 50, 200));  // marron semitransparente como fallback
        dibujarTodasSerpientes(g2, tamCelda, offsetX, offsetY, i + 1);
    }

    /**
     * Dibuja recursivamente todas las conexiones de escaleras del tablero.
     * Caso base: {@code conexionesEscaleras} es null o {@code i} supera su longitud.
     * Caso recursivo: dibuja la escalera en el indice {@code i} y avanza.
     *
     * @param g2       Contexto grafico 2D.
     * @param tamCelda Tamano en pixeles de cada celda.
     * @param offsetX  Desplazamiento horizontal del tablero.
     * @param offsetY  Desplazamiento vertical del tablero.
     * @param i        Indice actual del array de conexiones de escaleras.
     */
    private void dibujarTodasEscaleras(Graphics2D g2, int tamCelda,
            int offsetX, int offsetY, int i) {
        if (conexionesEscaleras == null || i >= conexionesEscaleras.length) {
            return;
        }
        dibujarConexionImagen(
                g2, imagenEscalera,
                conexionesEscaleras[i][0], conexionesEscaleras[i][1],
                tamCelda, offsetX, offsetY,
                new Color(80, 160, 80, 200));  // verde semitransparente como fallback
        dibujarTodasEscaleras(g2, tamCelda, offsetX, offsetY, i + 1);
    }

    /**
     * Dibuja una imagen PNG rotada y escalada entre los centros de dos casillas.
     * Si la imagen es null, dibuja un rectangulo redondeado de color como fallback.
     *
     * <p>Matematica aplicada:</p>
     * <ul>
     *   <li>{@code cx1, cy1} = centro en pixeles de la casilla origen.</li>
     *   <li>{@code cx2, cy2} = centro en pixeles de la casilla destino.</li>
     *   <li>{@code angulo}   = atan2(cy2-cy1, cx2-cx1) — orientacion de la conexion.</li>
     *   <li>{@code longitud} = distancia euclidiana entre los dos centros.</li>
     *   <li>Se traslada al punto medio, se rota el contexto 2D y se dibuja la
     *       imagen centrada respecto al punto de origen del contexto trasladado.</li>
     * </ul>
     *
     * <p>El metodo guarda y restaura el {@link AffineTransform} original del
     * contexto grafico para no afectar el resto del pintado.</p>
     *
     * @param g2             Contexto grafico 2D.
     * @param imagen         Imagen PNG a dibujar (puede ser null para usar fallback).
     * @param numeroCasillaA Numero de la casilla de origen de la conexion.
     * @param numeroCasillaB Numero de la casilla de destino de la conexion.
     * @param tamCelda       Tamano en pixeles de cada celda del tablero.
     * @param offsetX        Desplazamiento horizontal del tablero dentro del panel.
     * @param offsetY        Desplazamiento vertical del tablero dentro del panel.
     * @param colorFallback  Color con transparencia para dibujar si la imagen es null.
     */
    private void dibujarConexionImagen(Graphics2D g2, BufferedImage imagen,
            int numeroCasillaA, int numeroCasillaB,
            int tamCelda, int offsetX, int offsetY, Color colorFallback) {

        int[] fcA = obtenerFilaColumna(numeroCasillaA);
        int[] fcB = obtenerFilaColumna(numeroCasillaB);

        // Centro en pixeles de cada casilla
        int cx1 = offsetX + fcA[1] * tamCelda + tamCelda / 2;
        int cy1 = offsetY + fcA[0] * tamCelda + tamCelda / 2;
        int cx2 = offsetX + fcB[1] * tamCelda + tamCelda / 2;
        int cy2 = offsetY + fcB[0] * tamCelda + tamCelda / 2;

        // Angulo de rotacion y longitud entre los dos centros
        double angulo   = Math.atan2(cy2 - cy1, cx2 - cx1);
        int    longitud = (int) Math.sqrt(
                (long)(cx2 - cx1) * (cx2 - cx1) + (long)(cy2 - cy1) * (cy2 - cy1));
        int    midX     = (cx1 + cx2) / 2;
        int    midY     = (cy1 + cy2) / 2;
        int    alto     = Math.max(tamCelda / 4, 10);

        // Guardar transformacion original y aplicar traslacion + rotacion
        AffineTransform transformOriginal = g2.getTransform();
        g2.translate(midX, midY);
        g2.rotate(angulo);

        if (imagen != null) {
            // Dibujar imagen estirada entre los dos centros
            g2.drawImage(imagen, -longitud / 2, -alto / 2, longitud, alto, null);
        } else {
            // Fallback visual: rectangulo redondeado de color cuando no hay PNG
            g2.setColor(colorFallback);
            g2.fillRoundRect(-longitud / 2, -alto / 2, longitud, alto, 14, 14);
            g2.setColor(colorFallback.darker());
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(-longitud / 2, -alto / 2, longitud, alto, 14, 14);
        }

        // Restaurar transformacion original para no afectar el resto del pintado
        g2.setTransform(transformOriginal);
    }

    // ---------------------------------------------------------------
    // Dibujo de jugadores
    // ---------------------------------------------------------------

    /**
     * Dibuja los circulos de colores que representan la posicion de cada jugador.
     * Si varios jugadores comparten casilla, los circulos se desplazan para no solaparse.
     *
     * @param g2       Contexto grafico 2D.
     * @param tamCelda Tamano en pixeles de cada celda.
     * @param offsetX  Desplazamiento horizontal del tablero.
     * @param offsetY  Desplazamiento vertical del tablero.
     */
    private void dibujarJugadores(Graphics2D g2, int tamCelda, int offsetX, int offsetY) {
        Font fuenteJugador = new Font("SansSerif", Font.BOLD, 10);
        g2.setFont(fuenteJugador);

        for (int i = 0; i < jugadores.length; i++) {
            JugadorDTO jugador = jugadores[i];
            int posicion = jugador.getPosicionActual();

            if (posicion < 1 || posicion > 100) {
                continue; // Jugador aun no ha iniciado o posicion invalida
            }

            int[] fc           = obtenerFilaColumna(posicion);
            int   fila         = fc[0];
            int   col          = fc[1];
            int   x            = offsetX + col * tamCelda;
            int   y            = offsetY + fila * tamCelda;
            int   desplazamiento = i * 18;

            // Circulo del jugador
            Color colorJugador = (i < VentanaPrincipal.COLORES_JUGADORES.length)
                    ? VentanaPrincipal.COLORES_JUGADORES[i]
                    : Color.GRAY;

            g2.setColor(colorJugador);
            g2.fillOval(x + 4 + desplazamiento, y + tamCelda - 20, 16, 16);

            // Borde del circulo
            g2.setColor(Color.WHITE);
            g2.drawOval(x + 4 + desplazamiento, y + tamCelda - 20, 16, 16);

            // Numero del jugador dentro del circulo
            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(i + 1), x + 9 + desplazamiento, y + tamCelda - 8);
        }
    }

    // ---------------------------------------------------------------
    // Leyenda inferior
    // ---------------------------------------------------------------

    /**
     * Dibuja la leyenda de colores en la franja inferior del panel.
     * Muestra un rectangulo de color y el nombre de cada tipo de casilla.
     *
     * @param g2 Contexto grafico 2D.
     */
    private void dibujarLeyenda(Graphics2D g2) {
        int yLeyenda  = getHeight() - ALTURA_LEYENDA + 6;
        int xInicio   = 20;
        int anchoItem = (getWidth() - 40) / 4;
        Font fuenteLeyenda = new Font("SansSerif", Font.PLAIN, 10);
        g2.setFont(fuenteLeyenda);

        dibujarItemLeyenda(g2, "Normal",    VentanaPrincipal.COLOR_CASILLA_NORMAL,    xInicio,                 yLeyenda);
        dibujarItemLeyenda(g2, "Serpiente", VentanaPrincipal.COLOR_CASILLA_SERPIENTE, xInicio + anchoItem,     yLeyenda);
        dibujarItemLeyenda(g2, "Escalera",  VentanaPrincipal.COLOR_CASILLA_ESCALERA,  xInicio + anchoItem * 2, yLeyenda);
        dibujarItemLeyenda(g2, "Comodin",   VentanaPrincipal.COLOR_CASILLA_COMODIN,   xInicio + anchoItem * 3, yLeyenda);
    }

    /**
     * Dibuja un item individual de la leyenda: rectangulo de color + etiqueta de texto.
     *
     * @param g2     Contexto grafico 2D.
     * @param texto  Nombre del tipo de casilla.
     * @param color  Color representativo del tipo.
     * @param x      Posicion horizontal del item.
     * @param y      Posicion vertical del item.
     */
    private void dibujarItemLeyenda(Graphics2D g2, String texto, Color color, int x, int y) {
        // Rectangulo de color con borde
        g2.setColor(color);
        g2.fillRect(x, y, 12, 12);
        g2.setColor(VentanaPrincipal.COLOR_BORDE);
        g2.drawRect(x, y, 12, 12);

        // Etiqueta de texto
        g2.setColor(VentanaPrincipal.COLOR_TEXTO_OSCURO);
        g2.drawString(texto, x + 16, y + 11);
    }

    // ---------------------------------------------------------------
    // Calculos de posicion boustrophedon
    // ---------------------------------------------------------------

    /**
     * Calcula el numero de casilla correspondiente a una posicion en la cuadricula.
     * Sigue el patron boustrophedon: filas pares de izquierda a derecha,
     * filas impares de derecha a izquierda (contando desde abajo).
     *
     * @param fila    Indice de fila en pantalla (0 = arriba, 9 = abajo).
     * @param columna Indice de columna (0 = izquierda, 9 = derecha).
     * @return Numero de casilla del tablero (1 a 100).
     */
    private int obtenerNumeroCasilla(int fila, int columna) {
        int filaDesdeAbajo = 9 - fila;
        int base = filaDesdeAbajo * 10;
        if (filaDesdeAbajo % 2 == 0) {
            return base + columna + 1;       // Izquierda a derecha
        } else {
            return base + (9 - columna) + 1; // Derecha a izquierda
        }
    }

    /**
     * Calcula la posicion en la cuadricula (fila, columna) correspondiente
     * a un numero de casilla del tablero.
     *
     * @param numeroCasilla Numero de casilla (1 a 100).
     * @return Array int[]{fila, columna} con la posicion en pantalla.
     */
    private int[] obtenerFilaColumna(int numeroCasilla) {
        int filaDesdeAbajo = (numeroCasilla - 1) / 10;
        int posEnFila      = (numeroCasilla - 1) % 10;
        int fila           = 9 - filaDesdeAbajo;
        int columna        = (filaDesdeAbajo % 2 == 0) ? posEnFila : (9 - posEnFila);
        return new int[]{fila, columna};
    }
}
