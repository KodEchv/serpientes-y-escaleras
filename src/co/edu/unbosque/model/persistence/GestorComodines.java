package co.edu.unbosque.model.persistence;

import co.edu.unbosque.model.CasillaDTO;
import co.edu.unbosque.model.CasillaDTO.TipoCasilla;
import co.edu.unbosque.model.EscaleraDTO;
import co.edu.unbosque.model.JugadorDTO;
import co.edu.unbosque.model.MovimientoDTO;
import co.edu.unbosque.model.SerpienteDTO;
import co.edu.unbosque.utils.structure.MyLinkedList;
import co.edu.unbosque.utils.structure.NaryTree;
import co.edu.unbosque.utils.structure.NaryTree.NaryNode;
import co.edu.unbosque.utils.structure.Node;
import co.edu.unbosque.utils.structure.Vertex;

/**
 * Gestor que resuelve en tiempo de ejecucion el efecto de las casillas comodin
 * del tablero. Cuando un jugador cae en una casilla COMODIN, lanza un dado
 * adicional (1 a 6); este gestor interpreta ese valor y aplica el efecto
 * correspondiente sobre la posicion del jugador.
 *
 * <p>Efectos segun el dado comodin:</p>
 * <ul>
 *   <li><b>dado = 1</b>: mover a la base de escalera mas cercana (adelante o atras).</li>
 *   <li><b>dado = 2</b>: mover a la cabeza de serpiente mas cercana (adelante o atras).</li>
 *   <li><b>dado = 3</b>: avanzar 10 casillas (maximo 100).</li>
 *   <li><b>dado = 4</b>: retroceder 10 casillas (minimo 1).</li>
 *   <li><b>dado = 5</b>: avanzar al doble de la posicion actual (maximo 100).</li>
 *   <li><b>dado = 6</b>: retroceder a la mitad de la posicion actual,
 *       redondeado hacia abajo (minimo 1).</li>
 * </ul>
 *
 * <p>Internamente usa un {@link NaryTree} de 6 hijos para modelar el arbol de
 * decision de los efectos. El arbol se inicializa en el constructor y se consulta
 * en cada activacion para obtener el nombre descriptivo del efecto (trazabilidad
 * en el historial de movimientos).</p>
 *
 * <p>Todos los metodos recursivos carecen de ciclos (for/while/do-while).</p>
 *
 * @author Estudiante
 * @version 1.0
 */
public class GestorComodines {

    /**
     * Arbol de decision de los efectos comodin.
     * Raiz: "COMODIN_ACTIVO"
     * Hijos (indices 0-5): uno por cada valor del dado (1 a 6).
     */
    private NaryTree<String> arbolDecision;

    /**
     * Constructor de GestorComodines.
     * Inicializa el arbol de decision con los seis efectos posibles.
     * El indice de cada hijo corresponde a (dado - 1).
     */
    public GestorComodines() {
        arbolDecision = new NaryTree<String>(6);
        arbolDecision.setRoot("COMODIN_ACTIVO");
        NaryNode<String> raiz = arbolDecision.getRoot();
        // Hijo 0 -> dado=1
        arbolDecision.insertChild(raiz, "ESCALERA_CERCANA");
        // Hijo 1 -> dado=2
        arbolDecision.insertChild(raiz, "SERPIENTE_CERCANA");
        // Hijo 2 -> dado=3
        arbolDecision.insertChild(raiz, "AVANZA_10");
        // Hijo 3 -> dado=4
        arbolDecision.insertChild(raiz, "RETROCEDE_10");
        // Hijo 4 -> dado=5
        arbolDecision.insertChild(raiz, "AVANZA_DOBLE");
        // Hijo 5 -> dado=6
        arbolDecision.insertChild(raiz, "RETROCEDE_DOBLE");
    }

    // =========================================================================
    // METODO PRINCIPAL PUBLICO
    // =========================================================================

    /**
     * Activa el efecto comodin correspondiente al valor del dado comodin.
     * Actualiza la posicion del jugador, registra el movimiento en el historial
     * y retorna una descripcion del efecto aplicado.
     *
     * @param jugador      Jugador sobre el que se aplica el efecto.
     * @param dadoComodin  Valor del dado comodin lanzado (1 a 6).
     * @param tablero      Referencia al TableroDAO para consultar casillas especiales.
     * @param historial    Lista de movimientos de la partida para registrar el evento.
     * @return Descripcion del efecto activado (se usa tambien como evento en historial).
     */
    public String activarEfecto(JugadorDTO jugador, int dadoComodin,
                                TableroDAO tablero,
                                MyLinkedList<MovimientoDTO> historial) {
        int posAntes = jugador.getPosicionActual();
        String nombreEfecto = obtenerNombreEfecto(dadoComodin);

        int posFinal = resolverEfecto(posAntes, dadoComodin, tablero);

        jugador.setPosicionActual(posFinal);

        String descripcion = "COMODIN [dado=" + dadoComodin + "] "
                + nombreEfecto + ": " + posAntes + " -> " + posFinal;

        MovimientoDTO movComodin = new MovimientoDTO(
                jugador.getNombre(), dadoComodin, posAntes, posFinal, descripcion);
        historial.addLast(movComodin);

        return descripcion;
    }

