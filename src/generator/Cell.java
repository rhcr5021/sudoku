package generator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cell
{
	private int val;
	private boolean flag;
	private  Lock l = new ReentrantLock();
	
	public Cell(int val, boolean flag) {
		super();
		this.val = val;
		this.flag = flag;
	}

	public void lock()
	{
		l.lock();
	}
	
	public boolean tryLock()
	{
		return l.tryLock();
	}
	
	public void unlock()
	{
		l.unlock();
	}
	
	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
