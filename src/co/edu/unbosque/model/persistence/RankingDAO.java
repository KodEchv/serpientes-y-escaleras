package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.RankingDTO;
import co.edu.unbosque.utils.structure.AVLTree;

/**
 * DAO que gestiona el ranking de jugadores usando un {@link AVLTree} como
 * estructura de almacenamiento ordenado.
 *
 * <p>La clave entera del AVL se calcula como:
 * {@code posicionFinal * 1000 - cantidadTurnos}. Esto garantiza que:</p>
 * <ul>
 *   <li>Mayor posicion final = clave mas alta = mejor ranking.</li>
 *   <li>En empate de posicion, menos turnos = clave mas alta = mejor ranking.</li>
 * </ul>
 *
 * <p>El recorrido inorder del AVL entrega los resultados de menor a mayor clave,
 * lo que equivale a peor a mejor rendimiento en la partida.</p>
 *
 */
public class RankingDAO {

    /** Arbol AVL que almacena los RankingDTO ordenados por clave de puntaje. */
    private AVLTree<RankingDTO> arbolRanking;

    /**
     * Constructor de RankingDAO.
     * Inicializa el arbol AVL vacio.
     */
    public RankingDAO() {
        this.arbolRanking = new AVLTree<RankingDTO>();
    }

    /**
     * Registra un jugador en el ranking al finalizar la partida.
     * Construye un {@link RankingDTO} con los datos del jugador y lo inserta
     * en el AVL usando la clave calculada por {@link RankingDTO#calcularClave()}.
     *
     * <p>Si dos jugadores tienen la misma clave (misma posicion y mismos turnos),
     * el AVL actualiza el valor del nodo con el nuevo RankingDTO.</p>
     *
     * @param jugador JugadorDTO con el estado final del jugador.
     * @param gano    true si el jugador alcanzo la casilla 100 y gano la partida.
     */
    public void registrarJugador(JugadorDTO jugador, boolean gano) {
        RankingDTO entrada = new RankingDTO(
                jugador.getNombre(),
                jugador.getPosicionActual(),
                jugador.getCantidadTurnos(),
                gano
        );
        arbolRanking.insert(entrada.calcularClave(), entrada);
    }

    /**
     * Retorna todos los registros del ranking en orden ascendente de puntaje
     * (del peor al mejor resultado), usando el recorrido inorder del AVL.
     *
     * <p>El ultimo elemento del arreglo corresponde al mejor jugador de la partida.</p>
     *
     * @return Arreglo de {@link Object} con los {@link RankingDTO} ordenados.
     *         Cada elemento puede ser casteado a RankingDTO de forma segura.
     */
    public Object[] obtenerRankingOrdenado() {
        return arbolRanking.inorderArray();
    }

    /**
     * Imprime por consola el ranking completo en orden ascendente de puntaje,
     * usando la representacion inorder del AVL.
     * Si el arbol esta vacio, imprime un mensaje indicandolo.
     */
    public void imprimirRanking() {
        if (arbolRanking.isEmpty()) {
            System.out.println("El ranking esta vacio. Aun no se ha registrado ninguna partida.");
            return;
        }
        System.out.println("=== RANKING DE LA PARTIDA ===");
        System.out.println(arbolRanking.inorderString());
        System.out.println("=============================");
    }

    /**
     * Busca en el AVL el registro de ranking correspondiente a la posicion
     * final indicada. Dado que la clave incluye los turnos, esta busqueda
     * solo funciona si se conoce la clave exacta. Para busqueda aproximada
     * usar {@link #obtenerRankingOrdenado()} y filtrar manualmente.
     *
     * <p>Nota: este metodo busca por clave exacta {@code posicionFinal * 1000 - turnos}.
     * Si no se conocen los turnos, se recomienda iterar el arreglo inorder.</p>
     *
     * @param claveExacta Clave entera calculada como posicionFinal*1000 - cantidadTurnos.
     * @return El {@link RankingDTO} encontrado, o null si no existe esa clave.
     */
    public RankingDTO buscarPorClave(int claveExacta) {
        return arbolRanking.search(claveExacta);
    }

    /**
     * Busca el registro de ranking cuya posicion final coincida con la indicada,
     * recorriendo recursivamente el arreglo inorder del AVL.
     *
     * @param posicion Posicion final del jugador buscado.
     * @return El {@link RankingDTO} con esa posicion final, o null si no existe.
     */
    public RankingDTO buscarPorPosicion(int posicion) {
        Object[] arreglo = arbolRanking.inorderArray();
        return buscarEnArregloRecursivo(arreglo, posicion, 0);
    }

    /**
     * Verifica si el arbol de ranking esta vacio.
     *
     * @return true si no se ha registrado ningun jugador, false en caso contrario.
     */
    public boolean estaVacio() {
        return arbolRanking.isEmpty();
    }

    // =========================================================================
    // METODOS RECURSIVOS PRIVADOS
    // =========================================================================

    /**
     * Busca recursivamente en el arreglo inorder del AVL el primer RankingDTO
     * cuya posicion final coincida con la buscada.
     * Caso base: indice fuera de rango retorna null.
     *
     * @param arreglo  Arreglo con los RankingDTO en orden inorder.
     * @param posicion Posicion final buscada.
     * @param indice   Indice actual del arreglo en la recursion.
     * @return El RankingDTO encontrado, o null si no existe.
     */
    private RankingDTO buscarEnArregloRecursivo(Object[] arreglo, int posicion, int indice) {
        if (indice >= arreglo.length) {
            return null;
        }
        RankingDTO entrada = (RankingDTO) arreglo[indice];
        if (entrada.getPosicionFinal() == posicion) {
            return entrada;
        }
        return buscarEnArregloRecursivo(arreglo, posicion, indice + 1);
    }
}
