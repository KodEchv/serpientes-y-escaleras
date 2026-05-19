package co.edu.unbosque.utils.structure;
//Programa java que busca todos los caminos más cortos
//en un grafo

import java.util.ArrayList;
import java.util.Scanner;

/**
 * FloydWarshallAPSP
 *
 * Algoritmo de Floyd–Warshall para todos los pares de caminos más cortos.
 * Esta versión convierte los bucles principales a llamadas recursivas
 * y añade JavaDoc para los métodos públicos y auxiliares.
 */
public class FloydWarshallAPSP {

    /** Valor que representa infinito en la matriz de adyacencia. */
    static int INF = 9999;
    /** Número de vértices del grafo. */
    static int V = 0;
    /** Matriz de adyacencia del grafo. */
    static int[][] graph;
    /** Matriz auxiliar para reconstruir caminos. */
    static int[][] next;

    /**
     * Punto de entrada mínimo para probar el algoritmo desde stdin.
     * Formato esperado: V E seguido de E aristas (u v w).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        V = sc.nextInt();
        graph = new int[V][V];
        next = new int[V][V];
        int E = sc.nextInt();
        // Inicializa todo en infinito de forma recursiva
        initGraphRecursive(0, 0);
        // De un nodo al mismo nodo es 0
        setDiagonalRecursive(0);
        // Leer aristas
        for (int i = 0; i < E; i++) {
            graph[sc.nextInt()][sc.nextInt()] = sc.nextInt();
        }
        floydWarshall(graph);
        ArrayList<Integer> path;
        System.out.println("El camino mas corto desde 0 a 3: ");
        // Construir camino
        path = constructPath(0, 3);
        // imprimir camino
        printPath(path);
    }

    /**
     * Inicializa la matriz `graph` en `INF` usando recursividad.
     */
    private static void initGraphRecursive(int i, int j) {
        if (i >= V) return;
        if (j >= V) {
            initGraphRecursive(i + 1, 0);
            return;
        }
        graph[i][j] = INF;
        initGraphRecursive(i, j + 1);
    }

    /**
     * Establece la diagonal principal a 0 de forma recursiva.
     */
    private static void setDiagonalRecursive(int i) {
        if (i >= V) return;
        graph[i][i] = 0;
        setDiagonalRecursive(i + 1);
    }

    /**
     * Ejecuta el algoritmo de Floyd–Warshall usando llamadas recursivas
     * en lugar de bucles anidados para k, i y j.
     *
     * @param graph matriz de adyacencia de entrada
     */
    static void floydWarshall(int graph[][]) {
        int dist[][] = new int[V][V];
        // Inicializa dist y next recursivamente
        initDistNextRecursive(dist, 0, 0);
        // Ejecuta la recursión principal sobre k
        floydKRecursive(0, dist);
        // Imprime la matriz de distancias cortas
        printSolution(dist);
    }

    /**
     * Inicializa `dist` y `next` copiando `graph`.
     */
    private static void initDistNextRecursive(int[][] dist, int i, int j) {
        if (i >= V) return;
        if (j >= V) {
            initDistNextRecursive(dist, i + 1, 0);
            return;
        }
        dist[i][j] = graph[i][j];
        if (graph[i][j] == INF) {
            next[i][j] = -1;
        } else {
            next[i][j] = j;
        }
        initDistNextRecursive(dist, i, j + 1);
    }

    /**
     * Recursión sobre el índice k del algoritmo.
     */
    private static void floydKRecursive(int k, int[][] dist) {
        if (k >= V) return;
        floydIRecursive(k, 0, dist);
        floydKRecursive(k + 1, dist);
    }

    /**
     * Recursión sobre el índice i para un k dado.
     */
    private static void floydIRecursive(int k, int i, int[][] dist) {
        if (i >= V) return;
        floydJRecursive(k, i, 0, dist);
        floydIRecursive(k, i + 1, dist);
    }

    /**
     * Recursión sobre el índice j para un k e i dados.
     */
    private static void floydJRecursive(int k, int i, int j, int[][] dist) {
        if (j >= V) return;
        if (dist[i][k] != INF && dist[k][j] != INF) {
            if (dist[i][j] > dist[i][k] + dist[k][j]) {
                dist[i][j] = dist[i][k] + dist[k][j];
                next[i][j] = next[i][k];
            }
        }
        floydJRecursive(k, i, j + 1, dist);
    }

    /**
     * Construye recursivamente el camino desde u hasta v usando la matriz `next`.
     *
     * @param u nodo origen
     * @param v nodo destino
     * @return lista con la secuencia de vértices del camino (vacía si no existe)
     */
    static ArrayList<Integer> constructPath(int u, int v) {
        if (next[u][v] == -1) {
            return new ArrayList<>();
        }
        ArrayList<Integer> path = new ArrayList<>();
        constructPathRecursive(u, v, path);
        return path;
    }

    private static void constructPathRecursive(int u, int v, ArrayList<Integer> path) {
        path.add(u);
        if (u == v) return;
        constructPathRecursive(next[u][v], v, path);
    }

    /**
     * Imprime el camino de forma recursiva.
     */
    static void printPath(ArrayList<Integer> path) {
        if (path == null || path.size() == 0) {
            System.out.println("No path");
            return;
        }
        printPathRecursive(path, 0);
    }

    private static void printPathRecursive(ArrayList<Integer> path, int idx) {
        if (idx >= path.size() - 1) {
            System.out.println(path.get(path.size() - 1) + " -> ");
            return;
        }
        System.out.print(path.get(idx) + " -> ");
        printPathRecursive(path, idx + 1);
    }

    /**
     * Imprime la matriz de soluciones recursivamente.
     */
    static void printSolution(int dist[][]) {
        System.out.println("La siguiente matriz muestra las distancias "
                + "más cortas entre cada par de vertices");
        printSolutionRowsRecursive(dist, 0, 0);
    }

    private static void printSolutionRowsRecursive(int[][] dist, int i, int j) {
        if (i >= V) return;
        if (j >= V) {
            System.out.println();
            printSolutionRowsRecursive(dist, i + 1, 0);
            return;
        }
        if (dist[i][j] == FloydWarshallAPSP.INF) {
            System.out.print("INF ");
        } else {
            System.out.print(dist[i][j] + " ");
        }
        printSolutionRowsRecursive(dist, i, j + 1);
    }

}

