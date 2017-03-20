package backtracking;

import java.io.IOException;

import generator.Sudoku;

public class Backtracking extends Sudoku {
	int num_threads;

	private class Backer extends Thread {
		public void run ()
		{
			
		}
	}
	
	public Backtracking(String diff, int t) throws IOException {
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
	
	public boolean checkCell(int row, int col, int guess)
	{
		int temp = puzzle[row][col].getVal();
		puzzle[row][col].setVal(guess);
		if(countErrors() > 0)
		{
			puzzle[row][col].setVal(temp);
			return false;
		}
		puzzle[row][col].setVal(temp);
		return true;
	}

}
