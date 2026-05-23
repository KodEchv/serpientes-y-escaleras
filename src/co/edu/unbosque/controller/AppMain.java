package co.edu.unbosque.controller;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicacion "Escaleras y Serpientes a lo Bosque".
 * Lanza la interfaz grafica en el hilo de despacho de eventos de Swing
 * usando SwingUtilities.invokeLater para garantizar la seguridad del hilo.
 *
 */
public class AppMain {

    /**
     * Metodo principal de la aplicacion.
     * Crea el Controlador dentro del hilo de eventos de Swing.
     *
     * @param args Argumentos de linea de comandos (no se usan).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Controlador();
            }
        });
    }
}
