package co.edu.unbosque.utils.structure;

public class QueueImpl<E> implements Queue<E> {
	private MyDequeList<E> data;

	public QueueImpl() {
		data = new MyDequeList<E>();
	}

	@Override
	public void enqueue(E info) {
		data.insertFirst(info);
	}

	@Override
	public E dequeue() {
		return data.removeLast();
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public String toString() {
		return data.toString();
	}
}
