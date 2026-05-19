package co.edu.unbosque.utils.structure;


import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * PrimMST
 *
 * Implementación de Prim para construir un árbol de expansión mínima (MST).
 * Esta versión convierte bucles de inicialización y recorridos a llamadas
 * recursivas y añade documentación básica.
 */
public class PrimMST {

  public static void main(String[] args) {
      int V = 9;
      Graph graph = new Graph(V);
      addEdge(graph, 0, 1, 4);
      addEdge(graph, 0, 7, 8);
      addEdge(graph, 1, 2, 8);
      addEdge(graph, 1, 7, 11);
      addEdge(graph, 2, 3, 7);
      addEdge(graph, 2, 8, 2);
      addEdge(graph, 2, 5, 4);
      addEdge(graph, 3, 4, 9);
      addEdge(graph, 3, 5, 14);
      addEdge(graph, 4, 5, 10);
      addEdge(graph, 5, 6, 2);
      addEdge(graph, 6, 7, 1);
      addEdge(graph, 6, 8, 6);
      addEdge(graph, 7, 8, 7);
      prims_mst(graph);
  }

  static class node1 {
      int dest;
      int weight;
      node1(int a, int b) {
          dest = a;
          weight = b;
      }
  }

  static class Graph {
      int V;
      LinkedList<node1>[] adj;
      Graph(int e) {
          V = e;
          adj = new LinkedList[V];
          initAdjRecursive(0);
      }

      private void initAdjRecursive(int idx) {
          if (idx >= V) return;
          adj[idx] = new LinkedList<>();
          initAdjRecursive(idx + 1);
      }
  }

  static class node {
      int vertex;
      int key;
  }


  static class comparator implements Comparator<node> {

      @Override
      public int compare(node node0, node node1) {
          return node0.key - node1.key;
      }
  }
  static void addEdge(Graph graph, int src, int dest, int weight) {
      node1 node0 = new node1(dest, weight);
      node1 node = new node1(src, weight);
      graph.adj[src].addLast(node0);
      graph.adj[dest].addLast(node);
  }

  // Buscar MST
  static void prims_mst(Graph graph) {
      Boolean[] mstset = new Boolean[graph.V];
      node[] e = new node[graph.V];
      int[] parent = new int[graph.V];
      initNodesRecursive(e, 0, graph.V);
      initValuesRecursive(mstset, e, parent, 0, graph.V);
      // incluir el vertice inicial en el MST
      mstset[0] = true;
      e[0].key = 0;
      PriorityQueue<node> queue = new PriorityQueue<>(graph.V, new comparator());
      addQueueRecursive(queue, e, 0, graph.V);
      primProcessQueueRecursive(queue, mstset, e, parent, graph);
      // Imprimir el par de vertices del mst 
      printMSTRecursive(parent, 1, graph.V);
  }

  private static void initNodesRecursive(node[] e, int idx, int size) {
      if (idx >= size) return;
      e[idx] = new node();
      initNodesRecursive(e, idx + 1, size);
  }

  private static void initValuesRecursive(Boolean[] mstset, node[] e, int[] parent, int idx, int size) {
      if (idx >= size) return;
      mstset[idx] = false;
      e[idx].key = Integer.MAX_VALUE;
      e[idx].vertex = idx;
      parent[idx] = -1;
      initValuesRecursive(mstset, e, parent, idx + 1, size);
  }

  private static void addQueueRecursive(PriorityQueue<node> queue, node[] e, int idx, int size) {
      if (idx >= size) return;
      queue.add(e[idx]);
      addQueueRecursive(queue, e, idx + 1, size);
  }

  private static void primProcessQueueRecursive(PriorityQueue<node> queue, Boolean[] mstset, node[] e, int[] parent, Graph graph) {
      if (queue.isEmpty()) return;
      node node0 = queue.poll();
      mstset[node0.vertex] = true;
      processAdjacencyListRecursive(graph.adj[node0.vertex], 0, queue, mstset, e, parent, node0.vertex);
      primProcessQueueRecursive(queue, mstset, e, parent, graph);
  }

  private static void processAdjacencyListRecursive(LinkedList<node1> list, int idx, PriorityQueue<node> queue, Boolean[] mstset, node[] e, int[] parent, int currentVertex) {
      if (idx >= list.size()) return;
      node1 iterator = list.get(idx);
      if (!mstset[iterator.dest]) {
          if (e[iterator.dest].key > iterator.weight) {
              queue.remove(e[iterator.dest]);
              e[iterator.dest].key = iterator.weight;
              queue.add(e[iterator.dest]);
              parent[iterator.dest] = currentVertex;
          }
      }
      processAdjacencyListRecursive(list, idx + 1, queue, mstset, e, parent, currentVertex);
  }

  private static void printMSTRecursive(int[] parent, int idx, int size) {
      if (idx >= size) return;
      System.out.println(parent[idx] + " " + "-" + " " + idx);
      printMSTRecursive(parent, idx + 1, size);
  }
}


