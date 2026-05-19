package co.edu.unbosque.model.persistencie;

import java.util.Random;

import co.edu.unbosque.model.ComodinDTO;
import co.edu.unbosque.model.EscaleraDTO;
import co.edu.unbosque.model.SerpienteDTO;
import co.edu.unbosque.utils.exception.PosicionInvalidaException;
import co.edu.unbosque.utils.structure.MyLinkedList;

/**
 * Clase responsable de la generacion aleatoria y recursiva del contenido del tablero.
 * Genera serpientes, escaleras y comodines respetando las restricciones del juego,
 * sin usar ciclos (for, while, do-while).
 *
 * <p>Restricciones que garantiza:</p>
 * <ul>
 *   <li>Casillas 1 y 100 nunca tienen serpiente, escalera ni comodin.</li>
 *   <li>No superposicion: una casilla no puede ser cabeza de serpiente Y base de escalera.</li>
 *   <li>Serpientes: cabeza entre 20 y 99, cola menor que cabeza, minimo 8, maximo 12.</li>
 *   <li>Escaleras: base entre 2 y 80, cima mayor que base, minimo 8, maximo 12.</li>
 *   <li>Comodines: entre 5 y 8, posicion entre 2 y 99.</li>
 * </ul>
 *
 * @author Estudiante
 * @version 1.0
 */
public class GeneradorTablero {

    /** Generador de numeros aleatorios. */
    private Random aleatorio;

    /** Arreglo de 101 booleanos (indices 1-100) que indica casillas ya ocupadas. */
    private boolean[] casillasOcupadas;

    /** Cantidad minima de serpientes. */
    private static final int MIN_SERPIENTES = 8;

    /** Cantidad maxima de serpientes. */
    private static final int MAX_SERPIENTES = 12;

    /** Cantidad minima de escaleras. */
    private static final int MIN_ESCALERAS = 8;

    /** Cantidad maxima de escaleras. */
    private static final int MAX_ESCALERAS = 12;

    /** Cantidad exacta de comodines que se generan en cada partida. */
    private static final int CANTIDAD_COMODINES = 7;

    /**
     * Constructor de GeneradorTablero.
     * Inicializa el generador de aleatorios y el arreglo de ocupacion.
     */
    public GeneradorTablero() {
        this.aleatorio = new Random();
        this.casillasOcupadas = new boolean[101];
        // Las casillas 1 y 100 siempre estan bloqueadas
        this.casillasOcupadas[1] = true;
        this.casillasOcupadas[100] = true;
    }

    // =========================================================================
    // GENERACION DE SERPIENTES
    // =========================================================================

    /**
     * Genera la lista de serpientes de forma recursiva.
     * La cantidad es aleatoria entre MIN_SERPIENTES y MAX_SERPIENTES.
     *
     * @return Lista enlazada con los DTOs de todas las serpientes generadas.
     * @throws PosicionInvalidaException Si no se puede colocar una serpiente valida
     *                                   en el numero de intentos permitidos.
     */
    public MyLinkedList<SerpienteDTO> generarSerpientes() throws PosicionInvalidaException {
        int cantidad = MIN_SERPIENTES + aleatorio.nextInt(MAX_SERPIENTES - MIN_SERPIENTES + 1);
        MyLinkedList<SerpienteDTO> lista = new MyLinkedList<SerpienteDTO>();
        return generarSerpientesRecursivo(lista, cantidad);
    }

    /**
     * Metodo recursivo auxiliar para generar serpientes una por una.
     *
     * @param lista      Lista acumulada de serpientes generadas.
     * @param restantes  Cantidad de serpientes que faltan por generar.
     * @return Lista con todas las serpientes generadas.
     * @throws PosicionInvalidaException Si no se encuentra posicion valida.
     */
    private MyLinkedList<SerpienteDTO> generarSerpientesRecursivo(
            MyLinkedList<SerpienteDTO> lista, int restantes) throws PosicionInvalidaException {
        if (restantes == 0) {
            return lista;
        }
        SerpienteDTO serpiente = generarUnaSerpiente(100);
        lista.addLast(serpiente);
        return generarSerpientesRecursivo(lista, restantes - 1);
    }

