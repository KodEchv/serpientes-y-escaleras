package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.Ranking;
import co.edu.unbosque.utils.structure.AVLTree;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.Node;

public class RankingDAO implements OperacionDAO<Ranking> {

    private final String NOMBRE_ARCHIVO_SERIALIZADO = "ranking.bin";
    private MyLinkedList<Ranking> listaRanking;
    private AVLTree<Ranking> arbolRanking;

    public RankingDAO() {
        listaRanking = new MyLinkedList<>();
        arbolRanking = new AVLTree<>();
        leerArchivoSerializado();
    }

    @Override
    public void crear(Ranking nuevoDato) {
        listaRanking.addLast(nuevoDato);
        arbolRanking.insert(nuevoDato.calcularClave(), nuevoDato);
        escribirArchivoSerializado();
    }

    @Override
    public MyLinkedList<Ranking> obtenerLista() {
        return listaRanking;
    }

    @Override
    public String imprimirLista() {
        if (listaRanking.isEmpty()) {
            return "El ranking esta vacio. Aun no se ha registrado ninguna partida.";
        }
        return imprimirRecursivo(listaRanking.getFirst(), 0);
    }

    @Override
    public boolean eliminarDato(int indice) {
        if (indice < 0 || indice >= listaRanking.size()) {
            return false;
        }
        if (indice == 0) {
            listaRanking.extract();
        } else {
            eliminarEnIndiceRecursivo(listaRanking.getFirst(), indice, 0);
        }
        arbolRanking = new AVLTree<>();
        reconstruirArbolRecursivo(listaRanking.getFirst());
        escribirArchivoSerializado();
        return true;
    }

    @Override
    public boolean actualizarDato(int indice, Ranking datoActualizado) {
        if (indice < 0 || indice >= listaRanking.size()) {
            return false;
        }
        actualizarEnIndiceRecursivo(listaRanking.getFirst(), indice, 0, datoActualizado);
        arbolRanking = new AVLTree<>();
        reconstruirArbolRecursivo(listaRanking.getFirst());
        escribirArchivoSerializado();
        return true;
    }

    public void registrarJugador(JugadorDTO jugador, boolean gano) {
        Ranking entrada = new Ranking(
                jugador.getNombre(),
                jugador.getPosicionActual(),
                jugador.getCantidadTurnos(),
                gano
        );
        crear(entrada);
    }

    public Object[] obtenerRankingOrdenado() {
        return arbolRanking.inorderArray();
    }

    public void imprimirRanking() {
        if (arbolRanking.isEmpty()) {
            System.out.println("El ranking esta vacio. Aun no se ha registrado ninguna partida.");
            return;
        }
        System.out.println("=== RANKING DE LA PARTIDA ===");
        System.out.println(arbolRanking.inorderString());
        System.out.println("=============================");
    }

    public Ranking buscarPorClave(int claveExacta) {
        return arbolRanking.search(claveExacta);
    }

    public Ranking buscarPorPosicion(int posicion) {
        Object[] arreglo = arbolRanking.inorderArray();
        return buscarEnArregloRecursivo(arreglo, posicion, 0);
    }

    public boolean estaVacio() {
        return arbolRanking.isEmpty();
    }

    public void escribirArchivoSerializado() {
        FileHandler.checkFolder();
        FileHandler.writeSerializer(NOMBRE_ARCHIVO_SERIALIZADO, listaRanking);
    }

    @SuppressWarnings("unchecked")
    public void leerArchivoSerializado() {
        Object obj = FileHandler.readSerialized(NOMBRE_ARCHIVO_SERIALIZADO);
        if (obj != null) {
            listaRanking = (MyLinkedList<Ranking>) obj;
            reconstruirArbolRecursivo(listaRanking.getFirst());
        } else {
            listaRanking = new MyLinkedList<>();
        }
    }

    public MyLinkedList<Ranking> getListaRanking() { return listaRanking; }
    public void setListaRanking(MyLinkedList<Ranking> listaRanking) { this.listaRanking = listaRanking; }

    private String imprimirRecursivo(Node<Ranking> nodo, int indice) {
        if (nodo == null) {
            return "";
        }
        Ranking r = nodo.getInfo();
        String estado = r.isGano() ? "GANADOR" : "posicion " + r.getPosicionFinal();
        String linea = "-----------------------------------\n"
                + "Indice: " + indice + "\n"
                + "Jugador: " + r.getNombreJugador() + "\n"
                + "Estado: " + estado + "\n"
                + "Turnos: " + r.getCantidadTurnos() + "\n";
        return linea + imprimirRecursivo(nodo.getNext(), indice + 1);
    }

    private void eliminarEnIndiceRecursivo(Node<Ranking> anterior, int objetivo, int actual) {
        Node<Ranking> siguiente = anterior.getNext();
        if (siguiente == null) {
            return;
        }
        if (actual + 1 == objetivo) {
            anterior.setNext(siguiente.getNext());
            return;
        }
        eliminarEnIndiceRecursivo(siguiente, objetivo, actual + 1);
    }

    private void actualizarEnIndiceRecursivo(Node<Ranking> nodo, int objetivo, int actual,
            Ranking datoActualizado) {
        if (nodo == null) {
            return;
        }
        if (actual == objetivo) {
            nodo.setInfo(datoActualizado);
            return;
        }
        actualizarEnIndiceRecursivo(nodo.getNext(), objetivo, actual + 1, datoActualizado);
    }

    private void reconstruirArbolRecursivo(Node<Ranking> nodo) {
        if (nodo == null) {
            return;
        }
        Ranking r = nodo.getInfo();
        arbolRanking.insert(r.calcularClave(), r);
        reconstruirArbolRecursivo(nodo.getNext());
    }

    private Ranking buscarEnArregloRecursivo(Object[] arreglo, int posicion, int indice) {
        if (indice >= arreglo.length) {
            return null;
        }
        Ranking entrada = (Ranking) arreglo[indice];
        if (entrada.getPosicionFinal() == posicion) {
            return entrada;
        }
        return buscarEnArregloRecursivo(arreglo, posicion, indice + 1);
    }
}