    // =========================================================================
    // VERIFICACION DE CONFLICTOS
    // =========================================================================

    /**
     * Verifica que la posicion dada no coincida con ninguna cabeza/cola de serpiente
     * ni con ninguna base/cima de escalera. Se usa durante la validacion de comodines
     * generados para garantizar que no hay conflictos de posicion.
     *
     * @param posicionComodin Posicion del comodin a verificar.
     * @param serpientes      Lista de serpientes del tablero.
     * @param escaleras       Lista de escaleras del tablero.
     * @return {@code true} si la posicion esta libre de conflictos,
     *         {@code false} si coincide con alguna casilla especial de serpiente o escalera.
     */
    public boolean verificarSinConflicto(int posicionComodin,
                                          MyLinkedList<SerpienteDTO> serpientes,
                                          MyLinkedList<EscaleraDTO> escaleras) {
        if (esPosicionSerpiente(posicionComodin, serpientes.getFirst())) {
            return false;
        }
        if (esPosicionEscalera(posicionComodin, escaleras.getFirst())) {
            return false;
        }
        return true;
    }

    /**
     * Verifica recursivamente si la posicion coincide con la cabeza o la cola
     * de alguna serpiente de la lista.
     * Caso base: nodo nulo, la posicion no pertenece a ninguna serpiente.
     *
     * @param pos    Posicion a verificar.
     * @param nodo   Nodo actual de la lista de serpientes.
     * @return {@code true} si la posicion es cabeza o cola de alguna serpiente.
     */
    private boolean esPosicionSerpiente(int pos, Node<SerpienteDTO> nodo) {
        // Caso base: lista agotada sin encontrar conflicto
        if (nodo == null) {
            return false;
        }
        SerpienteDTO serpiente = nodo.getInfo();
        if (pos == serpiente.getPosicionCabeza() || pos == serpiente.getPosicionCola()) {
            return true;
        }
        return esPosicionSerpiente(pos, nodo.getNext());
    }

    /**
     * Verifica recursivamente si la posicion coincide con la base o la cima
     * de alguna escalera de la lista.
     * Caso base: nodo nulo, la posicion no pertenece a ninguna escalera.
     *
     * @param pos    Posicion a verificar.
     * @param nodo   Nodo actual de la lista de escaleras.
     * @return {@code true} si la posicion es base o cima de alguna escalera.
     */
    private boolean esPosicionEscalera(int pos, Node<EscaleraDTO> nodo) {
        // Caso base: lista agotada sin encontrar conflicto
        if (nodo == null) {
            return false;
        }
        EscaleraDTO escalera = nodo.getInfo();
        if (pos == escalera.getPosicionBase() || pos == escalera.getPosicionCima()) {
            return true;
        }
        return esPosicionEscalera(pos, nodo.getNext());
    }

    // =========================================================================
    // RESOLUCION DEL EFECTO (dispatcher recursivo via if-else)
    // =========================================================================

    /**
     * Resuelve la nueva posicion del jugador segun el valor del dado comodin.
     * Usa if-else encadenado (sin switch/case) para delegar al metodo de efecto
     * correspondiente. Si el dado esta fuera del rango 1-6, retorna la posicion sin cambio.
     *
     * @param posicion    Posicion actual del jugador.
     * @param dadoComodin Valor del dado comodin (1 a 6).
     * @param tablero     TableroDAO para efectos que buscan casillas especiales.
     * @return Nueva posicion del jugador tras aplicar el efecto.
     */
    private int resolverEfecto(int posicion, int dadoComodin, TableroDAO tablero) {
        if (dadoComodin == 1) {
            return buscarEscaleraCercanaBidireccional(posicion, tablero);
        }
        if (dadoComodin == 2) {
            return buscarSerpienteCercanaBidireccional(posicion, tablero);
        }
        if (dadoComodin == 3) {
            return efectoAvanzar10(posicion);
        }
        if (dadoComodin == 4) {
            return efectoRetroceder10(posicion);
        }
        if (dadoComodin == 5) {
            return efectoAvanzarDoble(posicion);
        }
        if (dadoComodin == 6) {
            return efectoRetrocederDoble(posicion);
        }
        // Dado invalido: sin efecto
        return posicion;
    }

    // =========================================================================
    // CONSULTA DEL ARBOL DE DECISION
    // =========================================================================

