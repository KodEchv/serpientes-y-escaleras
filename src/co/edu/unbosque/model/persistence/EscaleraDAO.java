package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.Escalera;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;

public class EscaleraDAO implements OperacionDAO<Escalera> {

    private final String NOMBRE_ARCHIVO_SERIALIZADO = "escalera.bin";
    private MyLinkedList<Escalera> listaEscalera;

    public EscaleraDAO() {
        listaEscalera = new MyLinkedList<>();
        leerArchivoSerializado();
    }

    @Override
    public void crear(Escalera nuevoDato) {
        listaEscalera.addLast(nuevoDato);
        escribirArchivoSerializado();
    }

    @Override
    public MyLinkedList<Escalera> obtenerLista() {
        return listaEscalera;
    }

    @Override
    public String imprimirLista() {
        if (listaEscalera.isEmpty()) {
            return "No hay escaleras registradas";
        }
        return imprimirRecursivo(listaEscalera.getFirst(), 0);
    }

    @Override
    public boolean eliminarDato(int indice) {
        if (indice < 0 || indice >= listaEscalera.size()) {
            return false;
        }
        if (indice == 0) {
            listaEscalera.extract();
        } else {
            eliminarEnIndiceRecursivo(listaEscalera.getFirst(), indice, 0);
        }
        escribirArchivoSerializado();
        return true;
    }

    @Override
    public boolean actualizarDato(int indice, Escalera datoActualizado) {
        if (indice < 0 || indice >= listaEscalera.size()) {
            return false;
        }
        actualizarEnIndiceRecursivo(listaEscalera.getFirst(), indice, 0, datoActualizado);
        escribirArchivoSerializado();
        return true;
    }

    public void escribirArchivoSerializado() {
        FileHandler.checkFolder();
        FileHandler.writeSerializer(NOMBRE_ARCHIVO_SERIALIZADO, listaEscalera);
    }

    @SuppressWarnings("unchecked")
    public void leerArchivoSerializado() {
        Object obj = FileHandler.readSerialized(NOMBRE_ARCHIVO_SERIALIZADO);
        listaEscalera = (obj != null) ? (MyLinkedList<Escalera>) obj : new MyLinkedList<>();
    }

    public MyLinkedList<Escalera> getListaEscalera() { return listaEscalera; }
    public void setListaEscalera(MyLinkedList<Escalera> listaEscalera) { this.listaEscalera = listaEscalera; }

    private String imprimirRecursivo(Node<Escalera> nodo, int indice) {
        if (nodo == null) {
            return "";
        }
        Escalera e = nodo.getInfo();
        String linea = "-----------------------------------\n"
                + "Indice: " + indice + "\n"
                + "Base: " + e.getPosicionBase() + "\n"
                + "Cima: " + e.getPosicionCima() + "\n";
        return linea + imprimirRecursivo(nodo.getNext(), indice + 1);
    }

    private void eliminarEnIndiceRecursivo(Node<Escalera> anterior, int objetivo, int actual) {
        Node<Escalera> siguiente = anterior.getNext();
        if (siguiente == null) {
            return;
        }
        if (actual + 1 == objetivo) {
            anterior.setNext(siguiente.getNext());
            return;
        }
        eliminarEnIndiceRecursivo(siguiente, objetivo, actual + 1);
    }

    private void actualizarEnIndiceRecursivo(Node<Escalera> nodo, int objetivo, int actual,
            Escalera datoActualizado) {
        if (nodo == null) {
            return;
        }
        if (actual == objetivo) {
            nodo.setInfo(datoActualizado);
            return;
        }
        actualizarEnIndiceRecursivo(nodo.getNext(), objetivo, actual + 1, datoActualizado);
    }
}
