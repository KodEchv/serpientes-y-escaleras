package co.edu.unbosque.model;

import java.io.Serializable;

public class Movimiento implements Serializable {

    private static final long serialVersionUID = 16L;

    private String nombreJugador;
    private int dado;
    private int posAntes;
    private int posDespues;
    private String evento;

    public Movimiento() { }

    public Movimiento(String nombreJugador, int dado, int posAntes, int posDespues, String evento) {
        this.nombreJugador = nombreJugador;
        this.dado = dado;
        this.posAntes = posAntes;
        this.posDespues = posDespues;
        this.evento = evento;
    }

    public String getNombreJugador() { return nombreJugador; }
    public void setNombreJugador(String nombreJugador) { this.nombreJugador = nombreJugador; }

    public int getDado() { return dado; }
    public void setDado(int dado) { this.dado = dado; }

    public int getPosAntes() { return posAntes; }
    public void setPosAntes(int posAntes) { this.posAntes = posAntes; }

    public int getPosDespues() { return posDespues; }
    public void setPosDespues(int posDespues) { this.posDespues = posDespues; }

    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }

    @Override
    public String toString() {
        return nombreJugador + ";" + dado + ";" + posAntes + ";" + posDespues + ";" + evento + "\n";
    }
}
