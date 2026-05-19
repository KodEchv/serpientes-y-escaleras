package co.edu.unbosque.utils.structure;

public class BreadthFirstSearch extends AbstractSearch {

	public BreadthFirstSearch(Vertex<?> sourceVertex, Vertex<?> destinationVertex) {
		super(sourceVertex, destinationVertex);

	}

	@Override
	public boolean runSearch() {

		if (this.sourceVertex.equals(destinationVertex)) {
			System.out.println("El objetivo fue encontrado");
			System.out.println(this.sourceVertex.getInfo());
		}

		QueueImpl<Vertex<?>> queueOfNodes = new QueueImpl<Vertex<?>>();
		MyLinkedList<Vertex<?>> visitedNodes = new MyLinkedList<Vertex<?>>();

		queueOfNodes.enqueue(this.sourceVertex);
		visitedNodes.addLast(sourceVertex);

		System.out.println("Ruta a seguir para ubicar el nodo");
		return bfsRecursive(queueOfNodes, visitedNodes);
	}



	private boolean bfsRecursive(QueueImpl<Vertex<?>> queue, MyLinkedList<Vertex<?>> visited) {
		if (queue.size() == 0) return false;
		Vertex<?> current = queue.dequeue();
		if (current.equals(this.destinationVertex)) {
			System.out.println(current.getInfo());
			System.out.println("nodo buscado encontrado\n");
			return true;
		} else {
			System.out.print(current.getInfo() + " -> ");
			MyLinkedList<Edge> adyacents = current.getAdyacentEdges();
			Node<Edge> edgeNode = adyacents.getFirst();
			enqueueAdjacentsRecursive(edgeNode, queue);
		}
		visited.addLast(current);
		return bfsRecursive(queue, visited);
	}

	private void enqueueAdjacentsRecursive(Node<Edge> edgeNode, QueueImpl<Vertex<?>> queue) {
		if (edgeNode == null) return;
		queue.enqueue(edgeNode.getInfo().getDestination());
		enqueueAdjacentsRecursive(edgeNode.getNext(), queue);
	}
	}

