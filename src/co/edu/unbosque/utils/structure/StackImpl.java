package co.edu.unbosque.utils.structure;

import java.util.LinkedList;
import java.util.Queue;

public class StackImpl<E> implements Stack<E> {
	private MyDequeList<E> data;

	public StackImpl() {
		data = new MyDequeList<E>();
		Queue<String> q = new LinkedList<String>();
	}

	@Override
	public void push(E info) {
		data.insertFirst(info);
	}

	@Override
	public E pop() {
		return data.removeFirst();
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
