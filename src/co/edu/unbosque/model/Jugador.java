package co.edu.unbosque.model;

import java.io.Serializable;

public class Jugador implements Serializable {

    private static final long serialVersionUID = 11L;

    private String nombre;
    private int posicionActual;
    private boolean tieneEscudo;
    private boolean tieneDobleTurno;
    private boolean pierdeTurno;
    private int cantidadTurnos;

    public Jugador() { }

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.posicionActual = 0;
        this.tieneEscudo = false;
        this.tieneDobleTurno = false;
        this.pierdeTurno = false;
        this.cantidadTurnos = 0;
    }

    public Jugador(String nombre, int posicionActual, boolean tieneEscudo,
                   boolean tieneDobleTurno, boolean pierdeTurno, int cantidadTurnos) {
        this.nombre = nombre;
        this.posicionActual = posicionActual;
        this.tieneEscudo = tieneEscudo;
        this.tieneDobleTurno = tieneDobleTurno;
        this.pierdeTurno = pierdeTurno;
        this.cantidadTurnos = cantidadTurnos;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPosicionActual() { return posicionActual; }
    public void setPosicionActual(int posicionActual) { this.posicionActual = posicionActual; }

    public boolean isTieneEscudo() { return tieneEscudo; }
    public void setTieneEscudo(boolean tieneEscudo) { this.tieneEscudo = tieneEscudo; }

    public boolean isTieneDobleTurno() { return tieneDobleTurno; }
    public void setTieneDobleTurno(boolean tieneDobleTurno) { this.tieneDobleTurno = tieneDobleTurno; }

    public boolean isPierdeTurno() { return pierdeTurno; }
    public void setPierdeTurno(boolean pierdeTurno) { this.pierdeTurno = pierdeTurno; }

    public int getCantidadTurnos() { return cantidadTurnos; }
    public void setCantidadTurnos(int cantidadTurnos) { this.cantidadTurnos = cantidadTurnos; }

    public void incrementarTurnos() {
        this.cantidadTurnos++;
    }

    @Override
    public String toString() {
        return nombre + ";" + posicionActual + ";" + tieneEscudo + ";"
             + tieneDobleTurno + ";" + pierdeTurno + ";" + cantidadTurnos + "\n";
    }
}
