package simualted_anealing;

import java.io.IOException;

import generator.Sudoku;
import generator.Sudoku.Cell;

public class SimAneal extends Sudoku{
	private Cell[][] tempPuzzle;

	public SimAneal(String diff) throws IOException {
		super(diff);
		// TODO Auto-generated constructor stub
	}
	public SimAneal(String diff, int n) throws IOException {
		super(diff,n);
	}
	
	public boolean chooseandSwap()
	{
		int c1 = (int)(Math.random()*9);
		int r1 = (int)(Math.random()*9);
		
		//or find neighbor
		
		int c2 = (int)(Math.random()*9);
		int r2 = (int)(Math.random()*9);
		return tempSwap(r1,c1,r2,c2);
	}

	private boolean tempSwap(int i,int j,int m,int n)
	{
		if(tempPuzzle[i][j].getFlag() || tempPuzzle[m][n].getFlag())
		{
			return false;
		}
		if(tempPuzzle[i][j].tryLock())
		{
			try {
				if(tempPuzzle[m][n].tryLock())
				{
					try {
						//System.out.println(m + "," + n);
						int temp = tempPuzzle[i][j].getVal();
						tempPuzzle[i][j].setVal(tempPuzzle[m][n].getVal());
						tempPuzzle[m][n].setVal(temp);
					} finally {
						tempPuzzle[m][n].unlock();
					}
				}
				else 
				{
					return false;
				}
			} finally{
				tempPuzzle[i][j].unlock();
			}
		}
		else
		{
			return false;
		}
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
	private int checkSection(int row[]) {
		int count = 0;
		//check for duplicates
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
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
	
	private int[] getBox(int i, Cell[][] mypuzz) {
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
				box[m] = tempPuzzle[j][k].getVal();
				m++;
				
			}
		}
		return box;
	}
	
	private int [] getCol(int i, Cell[][] mypuzz) {
		int [] col = new int[9];
		for (int j = 0; j < 9; j++)
		{
			col[j] = tempPuzzle[j][i].getVal();
		}
		return col;
	}
	
	private int[] getRow(int i, Cell[][] mypuzz) {
		int [] row = new int[9];
		for (int j = 0; j < 9; j++)
		{
			row[j] = tempPuzzle[i][j].getVal();
		}
		return row;
	}
	
	
	
	public void aneal()
	{
		fillNeeds();
		printPuzzle();
		int minerror=1000;
		double t=1;
		double a=.9;
		double min_t=.0000001;
		int i=0;
		int loop_counter=0;
		double k=1;
		while(min_t<t){
			tempPuzzle=puzzle;
			loop_counter+=1;
			if(i==100){
				i=0;
				t=t*a;
			}
			i+=1;
			chooseandSwap();
			int oldError=countErrors(puzzle);
			int newError=countErrors(tempPuzzle);		
			
			if(newError < oldError){
				puzzle=tempPuzzle;
			}
			else if(Math.random()< Math.exp((oldError-newError)/(k*t)) ){
				puzzle=tempPuzzle;
			}
			if(newError<minerror){
				minerror=newError;
			}
			
		}
		System.out.println(loop_counter);
		printPuzzle();
		System.out.println(minerror);
	}
}