    /**
     * Obtiene el nombre descriptivo del efecto consultando el arbol de decision.
     * El hijo en el indice (dadoComodin - 1) contiene el nombre del efecto.
     * Si el dado es invalido retorna "EFECTO_DESCONOCIDO".
     *
     * @param dadoComodin Valor del dado comodin (1 a 6).
     * @return Nombre del efecto segun el arbol de decision.
     */
    private String obtenerNombreEfecto(int dadoComodin) {
        NaryNode<String> raiz = arbolDecision.getRoot();
        if (raiz == null) {
            return "EFECTO_DESCONOCIDO";
        }
        int indice = dadoComodin - 1;
        if (indice < 0 || indice >= raiz.getChildCount()) {
            return "EFECTO_DESCONOCIDO";
        }
        return raiz.getChildren()[indice].getValue();
    }

    // =========================================================================
    // EFECTOS DE BUSQUEDA BIDIRECCIONAL — ESCALERA
    // =========================================================================

    /**
     * Busca la base de escalera mas cercana en ambas direcciones desde la posicion
     * actual del jugador. Compara distancias hacia adelante y hacia atras;
     * si hay empate en distancia, prefiere la de adelante.
     * Si no hay escaleras en ninguna direccion, retorna la posicion sin cambio.
     *
     * @param posicion Posicion actual del jugador.
     * @param tablero  TableroDAO para consultar el tipo de cada casilla.
     * @return Casilla de la base de escalera mas cercana, o posicion original si no hay.
     */
    private int buscarEscaleraCercanaBidireccional(int posicion, TableroDAO tablero) {
        int posAdelante = buscarEscaleraHaciaAdelante(posicion + 1, tablero, 1);
        int posAtras    = buscarEscaleraHaciaAtras(posicion - 1, tablero, 1);

        if (posAdelante == -1 && posAtras == -1) {
            // No hay escalera en ninguna direccion: sin cambio
            return posicion;
        }
        if (posAdelante == -1) {
            return posAtras;
        }
        if (posAtras == -1) {
            return posAdelante;
        }
        // Ambas existen: elegir la de menor distancia absoluta; empate -> adelante
        int distAdelante = posAdelante - posicion;
        int distAtras    = posicion - posAtras;
        if (distAtras < distAdelante) {
            return posAtras;
        }
        return posAdelante;
    }

    /**
     * Busca recursivamente la primera casilla ESCALERA desde {@code pos} hacia adelante.
     * Caso base: {@code pos > 100}, no hay escalera en esa direccion.
     *
     * @param pos     Casilla actual de la busqueda.
     * @param tablero TableroDAO para consultar tipos de casilla.
     * @param pasos   Pasos dados desde la posicion original (no usado en logica, solo trazabilidad).
     * @return Numero de la casilla base de escalera encontrada, o -1 si no existe.
     */
    private int buscarEscaleraHaciaAdelante(int pos, TableroDAO tablero, int pasos) {
        // Caso base: fuera del tablero
        if (pos > 100) {
            return -1;
        }
        Vertex<CasillaDTO> vertice = tablero.buscarVertice(pos);
        if (vertice != null && vertice.getInfo().getTipo() == TipoCasilla.ESCALERA) {
            return pos;
        }
        return buscarEscaleraHaciaAdelante(pos + 1, tablero, pasos + 1);
    }

    /**
     * Busca recursivamente la primera casilla ESCALERA desde {@code pos} hacia atras.
     * Caso base: {@code pos < 1}, no hay escalera en esa direccion.
     *
     * @param pos     Casilla actual de la busqueda.
     * @param tablero TableroDAO para consultar tipos de casilla.
     * @param pasos   Pasos dados desde la posicion original (trazabilidad).
     * @return Numero de la casilla base de escalera encontrada, o -1 si no existe.
     */
    private int buscarEscaleraHaciaAtras(int pos, TableroDAO tablero, int pasos) {
        // Caso base: fuera del tablero
        if (pos < 1) {
            return -1;
        }
        Vertex<CasillaDTO> vertice = tablero.buscarVertice(pos);
        if (vertice != null && vertice.getInfo().getTipo() == TipoCasilla.ESCALERA) {
            return pos;
        }
        return buscarEscaleraHaciaAtras(pos - 1, tablero, pasos + 1);
    }

    // =========================================================================
    // EFECTOS DE BUSQUEDA BIDIRECCIONAL — SERPIENTE
    // =========================================================================

