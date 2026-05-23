package co.edu.unbosque.model;

import java.io.Serializable;

public class Serpiente implements Serializable {

    private static final long serialVersionUID = 13L;

    private int posicionCabeza;
    private int posicionCola;

    public Serpiente() { }

    public Serpiente(int posicionCabeza, int posicionCola) {
        this.posicionCabeza = posicionCabeza;
        this.posicionCola = posicionCola;
    }

    public int getPosicionCabeza() { return posicionCabeza; }
    public void setPosicionCabeza(int posicionCabeza) { this.posicionCabeza = posicionCabeza; }

    public int getPosicionCola() { return posicionCola; }
    public void setPosicionCola(int posicionCola) { this.posicionCola = posicionCola; }

    @Override
    public String toString() {
        return posicionCabeza + ";" + posicionCola + "\n";
    }
}
