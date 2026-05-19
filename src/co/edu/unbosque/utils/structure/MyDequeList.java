package co.edu.unbosque.utils.structure;

public class MyDequeList<E> extends MyDoubleLinkedList<E> implements Dqueue<E> {

	private DNode<E> tail;
	private int size;

	public MyDequeList() {
		head = new DNode<E>();
		tail = new DNode<E>();
		head.setNext(tail);
		tail.setPrevious(head);
		size = 0;
	}

	@Override
	public void insertFirst(E info) {
		DNode<E> h = this.head;
		DNode<E> node = new DNode<E>();
		node.setInfo(info);
		node.setNext(h);
		h.setPrevious(node);
		head = node;
		if (size == 0)
			tail = node;
		size++;

	}

	@Override
	public void insertLast(E info) {
		DNode<E> t = tail;
		DNode<E> node = new DNode<E>();
		node.setInfo(info);
		node.setPrevious(t);
		t.setNext(node);
		tail = node;
		if (size == 0)
			head = node;
		size++;
	}

	@Override
	public E removeFirst() {
		if (head == null)
			return null;
		E val = head.getInfo();
		head = head.getNext();
		size--;
		return val;
	}

	@Override
	public E removeLast() {
		if (tail == null)
			return null;
		E val = tail.getInfo();
		tail = tail.getPrevious();
		size--;
		return val;
	}

	@Override
	public int size() {
		return size;
	}

	public DNode<E> getHead() {
		return head;
	}

	public void setHead(DNode<E> head) {
		this.head = head;
	}

	public DNode<E> getTail() {
		return tail;
	}

	public void setTail(DNode<E> tail) {
		this.tail = tail;
	}

	public DNode<E> getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(DNode<E> currentPosition) {
		this.currentPosition = currentPosition;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "head [" + toStringRecursive(head, 0) + "] tail";
	}

	private String toStringRecursive(DNode<E> node, int index) {
		if (node == null || index >= size) {
			return "";
		}
		String current = String.valueOf(node.getInfo());
		if (node == tail || index == size - 1) {
			return current;
		}
		return current + "-" + toStringRecursive(node.getNext(), index + 1);
	}

}