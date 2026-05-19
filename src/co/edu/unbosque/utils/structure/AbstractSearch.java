package co.edu.unbosque.utils.structure;

public abstract class AbstractSearch {
	protected Vertex<?> sourceVertex;
	protected Vertex<?> destinationVertex;

	public AbstractSearch(Vertex<?> sourceVertex, Vertex<?> destinationVertex) {
		super();
		this.sourceVertex = sourceVertex;
		this.destinationVertex = destinationVertex;
	}

	public Vertex<?> getSourceVertex() {
		return sourceVertex;
	}

	public void setSourceVertex(Vertex<?> sourceVertex) {
		this.sourceVertex = sourceVertex;
	}

	public Vertex<?> getDestinationVertex() {
		return destinationVertex;
	}

	public void setDestinationVertex(Vertex<?> destinationVertex) {
		this.destinationVertex = destinationVertex;
	}

	public abstract boolean runSearch();

}
