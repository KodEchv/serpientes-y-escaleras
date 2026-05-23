package co.edu.unbosque.model;

import java.io.Serializable;

public class Comodin implements Serializable {

    private static final long serialVersionUID = 15L;

    private int posicion;

    public Comodin() { }

    public Comodin(int posicion) {
        this.posicion = posicion;
    }

    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }

    @Override
    public String toString() {
        return posicion + "\n";
    }
}
