package co.edu.unbosque.utils.structure;




public class DepthFirstSearch extends AbstractSearch {

	private StackImpl<Vertex<?>> stackOfNodes = new StackImpl<Vertex<?>>();
	private MyLinkedList<Vertex<?>> visitedNodes = new MyLinkedList<Vertex<?>>();

	public DepthFirstSearch(Vertex<?> sourceVertex, Vertex<?> destinationVertex) {
		super(sourceVertex, destinationVertex);
	}

	@Override
	public boolean runSearch() {

		if (this.sourceVertex.equals(destinationVertex)) {
			System.out.println("Nodo destino encontrado a 0 de profundidad");
			System.out.println(sourceVertex.getInfo());
		}

		System.out.println("Ruta a seguir para ubicar el nodo");
		return dfsRecursive(this.sourceVertex, visitedNodes);

	}

	private boolean dfsRecursive(Vertex<?> current, MyLinkedList<Vertex<?>> visited) {
		if (current == null) return false;
		if (current.equals(destinationVertex)) {
			System.out.println(current.getInfo());
			System.out.println("nodo buscado encontrado\n");
			return true;
		}
		if (visited.get(current) != null) {
			return false;
		}
		System.out.print(current.getInfo() + " -> ");
		visited.addLast(current);
		MyLinkedList<Edge> adyacents = current.getAdyacentEdges();
		Node<Edge> edgeNode = adyacents.getFirst();
		return processAdjacentsDFS(edgeNode, visited);
	}

	private boolean processAdjacentsDFS(Node<Edge> edgeNode, MyLinkedList<Vertex<?>> visited) {
		if (edgeNode == null) return false;
		Vertex<?> nextVertex = edgeNode.getInfo().getDestination();
		if (visited.get(nextVertex) == null) {
			if (dfsRecursive(nextVertex, visited)) return true;
		}
		return processAdjacentsDFS(edgeNode.getNext(), visited);
	}

	public StackImpl<Vertex<?>> getStackOfNodes() {
		return stackOfNodes;
	}

	public void setStackOfNodes(StackImpl<Vertex<?>> stackOfNodes) {
		this.stackOfNodes = stackOfNodes;
	}

	public MyLinkedList<Vertex<?>> getVisitedNodes() {
		return visitedNodes;
	}

	public void setVisitedNodes(MyLinkedList<Vertex<?>> visitedNodes) {
		this.visitedNodes = visitedNodes;
	}

}
