package co.edu.unbosque.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

/**
 * Genera programaticamente las imagenes PNG de serpientes y escaleras
 * usadas en el tablero del juego "Escaleras y Serpientes a lo Bosque".
 *
 * <p>Las imagenes se crean en memoria con {@link BufferedImage} y {@link Graphics2D}.
 * Estan disenadas para ser estiradas y rotadas mediante {@code AffineTransform}
 * entre dos casillas del tablero, por lo que su orientacion base es horizontal:
 * el extremo izquierdo es el origen de la conexion y el derecho el destino.</p>
 *
 * <p>Serpiente: cabeza a la izquierda (casilla cabeza), cola a la derecha.</p>
 * <p>Escalera:  base a la izquierda, cima a la derecha.</p>
 *
 * @author Estudiante
 * @version 1.0
 */
public class GeneradorImagenes {

    /** Ancho de las imagenes generadas en pixeles. */
    private static final int ANCHO = 400;

    /** Alto de las imagenes generadas en pixeles. */
    private static final int ALTO  = 60;

    // =========================================================================
    // SERPIENTE
    // =========================================================================

    /**
     * Genera una imagen de serpiente orientada horizontalmente.
     * La cabeza aparece a la izquierda y la cola a la derecha.
     * Fondo transparente (canal alpha).
     *
     * @return {@link BufferedImage} de tipo ARGB con la serpiente dibujada.
     */
    public static BufferedImage generarSerpiente() {
        BufferedImage img = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        activarCalidad(g);

        // Fondo completamente transparente
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, ANCHO, ALTO);

        int cy = ALTO / 2;

        // --- Cuerpo sinusoidal ---
        Path2D cuerpo = new Path2D.Double();
        double amplitud  = ALTO * 0.25;
        double frecuencia = Math.PI * 4.0 / ANCHO;

        cuerpo.moveTo(30, cy);
        for (int x = 30; x <= ANCHO - 30; x++) {
            double y = cy + amplitud * Math.sin(frecuencia * (x - 30));
            cuerpo.lineTo(x, y);
        }