    /**
     * Busca la cabeza de serpiente mas cercana en ambas direcciones desde la posicion
     * actual del jugador. Compara distancias hacia adelante y hacia atras;
     * si hay empate en distancia, prefiere la de adelante.
     * Si no hay serpientes en ninguna direccion, retorna la posicion sin cambio.
     *
     * @param posicion Posicion actual del jugador.
     * @param tablero  TableroDAO para consultar el tipo de cada casilla.
     * @return Casilla de la cabeza de serpiente mas cercana, o posicion original si no hay.
     */
    private int buscarSerpienteCercanaBidireccional(int posicion, TableroDAO tablero) {
        int posAdelante = buscarSerpienteHaciaAdelante(posicion + 1, tablero, 1);
        int posAtras    = buscarSerpienteHaciaAtras(posicion - 1, tablero, 1);

        if (posAdelante == -1 && posAtras == -1) {
            return posicion;
        }
        if (posAdelante == -1) {
            return posAtras;
        }
        if (posAtras == -1) {
            return posAdelante;
        }
        int distAdelante = posAdelante - posicion;
        int distAtras    = posicion - posAtras;
        if (distAtras < distAdelante) {
            return posAtras;
        }
        return posAdelante;
    }

    /**
     * Busca recursivamente la primera casilla SERPIENTE desde {@code pos} hacia adelante.
     * Caso base: {@code pos > 100}, no hay serpiente en esa direccion.
     *
     * @param pos     Casilla actual de la busqueda.
     * @param tablero TableroDAO para consultar tipos de casilla.
     * @param pasos   Pasos dados desde la posicion original (trazabilidad).
     * @return Numero de la casilla cabeza de serpiente encontrada, o -1 si no existe.
     */
    private int buscarSerpienteHaciaAdelante(int pos, TableroDAO tablero, int pasos) {
        // Caso base: fuera del tablero
        if (pos > 100) {
            return -1;
        }
        Vertex<CasillaDTO> vertice = tablero.buscarVertice(pos);
        if (vertice != null && vertice.getInfo().getTipo() == TipoCasilla.SERPIENTE) {
            return pos;
        }
        return buscarSerpienteHaciaAdelante(pos + 1, tablero, pasos + 1);
    }

    /**
     * Busca recursivamente la primera casilla SERPIENTE desde {@code pos} hacia atras.
     * Caso base: {@code pos < 1}, no hay serpiente en esa direccion.
     *
     * @param pos     Casilla actual de la busqueda.
     * @param tablero TableroDAO para consultar tipos de casilla.
     * @param pasos   Pasos dados desde la posicion original (trazabilidad).
     * @return Numero de la casilla cabeza de serpiente encontrada, o -1 si no existe.
     */
    private int buscarSerpienteHaciaAtras(int pos, TableroDAO tablero, int pasos) {
        // Caso base: fuera del tablero
        if (pos < 1) {
            return -1;
        }
        Vertex<CasillaDTO> vertice = tablero.buscarVertice(pos);
        if (vertice != null && vertice.getInfo().getTipo() == TipoCasilla.SERPIENTE) {
            return pos;
        }
        return buscarSerpienteHaciaAtras(pos - 1, tablero, pasos + 1);
    }

    // =========================================================================
    // EFECTOS DE POSICION DIRECTA
    // =========================================================================

    /**
     * Calcula la nueva posicion avanzando 10 casillas desde la posicion actual.
     * La posicion resultante no puede superar la casilla 100.
     *
     * @param posicion Posicion actual del jugador.
     * @return Nueva posicion tras avanzar 10, como maximo 100.
     */
    private int efectoAvanzar10(int posicion) {
        int resultado = posicion + 10;
        if (resultado > 100) {
            return 100;
        }
        return resultado;
    }

    /**
     * Calcula la nueva posicion retrocediendo 10 casillas desde la posicion actual.
     * La posicion resultante no puede ser menor a 1.
     *
     * @param posicion Posicion actual del jugador.
     * @return Nueva posicion tras retroceder 10, como minimo 1.
     */
    private int efectoRetroceder10(int posicion) {
        int resultado = posicion - 10;
        if (resultado < 1) {
            return 1;
        }
        return resultado;
    }

    /**
     * Calcula la nueva posicion multiplicando por 2 la posicion actual.
     * La posicion resultante no puede superar la casilla 100.
     *
     * @param posicion Posicion actual del jugador.
     * @return Nueva posicion tras doblar, como maximo 100.
     */
    private int efectoAvanzarDoble(int posicion) {
        int resultado = posicion * 2;
        if (resultado > 100) {
            return 100;
        }
        return resultado;
    }

    /**
     * Calcula la nueva posicion dividiendo entre 2 la posicion actual,
     * redondeando hacia abajo (division entera).
     * La posicion resultante no puede ser menor a 1.
     *
     * @param posicion Posicion actual del jugador.
     * @return Nueva posicion tras reducir a la mitad, como minimo 1.
     */
    private int efectoRetrocederDoble(int posicion) {
        int resultado = posicion / 2;
        if (resultado < 1) {
            return 1;
        }
        return resultado;
    }
}
