package co.edu.unbosque.model;

import java.io.Serializable;

public class Casilla implements Serializable {

    private static final long serialVersionUID = 12L;

    private int numeroCasilla;
    private CasillaDTO.TipoCasilla tipo;

    public Casilla() { }

    public Casilla(int numeroCasilla, CasillaDTO.TipoCasilla tipo) {
        this.numeroCasilla = numeroCasilla;
        this.tipo = tipo;
    }

    public int getNumeroCasilla() { return numeroCasilla; }
    public void setNumeroCasilla(int numeroCasilla) { this.numeroCasilla = numeroCasilla; }

    public CasillaDTO.TipoCasilla getTipo() { return tipo; }
    public void setTipo(CasillaDTO.TipoCasilla tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return numeroCasilla + ";" + tipo + "\n";
    }
}
