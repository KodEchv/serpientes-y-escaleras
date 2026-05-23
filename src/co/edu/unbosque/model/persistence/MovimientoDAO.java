package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.Movimiento;
import co.edu.unbosque.model.MovimientoDTO;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;

public class MovimientoDAO implements OperacionDAO<Movimiento> {

    private final String NOMBRE_ARCHIVO_SERIALIZADO = "movimiento.bin";
    private MyLinkedList<Movimiento> listaMovimiento;

    public MovimientoDAO() {
        listaMovimiento = new MyLinkedList<>();
        leerArchivoSerializado();
    }

    @Override
    public void crear(Movimiento nuevoDato) {
        listaMovimiento.addLast(nuevoDato);
        escribirArchivoSerializado();
    }

    @Override
    public MyLinkedList<Movimiento> obtenerLista() {
        return listaMovimiento;
    }

    @Override
    public String imprimirLista() {
        if (listaMovimiento.isEmpty()) {
            return "No hay movimientos registrados";
        }
        return imprimirRecursivo(listaMovimiento.getFirst(), 0);
    }

    @Override
    public boolean eliminarDato(int indice) {
        if (indice < 0 || indice >= listaMovimiento.size()) {
            return false;
        }
        if (indice == 0) {
            listaMovimiento.extract();
        } else {
            eliminarEnIndiceRecursivo(listaMovimiento.getFirst(), indice, 0);
        }
        escribirArchivoSerializado();
        return true;
    }

    @Override
    public boolean actualizarDato(int indice, Movimiento datoActualizado) {
        if (indice < 0 || indice >= listaMovimiento.size()) {
            return false;
        }
        actualizarEnIndiceRecursivo(listaMovimiento.getFirst(), indice, 0, datoActualizado);
        escribirArchivoSerializado();
        return true;
    }

    public void registrarMovimiento(MovimientoDTO dto) {
        Movimiento m = new Movimiento(
                dto.getNombreJugador(),
                dto.getDado(),
                dto.getPosAntes(),
                dto.getPosDespues(),
                dto.getEvento()
        );
        crear(m);
    }

    public void imprimirHistorialCompleto() {
        if (listaMovimiento.isEmpty()) {
            System.out.println("El historial esta vacio. Aun no se han registrado movimientos.");
            return;
        }
        System.out.println("=== HISTORIAL DE MOVIMIENTOS ===");
        System.out.println(imprimirLista());
        System.out.println("================================");
    }

    public int tamanioHistorial() {
        return contarRecursivo(listaMovimiento.getFirst());
    }

    public void escribirArchivoSerializado() {
        FileHandler.checkFolder();
        FileHandler.writeSerializer(NOMBRE_ARCHIVO_SERIALIZADO, listaMovimiento);
    }

    @SuppressWarnings("unchecked")
    public void leerArchivoSerializado() {
        Object obj = FileHandler.readSerialized(NOMBRE_ARCHIVO_SERIALIZADO);
        listaMovimiento = (obj != null) ? (MyLinkedList<Movimiento>) obj : new MyLinkedList<>();
    }

    public MyLinkedList<Movimiento> getListaMovimiento() { return listaMovimiento; }
    public void setListaMovimiento(MyLinkedList<Movimiento> listaMovimiento) { this.listaMovimiento = listaMovimiento; }

    private String imprimirRecursivo(Node<Movimiento> nodo, int indice) {
        if (nodo == null) {
            return "";
        }
        Movimiento m = nodo.getInfo();
        String linea = "-----------------------------------\n"
                + "Indice: " + indice + "\n"
                + "Jugador: " + m.getNombreJugador() + "\n"
                + "Dado: " + m.getDado() + "\n"
                + "Posicion: " + m.getPosAntes() + " -> " + m.getPosDespues() + "\n"
                + "Evento: " + m.getEvento() + "\n";
        return linea + imprimirRecursivo(nodo.getNext(), indice + 1);
    }

    private void eliminarEnIndiceRecursivo(Node<Movimiento> anterior, int objetivo, int actual) {
        Node<Movimiento> siguiente = anterior.getNext();
        if (siguiente == null) {
            return;
        }
        if (actual + 1 == objetivo) {
            anterior.setNext(siguiente.getNext());
            return;
        }
        eliminarEnIndiceRecursivo(siguiente, objetivo, actual + 1);
    }

    private void actualizarEnIndiceRecursivo(Node<Movimiento> nodo, int objetivo, int actual,
            Movimiento datoActualizado) {
        if (nodo == null) {
            return;
        }
        if (actual == objetivo) {
            nodo.setInfo(datoActualizado);
            return;
        }
        actualizarEnIndiceRecursivo(nodo.getNext(), objetivo, actual + 1, datoActualizado);
    }

    private int contarRecursivo(Node<Movimiento> nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarRecursivo(nodo.getNext());
    }
}
