package backtracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import generator.Sudoku;

public class Backtracking extends Sudoku {
	int num_threads;
	Cell[][] big;

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
				System.out.println(row + "," + col);
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
		if(countErrorsBig() > 0)
		{
			big[row][col].setVal(temp);
			return false;
		}
		big[row][col].setVal(temp);
		return true;
	}

	private int countErrorsBig() {
		int row = 0, col = 0, box = 0;
		for (int i = 0; i < 81; i++)
		{
			row += checkSectionBig(getRowBig(i));
			col += checkSectionBig(getColBig(i));
			box += checkSectionBig(getBoxBig(i));
		}
		return (row+box+col);
		// TODO Auto-generated method stub
	}
	
	private int [] getColBig(int i) {
		int [] col = new int[81];
		for (int j = 0; j < 81; j++)
		{
			col[j] = big[j][i].getVal();
		}
		return col;
	}
	
	private int[] getRowBig(int i) {
		int [] row = new int[81];
		for (int j = 0; j < 81; j++)
		{
			row[j] = big[i][j].getVal();
		}
		return row;
	}

	private int checkSectionBig(int row[]) {
		int count = 0;
		//check for duplicates
		for (int i = 0; i < 81; i++)
		{
			for (int j = 0; j < 81; j++)
			{
				//only count once per pair and don't count zeros
				if(i > j && row[i] == row[j] && row[i] != 0)
				{
					count++;
				}
			}
		}
		return count;
	}
	
	private int[] getBoxBig(int i) {
		//use index i to get col and row indexes for the box
		/* =======
		 * |0|1|2|
		 * |3|4|5|
		 * |6|7|8|
		 * =======
		 */
		int row, col;
		row = (i/9);
		row *= 9;
		col = ((i)%9);
		col*= 9;
//		switch(i)
//		{
//		//FIRST 9X9
//		case 0: row = 0; col = 0; break; 
//		case 1: row = 0; col = 9; break;
//		case 2: row = 0; col = 18; break;
//		case 3: row = 9; col = 0; break;
//		case 4: row = 9; col = 9; break;
//		case 5: row = 9; col = 18; break;
//		case 6: row = 18; col = 0; break;
//		case 7: row = 18; col = 9; break;
//		case 8: row = 18; col = 18; break;
//		//Second 9X9
//		case 9: row = 0; col = 9; break; 
//		case 10: row = 0; col = 12; break;
//		case 11: row = 0; col = 15; break;
//		case 12: row = 3; col = 9; break;
//		case 13: row = 3; col = 12; break;
//		case 14: row = 3; col = 15; break;
//		case 15: row = 6; col = 9; break;
//		case 16: row = 6; col = 12; break;
//		case 17: row = 6; col = 15; break;
//		//Third 9X9
//		case 18: row = 0; col = 18; break; 
//		case 19: row = 0; col = 21; break;
//		case 20: row = 0; col = 24; break;
//		case 21: row = 3; col = ; break;
//		case 22: row = 3; col = 3; break;
//		case 23: row = 3; col = 6; break;
//		case 24: row = 6; col = 0; break;
//		case 25: row = 6; col = 3; break;
//		case 26: row = 6; col = 6; break;
//		//fourth 9X9
//		case 27: row = 0; col = 0; break; 
//		case 28: row = 0; col = 3; break;
//		case 29: row = 0; col = 6; break;
//		case 30: row = 3; col = 0; break;
//		case 31: row = 3; col = 3; break;
//		case 32: row = 3; col = 6; break;
//		case 33: row = 6; col = 0; break;
//		case 34: row = 6; col = 3; break;
//		case 35: row = 6; col = 6; break;
//		//5th 9X9
//		case 36: row = 0; col = 0; break; 
//		case 37: row = 0; col = 3; break;
//		case 38: row = 0; col = 6; break;
//		case 39: row = 3; col = 0; break;
//		case 40: row = 3; col = 3; break;
//		case 41: row = 3; col = 6; break;
//		case 42: row = 6; col = 0; break;
//		case 43: row = 6; col = 3; break;
//		case 44: row = 6; col = 6; break;
//		//6th 9X9
//		case 45: row = 0; col = 0; break; 
//		case 46: row = 0; col = 3; break;
//		case 47: row = 0; col = 6; break;
//		case 48: row = 3; col = 0; break;
//		case 49: row = 3; col = 3; break;
//		case 50: row = 3; col = 6; break;
//		case 51: row = 6; col = 0; break;
//		case 52: row = 6; col = 3; break;
//		case 53: row = 6; col = 6; break;
//		//7th 9X9
//		case 54: row = 0; col = 0; break; 
//		case 55: row = 0; col = 3; break;
//		case 56: row = 0; col = 6; break;
//		case 57: row = 3; col = 0; break;
//		case 58: row = 3; col = 3; break;
//		case 59: row = 3; col = 6; break;
//		case 60: row = 6; col = 0; break;
//		case 61: row = 6; col = 3; break;
//		case 62: row = 6; col = 6; break;
//		//8th 9X9
//		case 63: row = 0; col = 0; break; 
//		case 64: row = 0; col = 3; break;
//		case 65: row = 0; col = 6; break;
//		case 66: row = 3; col = 0; break;
//		case 67: row = 3; col = 3; break;
//		case 68: row = 3; col = 6; break;
//		case 69: row = 6; col = 0; break;
//		case 70: row = 6; col = 3; break;
//		case 71: row = 6; col = 6; break;		
//		//9th 9X9
//		case 72: row = 0; col = 0; break; 
//		case 73: row = 0; col = 3; break;
//		case 74: row = 0; col = 6; break;
//		case 75: row = 3; col = 0; break;
//		case 76: row = 3; col = 3; break;
//		case 77: row = 3; col = 6; break;
//		case 78: row = 6; col = 0; break;
//		case 79: row = 6; col = 3; break;
//		case 80: row = 6; col = 6; break;
//		default: System.out.println("invalid index"); return null;
//		}
		//get row and col with case statement, external method, or expression
		int box[] = new int[81];
		int m = 0;
		for (int j = row; j < row+9;j++)
		{
			for (int k = col; k < col+9;k++)
			{
				box[m] = big[j][k].getVal();
				m++;
				
			}
		}
		return box;
	}

}
