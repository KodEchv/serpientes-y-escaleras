package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.Casilla;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;

public class CasillaDAO implements OperacionDAO<Casilla> {

    private final String NOMBRE_ARCHIVO_SERIALIZADO = "casilla.bin";
    private MyLinkedList<Casilla> listaCasilla;

    public CasillaDAO() {
        listaCasilla = new MyLinkedList<>();
        leerArchivoSerializado();
    }

    @Override
    public void crear(Casilla nuevoDato) {
        listaCasilla.addLast(nuevoDato);
        escribirArchivoSerializado();
    }

    @Override
    public MyLinkedList<Casilla> obtenerLista() {
        return listaCasilla;
    }

    @Override
    public String imprimirLista() {
        if (listaCasilla.isEmpty()) {
            return "No hay casillas registradas";
        }
        return imprimirRecursivo(listaCasilla.getFirst(), 0);
    }

    @Override
    public boolean eliminarDato(int indice) {
        if (indice < 0 || indice >= listaCasilla.size()) {
            return false;
        }
        if (indice == 0) {
            listaCasilla.extract();
        } else {
            eliminarEnIndiceRecursivo(listaCasilla.getFirst(), indice, 0);
        }
        escribirArchivoSerializado();
        return true;
    }

    @Override
    public boolean actualizarDato(int indice, Casilla datoActualizado) {
        if (indice < 0 || indice >= listaCasilla.size()) {
            return false;
        }
        actualizarEnIndiceRecursivo(listaCasilla.getFirst(), indice, 0, datoActualizado);
        escribirArchivoSerializado();
        return true;
    }

    public void escribirArchivoSerializado() {
        FileHandler.checkFolder();
        FileHandler.writeSerializer(NOMBRE_ARCHIVO_SERIALIZADO, listaCasilla);
    }

    @SuppressWarnings("unchecked")
    public void leerArchivoSerializado() {
        Object obj = FileHandler.readSerialized(NOMBRE_ARCHIVO_SERIALIZADO);
        listaCasilla = (obj != null) ? (MyLinkedList<Casilla>) obj : new MyLinkedList<>();
    }

    public MyLinkedList<Casilla> getListaCasilla() { return listaCasilla; }
    public void setListaCasilla(MyLinkedList<Casilla> listaCasilla) { this.listaCasilla = listaCasilla; }

    private String imprimirRecursivo(Node<Casilla> nodo, int indice) {
        if (nodo == null) {
            return "";
        }
        Casilla c = nodo.getInfo();
        String linea = "-----------------------------------\n"
                + "Indice: " + indice + "\n"
                + "Numero: " + c.getNumeroCasilla() + "\n"
                + "Tipo: " + c.getTipo() + "\n";
        return linea + imprimirRecursivo(nodo.getNext(), indice + 1);
    }

    private void eliminarEnIndiceRecursivo(Node<Casilla> anterior, int objetivo, int actual) {
        Node<Casilla> siguiente = anterior.getNext();
        if (siguiente == null) {
            return;
        }
        if (actual + 1 == objetivo) {
            anterior.setNext(siguiente.getNext());
            return;
        }
        eliminarEnIndiceRecursivo(siguiente, objetivo, actual + 1);
    }

    private void actualizarEnIndiceRecursivo(Node<Casilla> nodo, int objetivo, int actual,
            Casilla datoActualizado) {
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
