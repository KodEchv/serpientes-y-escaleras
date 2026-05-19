package co.edu.unbosque.utils.structure;
public class DNode<E> {
	private DNode<E> next;
	private DNode<E> previous;
	private E info;

	public DNode(DNode<E> next, E info) {
		this.next = next;
		this.info = info;
	}

	public DNode(E elem) {
		this(null, elem);
	}

	public DNode() {
		// TODO Auto-generated constructor stub
	}

	public DNode<E> getNext() {
		return next;
	}

	public void setNext(DNode<E> next) {
		this.next = next;
	}

	public DNode<E> getPrevious() {
		return previous;
	}

	public void setPrevious(DNode<E> previous) {
		this.previous = previous;
	}

	public E getInfo() {
		return info;
	}

	public void setInfo(E info) {
		this.info = info;
	}
	
	public String toString() {
		if (info != null) {
			return info.toString();
		} else {
			return null;
		}
	}
	
	
}