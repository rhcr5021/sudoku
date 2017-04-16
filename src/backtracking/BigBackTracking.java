package backtracking;

import java.io.FileNotFoundException;
import java.io.IOException;

import generator.BigSudoku;

public class BigBackTracking extends BigSudoku{

	public BigBackTracking(String filename) throws IOException {
		super(filename);
		// TODO Auto-generated constructor stub
	}
	
	public boolean solveBig() throws FileNotFoundException {
		int[] spot = findEmptyCellBig();
		if (spot == null)
		{
			return true;
		}
		int row = spot[0], col = spot[1];
		for (int g = 81; g > 0; g--)
		{
			if(checkCellBig(row, col, g))
			{
				System.out.println(row + "," + col + "," + g);
				int temp = big[row][col].getVal();
				big[row][col].setVal(g);
				if(solveBig())
				{
					return true;
				}
				big[row][col].setVal(temp);
			}
		}
		return false;
	}

	private int[] findEmptyCellBig() {
		int[] empty = new int[2];
		for(int i = 0; i < 81;i++)
		{
			for(int j = 0; j < 81; j++)
			{
				if(big[i][j].getVal() == 0)
				{
					empty[0] = i;
					empty[1] = j;
					return empty;
				}
			}
		}
		return null;
	}
	
	public boolean checkCellBig(int row, int col, int guess)
	{
		int temp = big[row][col].getVal();
		big[row][col].setVal(guess);
		if(isErrorsBig())
		{
			big[row][col].setVal(temp);
			return false;
		}
		big[row][col].setVal(temp);
		return true;
	}
	
}