    /**
     * Genera una sola serpiente valida de forma recursiva con reintentos.
     * La cabeza debe estar entre 20 y 99, libre y no ocupada.
     * La cola debe ser menor que la cabeza, libre y no ocupada.
     *
     * @param intentosRestantes Numero de intentos disponibles antes de lanzar excepcion.
     * @return Un SerpienteDTO valido con posiciones libres.
     * @throws PosicionInvalidaException Si se agotan los intentos sin encontrar posicion.
     */
    private SerpienteDTO generarUnaSerpiente(int intentosRestantes) throws PosicionInvalidaException {
        if (intentosRestantes == 0) {
            throw new PosicionInvalidaException(
                "No se pudo encontrar posicion valida para una serpiente tras multiples intentos.");
        }
        // Cabeza: entre 20 y 99
        int cabeza = 20 + aleatorio.nextInt(80);
        if (casillasOcupadas[cabeza]) {
            return generarUnaSerpiente(intentosRestantes - 1);
        }
        // Cola: entre 1 y cabeza-1 (minimo separacion de 5 casillas para que sea efectiva)
        int rangoMaxCola = cabeza - 5;
        if (rangoMaxCola < 2) {
            return generarUnaSerpiente(intentosRestantes - 1);
        }
        int cola = 2 + aleatorio.nextInt(rangoMaxCola - 1);
        if (casillasOcupadas[cola]) {
            return generarUnaSerpiente(intentosRestantes - 1);
        }
        // Marcar ambas posiciones como ocupadas
        casillasOcupadas[cabeza] = true;
        casillasOcupadas[cola] = true;
        return new SerpienteDTO(cabeza, cola);
    }

    // =========================================================================
    // GENERACION DE ESCALERAS
    // =========================================================================

    /**
     * Genera la lista de escaleras de forma recursiva.
     * La cantidad es aleatoria entre MIN_ESCALERAS y MAX_ESCALERAS.
     *
     * @return Lista enlazada con los DTOs de todas las escaleras generadas.
     * @throws PosicionInvalidaException Si no se puede colocar una escalera valida.
     */
    public MyLinkedList<EscaleraDTO> generarEscaleras() throws PosicionInvalidaException {
        int cantidad = MIN_ESCALERAS + aleatorio.nextInt(MAX_ESCALERAS - MIN_ESCALERAS + 1);
        MyLinkedList<EscaleraDTO> lista = new MyLinkedList<EscaleraDTO>();
        return generarEscalerasRecursivo(lista, cantidad);
    }

    /**
     * Metodo recursivo auxiliar para generar escaleras una por una.
     *
     * @param lista      Lista acumulada de escaleras generadas.
     * @param restantes  Cantidad de escaleras que faltan por generar.
     * @return Lista con todas las escaleras generadas.
     * @throws PosicionInvalidaException Si no se encuentra posicion valida.
     */
    private MyLinkedList<EscaleraDTO> generarEscalerasRecursivo(
            MyLinkedList<EscaleraDTO> lista, int restantes) throws PosicionInvalidaException {
        if (restantes == 0) {
            return lista;
        }
        EscaleraDTO escalera = generarUnaEscalera(100);
        lista.addLast(escalera);
        return generarEscalerasRecursivo(lista, restantes - 1);
    }

