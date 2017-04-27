package generator;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedEQueue {
	//lock for enquerer and dequerer
	private ReentrantLock enqLock, deqLock;
	//atomic integer to keep track of size
	public AtomicInteger size;
	//nodes for the head and tail of the queue
	private Node  head, tail;
	//capacity entered through constructer
	private int capacity;
	//boolean to set first element
	public AtomicBoolean init;
	
	protected class Node  {
		//no value needed for this application
		public Node  next;
		//set next to null to make a node
		public Node()
		{
			next = null;
		}
	}
	
	public BoundedEQueue(int _capacity) {
		//set the capacitt
		capacity = _capacity;
		//make the head and tail pointers
		head = new Node ();
		tail = head;
		//set size to zero
		size = new AtomicInteger(0);
		//set init to true
		init = new AtomicBoolean(true);
		//init the locks
		enqLock = new ReentrantLock();
		deqLock = new ReentrantLock();
	}
	
	public void enq() {
		//lock to stop other enqueuers
		enqLock.lock();
		try {
			//if you are not the first enqueuer and the size is not capacity or zero
			if ((size.get() == capacity || size.get() == 0 )&& !init.get())
			{
				return;
			}
			//if you are the first set the init boolean
			if(init.get())
			{
				init.set(false);
			}
			//make a new node
			Node  e = new Node();
			//add it to the end of the queue
			tail.next = (Node) e;
			//put the tail pointer at the end
			tail = tail.next;
			//incriment size
			size.getAndIncrement();
		}
		finally
		{
			//unlock at the end
			enqLock.unlock();
		}
	}
	
	public void deq()
	{
		//lock the dequeue lock
		deqLock.lock();
		try
		{
			//return if queue is full or empty
			if (size.get() == 0 || size.get() == capacity)
			{
				return;
			}
			//move the front of the queue
			head = head.next;
			//decriment the size
			size.getAndDecrement();
		}
		finally
		{
			//make sure to unlock
			deqLock.unlock();
		}
	}
}

