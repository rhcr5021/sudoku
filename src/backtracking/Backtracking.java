package backtracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import generator.Cell;
import generator.Sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Backtracking extends Sudoku {
	int num_threads;
	private ExecutorService service = Executors.newCachedThreadPool();
	private List<TryNew> first_triers = new ArrayList<TryNew>();
	
	private class TryNew implements Callable<Cell[][]>
	{
		int t;
		int r; int c;
		Cell[][] puz;
		public TryNew(Cell[][] puzzle, int i, int row, int col)
		{
			t = i;
			c = col;
			r = row;
			puz = puzzle;
		}
		public Cell[][] call() throws Exception {
			puz[c][r].setVal(t);
			int[] spot = findEmptyCell();
			int row = spot[0], col = spot[1];
			for (int g = 1; g <= 9; g++)
			{
				service.submit(new TryNew(puz,g,row,col));
			}
			System.out.println(countErrors(puz));
			return puz;
		}
	}
	private class BackerTask {
		ExecutorService exec = Executors.newCachedThreadPool();
		boolean add(Cell[][] puz)
		{
			Future<?> future = exec.submit(new AddTask(puz));
			return false;
			
		}
		
		public class AddTask implements Runnable {
			Cell[][] puz;
			public AddTask(Cell[][] puzzle)
			{
				puz = puzzle;
			}
			
			public void run()
			{
				
			}
		}
	}
	
	
	public Backtracking(String diff, int t,boolean big) throws IOException {
		super(diff);
		num_threads = t;
		// TODO Auto-generated constructor stub
	}
	
	public Backtracking(String diff, int n, int t) throws IOException {
		super(diff,n);
		num_threads = t;
	}
	
	public int[] findEmptyCell()
	{
		int[] empty = new int[2];
		for(int i = 0; i < 9;i++)
		{
			for(int j = 0; j < 9; j++)
			{
				if(puzzle[i][j].getVal() == 0)
				{
					empty[0] = i;
					empty[1] = j;
					return empty;
				}
			}
		}
		return null;
	}
	public int[] findEmptyCell(Cell[][] puz)
	{
		int[] empty = new int[2];
		for(int i = 0; i < 9;i++)
		{
			for(int j = 0; j < 9; j++)
			{
				if(puz[i][j].getVal() == 0)
				{
					empty[0] = i;
					empty[1] = j;
					return empty;
				}
			}
		}
		return null;
	}
	
	public boolean solve()
	{
		int[] spot = findEmptyCell();
		if (spot == null)
		{
			return true;
		}
		int row = spot[0], col = spot[1];
		for (int g = 9; g > 0; g--)
		{
			if(checkCell(row, col, g))
			{
				int temp = puzzle[row][col].getVal();
				puzzle[row][col].setVal(g);
				if(solve())
				{
					return true;
				}
				puzzle[row][col].setVal(temp);
			}
		}
		return false;
	}
	
	public boolean solveConcurrent()
	{
		int[] spot = findEmptyCell();
		int row = spot[0], col = spot[1];
		for (int g = 0; g < 9; g++)
		{
			first_triers.add(new TryNew(puzzle,g,row,col));
			service.submit(first_triers.get(g));
		}
		try {
			List<Future<Cell[][]>> f = service.invokeAll(first_triers);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkCell(int row, int col, int guess)
	{
		int temp = puzzle[row][col].getVal();
		puzzle[row][col].setVal(guess);
		if(isErrors(puzzle))
		{
			puzzle[row][col].setVal(temp);
			return false;
		}
		puzzle[row][col].setVal(temp);
		return true;
		
	}
	
	public int countErrors(Cell[][] mypuzz)
	{
		//count errors for all rows, columns, and boxes
		int row = 0, col = 0, box = 0;
		for (int i = 0; i < 9; i++)
		{
			row += checkSection(getRow(i,mypuzz));
			col += checkSection(getCol(i, mypuzz));
			box += checkSection(getBox(i, mypuzz));
		}
		return (row+box+col);
	}
	
	protected int[] getBox(int i, Cell[][] mypuzz) {
		//use index i to get col and row indexes for the box
		/*  _____
		 * |0|1|2|
		 * |3|4|5|
		 * |6|7|8|
		 */
		int row, col;
		switch(i)
		{
		case 0: row = 0; col = 0; break; 
		case 1: row = 0; col = 3; break;
		case 2: row = 0; col = 6; break;
		case 3: row = 3; col = 0; break;
		case 4: row = 3; col = 3; break;
		case 5: row = 3; col = 6; break;
		case 6: row = 6; col = 0; break;
		case 7: row = 6; col = 3; break;
		case 8: row = 6; col = 6; break;
		default: System.out.println("invalid index"); return null;
		}
		//get row and col with case statement, external method, or expression
		int box[] = new int[9];
		int m = 0;
		for (int j = row; j < row+3;j++)
		{
			for (int k = col; k < col+3;k++)
			{
				box[m] = mypuzz[j][k].getVal();
				m++;
				
			}
		}
		return box;
	}
	
	protected int [] getCol(int i, Cell[][] mypuzz) {
		int [] col = new int[9];
		for (int j = 0; j < 9; j++)
		{
			col[j] = mypuzz[j][i].getVal();
		}
		return col;
	}
	
	protected int[] getRow(int i, Cell[][] mypuzz) {
		int [] row = new int[9];
		for (int j = 0; j < 9; j++)
		{
			row[j] = mypuzz[i][j].getVal();
		}
		return row;
	}
	
	protected boolean isErrors(Cell[][] puz) {
		for (int i = 0; i < 9; i++)
		{
			if(isErrorInSection(getRow(i, puz)))
			{
				return true;
			}
			else if(isErrorInSection(getCol(i, puz)))
			{
				return true;
			}
			else if(isErrorInSection(getBox(i, puz)))
			{
				return true;
			}
		}
		return false;
	}
	
}
