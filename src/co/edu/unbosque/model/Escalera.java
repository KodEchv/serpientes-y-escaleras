package co.edu.unbosque.model;

import java.io.Serializable;

public class Escalera implements Serializable {

    private static final long serialVersionUID = 14L;

    private int posicionBase;
    private int posicionCima;

    public Escalera() { }

    public Escalera(int posicionBase, int posicionCima) {
        this.posicionBase = posicionBase;
        this.posicionCima = posicionCima;
    }

    public int getPosicionBase() { return posicionBase; }
    public void setPosicionBase(int posicionBase) { this.posicionBase = posicionBase; }

    public int getPosicionCima() { return posicionCima; }
    public void setPosicionCima(int posicionCima) { this.posicionCima = posicionCima; }

    @Override
    public String toString() {
        return posicionBase + ";" + posicionCima + "\n";
    }
}
