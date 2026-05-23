package co.edu.unbosque.model.persistence;

import co.edu.unbosque.utils.structure.MyLinkedList;

/**
 * Interfaz generica CRUD que todos los DAOs del sistema deben implementar.
 * Define el contrato minimo de operaciones sobre una coleccion de tipo T.
 *
 * @param <T> Tipo de dato gestionado por el DAO.
 */
public interface OperacionDAO<T> {

    /**
     * Agrega un nuevo elemento a la coleccion.
     *
     * @param nuevoDato Elemento a agregar. No debe ser null.
     */
    void crear(T nuevoDato);

    /**
     * Retorna la coleccion completa de elementos.
     *
     * @return MyLinkedList con todos los elementos registrados.
     */
    MyLinkedList<T> obtenerLista();

    /**
     * Genera una representacion en texto de todos los elementos de la coleccion.
     *
     * @return String formateado con la informacion de cada elemento,
     *         o un mensaje indicando que la coleccion esta vacia.
     */
    String imprimirLista();

    /**
     * Elimina el elemento ubicado en el indice dado (base 0).
     *
     * @param indice Posicion del elemento a eliminar.
     * @return true si el indice era valido y el elemento fue eliminado,
     *         false si el indice esta fuera de rango.
     */
    boolean eliminarDato(int indice);

    /**
     * Reemplaza el elemento ubicado en el indice dado con el nuevo valor.
     *
     * @param indice         Posicion del elemento a actualizar.
     * @param datoActualizado Nuevo valor que sustituye al existente.
     * @return true si el indice era valido y la actualizacion se realizo,
     *         false si el indice esta fuera de rango.
     */
    boolean actualizarDato(int indice, T datoActualizado);
}
