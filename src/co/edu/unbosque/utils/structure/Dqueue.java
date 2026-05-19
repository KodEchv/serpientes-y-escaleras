package co.edu.unbosque.utils.structure;

public interface Dqueue<E> {

	

	public void insertLast(E info);

	public E removeFirst();

	public E removeLast();

	public int size();

	public void insertFirst(E info);
}