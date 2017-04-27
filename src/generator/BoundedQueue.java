package generator;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class BoundedQueue<T> {
	
	private ReentrantLock enqLock, deqLock;
	public AtomicInteger size;
	private Node<T> head, tail;
	private int capacity;
	public AtomicBoolean init;
	
	protected class Node<T> {
		public T value;
		public Node<T> next;
		public Node(T x)
		{
			value = x;
			next = null;
		}
	}
	
	public BoundedQueue(int _capacity) {
		capacity = _capacity;
		head = new Node<T>(null);
		tail = head;
		size = new AtomicInteger(0);
		init = new AtomicBoolean(true);
		enqLock = new ReentrantLock();
		deqLock = new ReentrantLock();
	}
	
	public void enq(T x) {
		enqLock.lock();
		try {
			if ((size.get() == capacity || size.get() == 0 )&& !init.get())
			{
				return;
			}
			if(init.get())
			{
				init.set(false);
			}
			Node<T> e = new Node<T>(x);
			tail.next = (Node<T>) e;
			tail = tail.next;
			size.getAndIncrement();
		}
		finally
		{
			enqLock.unlock();
		}
	}
	
	public T deq()
	{
		T result;
		//lock the dequeue lock
		deqLock.lock();
		try
		{
			//return if queue is full or empty
			if (size.get() == 0 || size.get() == capacity)
			{
				return null;
			}
			//get the value 
			result = head.next.value;
			head = head.next;
			size.getAndDecrement();
		}
		finally
		{
			deqLock.unlock();
		}
		return result;
	}
	
	T peek()
	{
		if (head.next == null)
		{
			return null;
		}
		return head.next.value;
	}

	public boolean isEmpty() {
		if(size.get() == 0)
		{
			return true;
		}
		return false;
	}
}
