package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.Jugador;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;

public class JugadorDAO implements OperacionDAO<Jugador> {

    private final String NOMBRE_ARCHIVO_SERIALIZADO = "jugador.bin";
    private MyLinkedList<Jugador> listaJugador;

    public JugadorDAO() {
        listaJugador = new MyLinkedList<>();
        leerArchivoSerializado();
    }

    @Override
    public void crear(Jugador nuevoDato) {
        listaJugador.addLast(nuevoDato);
        escribirArchivoSerializado();
    }

    @Override
    public MyLinkedList<Jugador> obtenerLista() {
        return listaJugador;
    }

    @Override
    public String imprimirLista() {
        if (listaJugador.isEmpty()) {
            return "No hay jugadores registrados";
        }
        return imprimirRecursivo(listaJugador.getFirst(), 0);
    }

    @Override
    public boolean eliminarDato(int indice) {
        if (indice < 0 || indice >= listaJugador.size()) {
            return false;
        }
        if (indice == 0) {
            listaJugador.extract();
        } else {
            eliminarEnIndiceRecursivo(listaJugador.getFirst(), indice, 0);
        }
        escribirArchivoSerializado();
        return true;
    }

    @Override
    public boolean actualizarDato(int indice, Jugador datoActualizado) {
        if (indice < 0 || indice >= listaJugador.size()) {
            return false;
        }
        actualizarEnIndiceRecursivo(listaJugador.getFirst(), indice, 0, datoActualizado);
        escribirArchivoSerializado();
        return true;
    }

    public void escribirArchivoSerializado() {
        FileHandler.checkFolder();
        FileHandler.writeSerializer(NOMBRE_ARCHIVO_SERIALIZADO, listaJugador);
    }

    @SuppressWarnings("unchecked")
    public void leerArchivoSerializado() {
        Object obj = FileHandler.readSerialized(NOMBRE_ARCHIVO_SERIALIZADO);
        listaJugador = (obj != null) ? (MyLinkedList<Jugador>) obj : new MyLinkedList<>();
    }

    public Jugador buscarJugador(String nombre) {
        return buscarPorNombreRecursivo(listaJugador.getFirst(), nombre);
    }

    public int cantidadJugadores() {
        return contarRecursivo(listaJugador.getFirst());
    }

    public boolean estaVacia() {
        return listaJugador.isEmpty();
    }

    public MyLinkedList<Jugador> getListaJugador() { return listaJugador; }
    public void setListaJugador(MyLinkedList<Jugador> listaJugador) { this.listaJugador = listaJugador; }

    private String imprimirRecursivo(Node<Jugador> nodo, int indice) {
        if (nodo == null) {
            return "";
        }
        Jugador j = nodo.getInfo();
        String linea = "-----------------------------------\n"
                + "Indice: " + indice + "\n"
                + "Nombre: " + j.getNombre() + "\n"
                + "Posicion: " + j.getPosicionActual() + "\n"
                + "Turnos: " + j.getCantidadTurnos() + "\n";
        return linea + imprimirRecursivo(nodo.getNext(), indice + 1);
    }

    private void eliminarEnIndiceRecursivo(Node<Jugador> anterior, int objetivo, int actual) {
        Node<Jugador> siguiente = anterior.getNext();
        if (siguiente == null) {
            return;
        }
        if (actual + 1 == objetivo) {
            anterior.setNext(siguiente.getNext());
            return;
        }
        eliminarEnIndiceRecursivo(siguiente, objetivo, actual + 1);
    }

    private void actualizarEnIndiceRecursivo(Node<Jugador> nodo, int objetivo, int actual,
            Jugador datoActualizado) {
        if (nodo == null) {
            return;
        }
        if (actual == objetivo) {
            nodo.setInfo(datoActualizado);
            return;
        }
        actualizarEnIndiceRecursivo(nodo.getNext(), objetivo, actual + 1, datoActualizado);
    }

    private Jugador buscarPorNombreRecursivo(Node<Jugador> nodo, String nombre) {
        if (nodo == null) {
            return null;
        }
        if (nodo.getInfo().getNombre().equals(nombre)) {
            return nodo.getInfo();
        }
        return buscarPorNombreRecursivo(nodo.getNext(), nombre);
    }

    private int contarRecursivo(Node<Jugador> nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarRecursivo(nodo.getNext());
    }
}