    /**
     * Genera una sola escalera valida de forma recursiva con reintentos.
     * La base debe estar entre 2 y 80, libre y no ocupada.
     * La cima debe ser mayor que la base, libre y no ocupada.
     *
     * @param intentosRestantes Numero de intentos disponibles.
     * @return Un EscaleraDTO valido con posiciones libres.
     * @throws PosicionInvalidaException Si se agotan los intentos.
     */
    private EscaleraDTO generarUnaEscalera(int intentosRestantes) throws PosicionInvalidaException {
        if (intentosRestantes == 0) {
            throw new PosicionInvalidaException(
                "No se pudo encontrar posicion valida para una escalera tras multiples intentos.");
        }
        // Base: entre 2 y 80
        int base = 2 + aleatorio.nextInt(79);
        if (casillasOcupadas[base]) {
            return generarUnaEscalera(intentosRestantes - 1);
        }
        // Cima: entre base+5 y 99 (minimo separacion de 5 casillas)
        int rangoMinCima = base + 5;
        if (rangoMinCima > 99) {
            return generarUnaEscalera(intentosRestantes - 1);
        }
        int rangoCima = 99 - rangoMinCima;
        if (rangoCima < 1) {
            return generarUnaEscalera(intentosRestantes - 1);
        }
        int cima = rangoMinCima + aleatorio.nextInt(rangoCima);
        if (casillasOcupadas[cima]) {
            return generarUnaEscalera(intentosRestantes - 1);
        }
        // Marcar ambas posiciones como ocupadas
        casillasOcupadas[base] = true;
        casillasOcupadas[cima] = true;
        return new EscaleraDTO(base, cima);
    }

    // =========================================================================
    // GENERACION DE COMODINES
    // =========================================================================

    /**
     * Genera la lista de comodines de forma recursiva.
     * Genera exactamente CANTIDAD_COMODINES (7) comodines por partida.
     * El efecto de cada comodin se decide en tiempo de ejecucion cuando el
     * jugador cae en la casilla y lanza el dado comodin (ver GestorComodines).
     *
     * @return Lista enlazada con exactamente 7 ComodinDTO generados.
     * @throws PosicionInvalidaException Si no se puede colocar un comodin valido
     *                                   tras los intentos disponibles.
     */
    public MyLinkedList<ComodinDTO> generarComodines() throws PosicionInvalidaException {
        MyLinkedList<ComodinDTO> lista = new MyLinkedList<ComodinDTO>();
        return generarComodinesRecursivo(lista, CANTIDAD_COMODINES);
    }

    /**
     * Metodo recursivo auxiliar para generar comodines uno por uno hasta
     * completar la cantidad requerida.
     *
     * @param lista      Lista acumulada de comodines ya generados.
     * @param restantes  Cantidad de comodines que faltan por generar.
     * @return Lista con todos los comodines generados.
     * @throws PosicionInvalidaException Si no se encuentra posicion valida.
     */
    private MyLinkedList<ComodinDTO> generarComodinesRecursivo(
            MyLinkedList<ComodinDTO> lista, int restantes) throws PosicionInvalidaException {
        // Caso base: ya se generaron todos los comodines necesarios
        if (restantes == 0) {
            return lista;
        }
        ComodinDTO comodin = generarUnComodin(100);
        lista.addLast(comodin);
        return generarComodinesRecursivo(lista, restantes - 1);
    }

    /**
     * Genera un solo comodin valido de forma recursiva con reintentos.
     * La posicion debe estar entre 2 y 99, libre y no ocupada por ninguna
     * serpiente, escalera u otro comodin ya colocado.
     * El comodin no tiene tipo ni destino precalculado; solo almacena su posicion.
     *
     * @param intentosRestantes Numero de intentos disponibles antes de lanzar excepcion.
     * @return Un ComodinDTO valido con posicion libre en el tablero.
     * @throws PosicionInvalidaException Si se agotan los intentos sin encontrar posicion.
     */
    private ComodinDTO generarUnComodin(int intentosRestantes) throws PosicionInvalidaException {
        // Caso base: sin intentos disponibles
        if (intentosRestantes == 0) {
            throw new PosicionInvalidaException(
                "No se pudo encontrar posicion valida para un comodin tras multiples intentos.");
        }
        // Posicion aleatoria entre 2 y 99 (casillas 1 y 100 siempre bloqueadas)
        int posicion = 2 + aleatorio.nextInt(98);
        if (casillasOcupadas[posicion]) {
            return generarUnComodin(intentosRestantes - 1);
        }
        casillasOcupadas[posicion] = true;
        return new ComodinDTO(posicion);
    }

