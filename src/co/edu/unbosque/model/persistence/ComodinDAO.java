package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.Comodin;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;

public class ComodinDAO implements OperacionDAO<Comodin> {

    private final String NOMBRE_ARCHIVO_SERIALIZADO = "comodin.bin";
    private MyLinkedList<Comodin> listaComodin;

    public ComodinDAO() {
        listaComodin = new MyLinkedList<>();
        leerArchivoSerializado();
    }

    @Override
    public void crear(Comodin nuevoDato) {
        listaComodin.addLast(nuevoDato);
        escribirArchivoSerializado();
    }

    @Override
    public MyLinkedList<Comodin> obtenerLista() {
        return listaComodin;
    }

    @Override
    public String imprimirLista() {
        if (listaComodin.isEmpty()) {
            return "No hay comodines registrados";
        }
        return imprimirRecursivo(listaComodin.getFirst(), 0);
    }

    @Override
    public boolean eliminarDato(int indice) {
        if (indice < 0 || indice >= listaComodin.size()) {
            return false;
        }
        if (indice == 0) {
            listaComodin.extract();
        } else {
            eliminarEnIndiceRecursivo(listaComodin.getFirst(), indice, 0);
        }
        escribirArchivoSerializado();
        return true;
    }

    @Override
    public boolean actualizarDato(int indice, Comodin datoActualizado) {
        if (indice < 0 || indice >= listaComodin.size()) {
            return false;
        }
        actualizarEnIndiceRecursivo(listaComodin.getFirst(), indice, 0, datoActualizado);
        escribirArchivoSerializado();
        return true;
    }

    public void escribirArchivoSerializado() {
        FileHandler.checkFolder();
        FileHandler.writeSerializer(NOMBRE_ARCHIVO_SERIALIZADO, listaComodin);
    }

    @SuppressWarnings("unchecked")
    public void leerArchivoSerializado() {
        Object obj = FileHandler.readSerialized(NOMBRE_ARCHIVO_SERIALIZADO);
        listaComodin = (obj != null) ? (MyLinkedList<Comodin>) obj : new MyLinkedList<>();
    }

    public MyLinkedList<Comodin> getListaComodin() { return listaComodin; }
    public void setListaComodin(MyLinkedList<Comodin> listaComodin) { this.listaComodin = listaComodin; }

    private String imprimirRecursivo(Node<Comodin> nodo, int indice) {
        if (nodo == null) {
            return "";
        }
        Comodin c = nodo.getInfo();
        String linea = "-----------------------------------\n"
                + "Indice: " + indice + "\n"
                + "Posicion: " + c.getPosicion() + "\n";
        return linea + imprimirRecursivo(nodo.getNext(), indice + 1);
    }

    private void eliminarEnIndiceRecursivo(Node<Comodin> anterior, int objetivo, int actual) {
        Node<Comodin> siguiente = anterior.getNext();
        if (siguiente == null) {
            return;
        }
        if (actual + 1 == objetivo) {
            anterior.setNext(siguiente.getNext());
            return;
        }
        eliminarEnIndiceRecursivo(siguiente, objetivo, actual + 1);
    }

    private void actualizarEnIndiceRecursivo(Node<Comodin> nodo, int objetivo, int actual,
            Comodin datoActualizado) {
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
