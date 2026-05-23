package co.edu.unbosque.model;

import java.io.Serializable;

public class Ranking implements Comparable<Ranking>, Serializable {

    private static final long serialVersionUID = 17L;

    private String nombreJugador;
    private int posicionFinal;
    private int cantidadTurnos;
    private boolean gano;

    public Ranking() { }

    public Ranking(String nombreJugador, int posicionFinal, int cantidadTurnos, boolean gano) {
        this.nombreJugador = nombreJugador;
        this.posicionFinal = posicionFinal;
        this.cantidadTurnos = cantidadTurnos;
        this.gano = gano;
    }

    @Override
    public int compareTo(Ranking otro) {
        int puntuacionPropia = this.posicionFinal * 1000 - this.cantidadTurnos;
        int puntuacionOtro = otro.posicionFinal * 1000 - otro.cantidadTurnos;
        return puntuacionPropia - puntuacionOtro;
    }

    public int calcularClave() {
        return posicionFinal * 1000 - cantidadTurnos;
    }

    public String getNombreJugador() { return nombreJugador; }
    public void setNombreJugador(String nombreJugador) { this.nombreJugador = nombreJugador; }

    public int getPosicionFinal() { return posicionFinal; }
    public void setPosicionFinal(int posicionFinal) { this.posicionFinal = posicionFinal; }

    public int getCantidadTurnos() { return cantidadTurnos; }
    public void setCantidadTurnos(int cantidadTurnos) { this.cantidadTurnos = cantidadTurnos; }

    public boolean isGano() { return gano; }
    public void setGano(boolean gano) { this.gano = gano; }

    @Override
    public String toString() {
        return nombreJugador + ";" + posicionFinal + ";" + cantidadTurnos + ";" + gano + "\n";
    }
}
