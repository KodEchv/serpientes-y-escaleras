package co.edu.unbosque.utils.structure;

public interface Queue<E> {

	public void enqueue(E info);

	public E dequeue();

	int size();
}