    // =========================================================================
    // VERIFICACION DE RESTRICCIONES
    // =========================================================================

    /**
     * Verifica recursivamente que no haya superposicion entre las cabezas de
     * serpientes y las bases de escaleras en una lista de serpientes dada.
     *
     * @param nodoCabeza Nodo actual de la lista de serpientes.
     * @param escaleras  Lista de escaleras con la que comparar.
     * @return true si no hay superposicion, false si existe conflicto.
     */
    public boolean verificarSinSuperposicion(
            co.edu.unbosque.utils.structure.Node<SerpienteDTO> nodoCabeza,
            MyLinkedList<EscaleraDTO> escaleras) {
        if (nodoCabeza == null) {
            return true;
        }
        int cabeza = nodoCabeza.getInfo().getPosicionCabeza();
        if (esCasillaBaseDeEscalera(cabeza, escaleras.getFirst())) {
            return false;
        }
        return verificarSinSuperposicion(nodoCabeza.getNext(), escaleras);
    }

    /**
     * Verifica recursivamente si una posicion coincide con la base de alguna escalera.
     *
     * @param posicion   Posicion a verificar.
     * @param nodoActual Nodo actual de la lista de escaleras.
     * @return true si la posicion es base de alguna escalera, false en caso contrario.
     */
    private boolean esCasillaBaseDeEscalera(
            int posicion,
            co.edu.unbosque.utils.structure.Node<EscaleraDTO> nodoActual) {
        if (nodoActual == null) {
            return false;
        }
        if (nodoActual.getInfo().getPosicionBase() == posicion) {
            return true;
        }
        return esCasillaBaseDeEscalera(posicion, nodoActual.getNext());
    }

    /**
     * Verifica recursivamente que todas las casillas del arreglo de ocupacion
     * cumplan que las casillas 1 y 100 no tengan elementos especiales.
     * (Esto ya se garantiza en la generacion, este metodo es de auditoria.)
     *
     * @param lista      Lista de serpientes generadas.
     * @param nodoActual Nodo actual en la recursion.
     * @return true si ninguna serpiente esta en casilla 1 o 100.
     */
    public boolean verificarCasillasProhibidas(
            co.edu.unbosque.utils.structure.Node<SerpienteDTO> nodoActual) {
        if (nodoActual == null) {
            return true;
        }
        int cabeza = nodoActual.getInfo().getPosicionCabeza();
        int cola = nodoActual.getInfo().getPosicionCola();
        if (cabeza == 1 || cabeza == 100 || cola == 1 || cola == 100) {
            return false;
        }
        return verificarCasillasProhibidas(nodoActual.getNext());
    }

    /**
     * Resetea el arreglo de casillas ocupadas para permitir una nueva generacion.
     * Solo deja bloqueadas las casillas 1 y 100.
     */
    public void resetearOcupacion() {
        resetearRecursivo(0);
    }

    /**
     * Metodo recursivo que recorre el arreglo de ocupacion y lo limpia.
     *
     * @param indice Indice actual del arreglo.
     */
    private void resetearRecursivo(int indice) {
        if (indice >= casillasOcupadas.length) {
            return;
        }
        casillasOcupadas[indice] = (indice == 1 || indice == 100);
        resetearRecursivo(indice + 1);
    }

    /**
     * Expone el estado de ocupacion de una casilla especifica.
     *
     * @param casilla Numero de casilla (1 a 100).
     * @return true si la casilla ya esta ocupada por algun elemento.
     */
    public boolean estaOcupada(int casilla) {
        if (casilla < 1 || casilla > 100) {
            return true;
        }
        return casillasOcupadas[casilla];
    }
}
