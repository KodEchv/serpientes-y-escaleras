package co.edu.unbosque.utils.structure;

public class MyLinkedList<E> implements java.io.Serializable {

	private static final long serialVersionUID = 1001L;

	protected Node<E> first;

	public MyLinkedList() {
		this.first = null;
	}

	public Node<E> getFirst() {
		return this.first;
	}

	public void setFirst(Node<E> first) {
		this.first = first;
	}

	public boolean isEmpty() {
		return (this.first == null);
	}

	public void add(E info) {
		Node<E> newNode = new Node<E>(info);
		newNode.setNext(this.first);
		first = newNode;
	}

	public void insert(E info, Node<E> previous) {
		if (previous != null) {
			Node<E> newNode = new Node<E>(info);
			newNode.setNext(previous.getNext());
			previous.setNext(newNode);
		}
	}

	public void addLast(E info) {
		Node<E> lastNode = getLastNode();

		if (lastNode != null) {
			insert(info, lastNode);
		} else {
			this.first = new Node<E>(info);

		}
	}

	public E extract() {
		E data = null;
		if (this.first != null) {
			data = this.first.getInfo();
			this.first = this.first.getNext();
		}
		return data;
	}

	public E extract(Node<E> previous) {
		E data = null;
		if (previous != null && previous.getNext() != null) {
			data = previous.getNext().getInfo();
			previous.setNext(previous.getNext().getNext());
		}
		return data;
	}

	public int size() {
		return sizeRecursive(this.first);
	}

	public String print() {
		return this.toString();
	}

//informacion si se sabe que se esta buscando
	public Node<E> get(E info) {
		return getByInfoRecursive(this.first, info);
	}
//Posicion en caso de que no se sepa que se esta buscando
	public Node<E> get(int n) {
		return getByIndexRecursive(this.first, n, 0);
	}

	public Node<E> getLastNode() {
		return getLastNodeRecursive(this.first);
	}
//Busca la primera ocurrencia hasta el final 
	public int indexOf(E info) {
		if (isEmpty()) {
			return -1;
		}
		int index = indexOfRecursive(this.first, info, 0);
		return index == -1 ? size() : index;
	}

	public int numberOfOccurrences(E info) {	
		return numberOfOccurrencesRecursive(this.first, info);
	}

	public E extractLast() {
		E info = null;
		Node<E> current = this.first;
		int listSize = size();

		if (!isEmpty()) {
			if (listSize == 1) {
				info = current.getInfo();
				this.first = null;
			} else {
				Node<E> previousLastNode = get(listSize - 2);
				info = extract(previousLastNode);
			}
		}
		return info;
	}

	public String print(int position) {
		if (isEmpty()) {
			return "";
		}
		Node<E> current = get(position);
		return toStringRecursive(current);
	}

	public String toString() {
		return toStringRecursive(this.first);
	}

	private int sizeRecursive(Node<E> current) {
		if (current == null) {
			return 0;
		}
		return 1 + sizeRecursive(current.getNext());
	}

	private Node<E> getByInfoRecursive(Node<E> current, E info) {
		if (current == null) {
			return null;
		}
		if (current.getInfo().equals(info)) {
			return current;
		}
		return getByInfoRecursive(current.getNext(), info);
	}

	private Node<E> getByIndexRecursive(Node<E> current, int n, int counter) {
		if (current == null) {
			return null;
		}
		if (counter == n) {
			return current;
		}
		return getByIndexRecursive(current.getNext(), n, counter + 1);
	}

	private Node<E> getLastNodeRecursive(Node<E> current) {
		if (current == null || current.getNext() == null) {
			return current;
		}
		return getLastNodeRecursive(current.getNext());
	}

	private int indexOfRecursive(Node<E> current, E info, int index) {
		if (current == null) {
			return -1;
		}
		if (current.getInfo().equals(info)) {
			return index;
		}
		return indexOfRecursive(current.getNext(), info, index + 1);
	}

	private int numberOfOccurrencesRecursive(Node<E> current, E info) {
		if (current == null) {
			return 0;
		}
		int addition = current.getInfo().equals(info) ? 1 : 0;
		return addition + numberOfOccurrencesRecursive(current.getNext(), info);
	}

	private String toStringRecursive(Node<E> current) {
		if (current == null) {
			return "";
		}
		if (current.getNext() == null) {
			return current.getInfo().toString();
		}
		return current.getInfo().toString() + " -> " + toStringRecursive(current.getNext());
	}
}