package co.edu.unbosque.utils.structure;

//Programa java que encuentra si un grafo es 
//bipartito o no

import java.util.*;

public class BipartiteGraphMatcher {

  final static int V = 4;

  static boolean isBipartite(int G[][], int src) {
      /*Crear una matriz de colores para almacenar los 
      colores asignados a todas las verificaciones. 
      El número de vértice se utiliza como índice en 
      esta matriz. El valor '-1' de colorArr [i] se 
      usa para indicar que no se asigna ningún color 
      al vértice 'i'. El valor 1 se utiliza para indicar 
      que el primer color está asignado y el valor 0 
      indica que el segundo color está asignado.*/
      int colorArr[] = new int[V];
      for (int i = 0; i < V; ++i) {
          colorArr[i] = -1;
      }
      //Asigna primer color al origen
      colorArr[src] = 1;
      //Crea una cola de número de vertices
      // y encola el vertice origen 
      LinkedList<Integer> q = new LinkedList<>();
      q.add(src);
      while (!q.isEmpty()) {
          // Descola un vertice de la cola
          int u = q.poll();
          //Retorna falso si hay un autociclo
          if (G[u][u] == 1) {
              return false;
          }
          //Encuentra todos los vertoces adyacentes sin color 
          for (int v = 0; v < V; ++v) {
              // Un camino de u a v existe
              // y destino v no esta coloreado
              if (G[u][v] == 1 && colorArr[v] == -1) {
                  //Asigna color alternativo para esta adyacencia
                  colorArr[v] = 1 - colorArr[u];
                  q.add(v);
              } /*Un camino de u a v existe y el destino 
              esta del mismo color que u*/ 
              else if (G[u][v] == 1 && colorArr[v] == colorArr[u]) {
                  return false;
              }
          }
      }
      // Si llegamos aqui, todos los vertoces adyacentes pueden
      // ser coloreados con color alternativo
      return true;
  }

  public static void main(String[] args) {
      int G[][] = {{0, 1, 0, 1},
      {1, 0, 1, 0},
      {0, 1, 0, 1},
      {1, 0, 1, 0}
      };
      if (isBipartite(G, 0)) {
          System.out.println("Si");
      } else {
          System.out.println("No");
      }
  }
}