        // Sombra del cuerpo
        g.setColor(new Color(20, 80, 20, 120));
        g.setStroke(new BasicStroke(14f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.translate(2, 2);
        g.draw(cuerpo);
        g.translate(-2, -2);

        // Cuerpo principal verde
        GradientPaint gradienteCuerpo = new GradientPaint(
                0, cy - 10, new Color(60, 160, 50),
                0, cy + 10, new Color(30, 100, 20));
        g.setPaint(gradienteCuerpo);
        g.setStroke(new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(cuerpo);

        // Escamas (lineas transversales sobre el cuerpo)
        g.setStroke(new BasicStroke(1.2f));
        g.setColor(new Color(40, 120, 30, 160));
        for (int x = 50; x < ANCHO - 40; x += 18) {
            double yC = cy + amplitud * Math.sin(frecuencia * (x - 30));
            double angEscama = Math.atan2(
                    amplitud * frecuencia * Math.cos(frecuencia * (x - 30)), 1.0);
            double perpX = -Math.sin(angEscama) * 5;
            double perpY =  Math.cos(angEscama) * 5;
            g.drawLine((int)(x - perpX), (int)(yC - perpY),
                       (int)(x + perpX), (int)(yC + perpY));
        }

        // --- Cabeza (izquierda) ---
        int hR = 13; // radio de la cabeza
        int hx = 20;
        int hy = cy;
        g.setColor(new Color(30, 110, 20));
        g.fillOval(hx - hR, hy - hR, hR * 2, hR * 2);
        g.setColor(new Color(20, 80, 15));
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(hx - hR, hy - hR, hR * 2, hR * 2);

        // Ojo izquierdo
        g.setColor(Color.WHITE);
        g.fillOval(hx - 4, hy - 7, 8, 8);
        g.setColor(new Color(10, 10, 10));
        g.fillOval(hx - 2, hy - 6, 5, 5);
        g.setColor(new Color(255, 255, 255, 180));
        g.fillOval(hx - 1, hy - 6, 2, 2); // brillo

        // Boca (arco) y lengua
        g.setColor(new Color(180, 30, 30));
        g.setStroke(new BasicStroke(1.5f));
        g.drawArc(hx - hR + 2, hy + 2, hR - 2, hR / 2, 200, 140);
        // Lengua bífida
        g.setColor(new Color(220, 40, 40));
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(hx - hR - 6, hy + 2, hx - hR, hy + 2);
        g.drawLine(hx - hR - 6, hy + 2, hx - hR - 10, hy - 1);
        g.drawLine(hx - hR - 6, hy + 2, hx - hR - 10, hy + 5);

        // --- Cola (derecha, punta afilada) ---
        Path2D cola = new Path2D.Double();
        cola.moveTo(ANCHO - 30, cy - 5);
        cola.lineTo(ANCHO - 5,  cy);
        cola.lineTo(ANCHO - 30, cy + 5);
        cola.closePath();
        g.setColor(new Color(50, 140, 40));
        g.fill(cola);

        g.dispose();
        return img;
    }

    // =========================================================================
    // ESCALERA
    // =========================================================================

    /**
     * Genera una imagen de escalera de madera orientada horizontalmente.
     * La base aparece a la izquierda y la cima a la derecha.
     * Fondo transparente (canal alpha).
     *
     * @return {@link BufferedImage} de tipo ARGB con la escalera dibujada.
     */
    public static BufferedImage generarEscalera() {
        BufferedImage img = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        activarCalidad(g);

        // Fondo completamente transparente
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, ANCHO, ALTO);

        Color colorRiel    = new Color(139, 90, 43);
        Color colorClaro   = new Color(180, 130, 70);
        Color colorOscuro  = new Color(100, 60, 20);
        Color colorPeldano = new Color(160, 110, 55);

        int margen    = 8;
        int grosorRiel = 7;
        int yArriba   = margen;
        int yAbajo    = ALTO - margen - grosorRiel;

        // Sombra de los rieles
        g.setColor(new Color(60, 30, 0, 100));
        g.fillRoundRect(4, yArriba + 3, ANCHO - 4, grosorRiel, 6, 6);
        g.fillRoundRect(4, yAbajo  + 3, ANCHO - 4, grosorRiel, 6, 6);

        // Riel superior con degradado
        GradientPaint gradRiel = new GradientPaint(
                0, yArriba, colorClaro, 0, yArriba + grosorRiel, colorOscuro);
        g.setPaint(gradRiel);
        g.fillRoundRect(0, yArriba, ANCHO, grosorRiel, 6, 6);
        g.setColor(colorOscuro);
        g.setStroke(new BasicStroke(0.8f));
        g.drawRoundRect(0, yArriba, ANCHO, grosorRiel, 6, 6);

        // Riel inferior con degradado
        g.setPaint(gradRiel);
        g.fillRoundRect(0, yAbajo, ANCHO, grosorRiel, 6, 6);
        g.setColor(colorOscuro);
        g.drawRoundRect(0, yAbajo, ANCHO, grosorRiel, 6, 6);

        // Peldaños
        int numPeldanos = 9;
        int espacioUtil = ANCHO - 20;
        int paso        = espacioUtil / (numPeldanos + 1);
        int anchoPeldano = 5;
        int yPeldanoTop  = yArriba + grosorRiel;
        int altoPeldano  = yAbajo - yPeldanoTop;

        for (int i = 1; i <= numPeldanos; i++) {
            int xP = 10 + i * paso;

            // Sombra del peldano
            g.setColor(new Color(60, 30, 0, 80));
            g.fillRoundRect(xP + 2, yPeldanoTop + 2, anchoPeldano, altoPeldano, 3, 3);

            // Peldano con degradado
            GradientPaint gradPeldano = new GradientPaint(
                    xP, 0, colorPeldano, xP + anchoPeldano, 0, colorOscuro);
            g.setPaint(gradPeldano);
            g.fillRoundRect(xP, yPeldanoTop, anchoPeldano, altoPeldano, 3, 3);

            // Veta de madera
            g.setColor(new Color(120, 80, 30, 120));
            g.setStroke(new BasicStroke(0.6f));
            g.drawLine(xP + 1, yPeldanoTop + 3, xP + 1, yPeldanoTop + altoPeldano - 3);
        }

        // Reflejos en los rieles
        g.setColor(new Color(255, 220, 150, 80));
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(5, yArriba + 2, ANCHO - 5, yArriba + 2);
        g.drawLine(5, yAbajo  + 2, ANCHO - 5, yAbajo  + 2);

        // Indicadores de inicio (flecha izquierda) y fin (flecha derecha)
        g.setColor(new Color(colorRiel.getRed(), colorRiel.getGreen(), colorRiel.getBlue(), 200));
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int midY = ALTO / 2;
        // Flecha inicio
        g.drawLine(8, midY - 5, 3, midY);
        g.drawLine(3, midY, 8, midY + 5);
        // Flecha fin
        g.drawLine(ANCHO - 8, midY - 5, ANCHO - 3, midY);
        g.drawLine(ANCHO - 3, midY, ANCHO - 8, midY + 5);

        g.dispose();
        return img;
    }

    // =========================================================================
    // UTILIDAD PRIVADA
    // =========================================================================

    /**
     * Activa el antialiasing y calidad de texto en el contexto grafico.
     *
     * @param g Contexto Graphics2D a configurar.
     */
    private static void activarCalidad(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
    }
}
