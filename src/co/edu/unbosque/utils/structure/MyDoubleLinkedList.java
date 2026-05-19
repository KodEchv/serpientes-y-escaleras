package co.edu.unbosque.utils.structure;

public class MyDoubleLinkedList<E>{

	protected DNode<E> head;
	protected DNode<E> currentPosition;


	public MyDoubleLinkedList() {

	}

	/**
	 * Increments current position -numPositions-
	 */
	public void forward(int numPositions) {
		if (numPositions > 0 && head != null) {
			int positionsForward = numPositions;
			if (currentPosition == null) {
				currentPosition = head;
				positionsForward--;
			}
			forwardRecursive(positionsForward);
		}
	}
	public void back(int numPositions) {
		if (numPositions <= 0 || head == null || currentPosition == null)
			return;
		backRecursive(numPositions);
	}

	
	public void insert(E data) {
		DNode<E> node = new DNode<E>(data);

		if (currentPosition == null) {
			node.setNext(head);       
			if (head != null) {
				head.setPrevious(node);
			}
			head = node;
		} else {
			node.setNext(currentPosition.getNext());
			node.setPrevious(currentPosition);
			if (currentPosition.getNext() != null) {
				currentPosition.getNext().setPrevious(node);
			}
			currentPosition.setNext(node);
		}

		
		currentPosition = node;
	}

	public E extract() {
		E info = null;

		if (currentPosition != null) {
			info = currentPosition.getInfo();

			if (head == currentPosition) {
				head = currentPosition.getNext();
			} else {
				currentPosition.getPrevious().setNext(currentPosition.getNext());
			}

			if (currentPosition.getNext() != null) {
				currentPosition.getNext().setPrevious(currentPosition.getPrevious());
			}

			currentPosition = currentPosition.getNext();
		}
		return info;
	}

	public String toString() {
		return toStringRecursive(head);
	}

	

	public DNode<E> getHead() {
		return head;
	}

	public void setHead(DNode<E> head) {
		this.head = head;
	}

	public DNode<E> getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(DNode<E> currentPosition) {
		this.currentPosition = currentPosition;
	}

	private void forwardRecursive(int positionsForward) {
		if (currentPosition == null || currentPosition.getNext() == null || positionsForward <= 0) {
			return;
		}
		currentPosition = currentPosition.getNext();
		forwardRecursive(positionsForward - 1);
	}

	private void backRecursive(int positionsBack) {
		if (currentPosition == null || positionsBack <= 0) {
			return;
		}
		currentPosition = currentPosition.getPrevious();
		backRecursive(positionsBack - 1);
	}

	private String toStringRecursive(DNode<E> node) {
		if (node == null) {
			return "";
		}
		if (node.getNext() == null) {
			return String.valueOf(node.getInfo());
		}
		return String.valueOf(node.getInfo()) + " <-> " + toStringRecursive(node.getNext());
	}


}