package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.Serpiente;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;

public class SerpienteDAO implements OperacionDAO<Serpiente> {

    private final String NOMBRE_ARCHIVO_SERIALIZADO = "serpiente.bin";
    private MyLinkedList<Serpiente> listaSerpiente;

    public SerpienteDAO() {
        listaSerpiente = new MyLinkedList<>();
        leerArchivoSerializado();
    }

    @Override
    public void crear(Serpiente nuevoDato) {
        listaSerpiente.addLast(nuevoDato);
        escribirArchivoSerializado();
    }

    @Override
    public MyLinkedList<Serpiente> obtenerLista() {
        return listaSerpiente;
    }

    @Override
    public String imprimirLista() {
        if (listaSerpiente.isEmpty()) {
            return "No hay serpientes registradas";
        }
        return imprimirRecursivo(listaSerpiente.getFirst(), 0);
    }

    @Override
    public boolean eliminarDato(int indice) {
        if (indice < 0 || indice >= listaSerpiente.size()) {
            return false;
        }
        if (indice == 0) {
            listaSerpiente.extract();
        } else {
            eliminarEnIndiceRecursivo(listaSerpiente.getFirst(), indice, 0);
        }
        escribirArchivoSerializado();
        return true;
    }

    @Override
    public boolean actualizarDato(int indice, Serpiente datoActualizado) {
        if (indice < 0 || indice >= listaSerpiente.size()) {
            return false;
        }
        actualizarEnIndiceRecursivo(listaSerpiente.getFirst(), indice, 0, datoActualizado);
        escribirArchivoSerializado();
        return true;
    }

    public void escribirArchivoSerializado() {
        FileHandler.checkFolder();
        FileHandler.writeSerializer(NOMBRE_ARCHIVO_SERIALIZADO, listaSerpiente);
    }

    @SuppressWarnings("unchecked")
    public void leerArchivoSerializado() {
        Object obj = FileHandler.readSerialized(NOMBRE_ARCHIVO_SERIALIZADO);
        listaSerpiente = (obj != null) ? (MyLinkedList<Serpiente>) obj : new MyLinkedList<>();
    }

    public MyLinkedList<Serpiente> getListaSerpiente() { return listaSerpiente; }
    public void setListaSerpiente(MyLinkedList<Serpiente> listaSerpiente) { this.listaSerpiente = listaSerpiente; }

    private String imprimirRecursivo(Node<Serpiente> nodo, int indice) {
        if (nodo == null) {
            return "";
        }
        Serpiente s = nodo.getInfo();
        String linea = "-----------------------------------\n"
                + "Indice: " + indice + "\n"
                + "Cabeza: " + s.getPosicionCabeza() + "\n"
                + "Cola: " + s.getPosicionCola() + "\n";
        return linea + imprimirRecursivo(nodo.getNext(), indice + 1);
    }

    private void eliminarEnIndiceRecursivo(Node<Serpiente> anterior, int objetivo, int actual) {
        Node<Serpiente> siguiente = anterior.getNext();
        if (siguiente == null) {
            return;
        }
        if (actual + 1 == objetivo) {
            anterior.setNext(siguiente.getNext());
            return;
        }
        eliminarEnIndiceRecursivo(siguiente, objetivo, actual + 1);
    }

    private void actualizarEnIndiceRecursivo(Node<Serpiente> nodo, int objetivo, int actual,
            Serpiente datoActualizado) {
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
