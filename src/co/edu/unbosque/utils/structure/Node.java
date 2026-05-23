package co.edu.unbosque.utils.structure;
public class Node<E> implements java.io.Serializable {

	private static final long serialVersionUID = 1002L;

	private E info;
	private Node<E> next;

	public Node(E info, Node<E> next) {
		this.info = info;
		this.next = next;
	}

	public Node(E info) {
		this(info, null);
	}

	public Node() {
		this(null, null);
	}

	public E getInfo() {
		return this.info;
	}

	public Node<E> getNext() {
		return this.next;
	}

	public void setInfo(E info) {
		this.info = info;
	}

	public void setNext(Node<E> next) {
		this.next = next;
	}

	public String toString() {
		if (info != null) {
			return info.toString();
		} else {
			return null;
		}
	}
}