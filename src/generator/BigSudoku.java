package generator;

import generator.Cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class BigSudoku {
	protected Cell[][] big;
	
	public BigSudoku(String filename) throws IOException {
		super();
		big = massivePuzImport(filename);
	}
	
	public Cell[][] massivePuzImport(String filename) throws FileNotFoundException
	{
		Cell[][] puz = new Cell[81][81];
		File f = new File(filename);
		Scanner infile = new Scanner(f);
		String line;
		infile.useDelimiter("\\s*,\\s*");
		for (int i = 0; i < 81; i++)
		{
			for(int j = 0; j < 81; j++)
			{
				String s;
				if (j == 80)
				{
					infile.useDelimiter("\n");
					s = infile.next();
					infile.useDelimiter("\\s*,\\s*");
					s = s.substring(1);
				}
				else
				{
					s = infile.next();
				}
				if (s.contains("\n"))
				{
					s = s.substring(1);
				}
				if (s.hashCode() == 160)
				{
					//'.'s are zeros are unfilled cells
					puz[i][j] = new Cell(0,false);
				}
				else
				{
					//convert to int
					puz[i][j] = new Cell(Integer.parseInt(s),true);
				}
			}
		}
		infile.close();
		return puz;
	}
	
	public void fillNeedsBig()
	{
		int [] needs = getNeedsBig();
		int filler = 1;
		for (int i = 0; i < 81; i++)
		{
			for (int j = 0; j < 81; j++)
			{
				if (big[i][j].getFlag() == false)
				{
					if(needs[filler] == 81)
					{
						filler++;
					}
					big[i][j].setVal(filler); 
					needs[filler]++;
				}
			}
		}
	}
	
	public void printBigPuzzle() {
		int i,j;
		System.out.print("-----------------------------------------------------------" +
						"-------------------------------------------------------------" +
						"--------------------------------------------------------------" +
						"---------------------------------------------------------------" +
						"-----------------\n"); 
		for ( i = 0; i < 81; i++)
		{
			for ( j = 0; j < 81; j++)
			{
				if (j % 9 == 0)
				{
					System.out.print("- ");
				}
				if(big[i][j].getVal() / 10 == 0)
				{
					System.out.print(big[i][j].getVal() + "  ");
				}
				else
				{
					System.out.print(big[i][j].getVal() + " ");
				}
			}
			if (i % 9 == 8)
			{
				System.out.print("-\n---------------------------------------------" +
						"-------------------------------------------------------------" +
						"--------------------------------------------------------------" +
						"---------------------------------------------------------------" +
						"------------------------------"); 
			}
			System.out.println("-");
		}
	}

	private boolean isBigErrorInSection(int[] row) {
		for (int i = 0; i < 81; i++)
		{
			for (int j = 0; j < 81; j++)
			{
				//only count once per pair and don't count zeros
				if(i > j && row[i] == row[j] && row[i] != 0)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public int[] getNeedsBig()
	{
		int [] needs = new int[82];
		for (int i = 0; i < 81; i++)
		{
			for (int j = 0; j < 81; j++)
			{
				if (big[i][j].getFlag())
				{
					needs[big[i][j].getVal()]++;
				}
			}
		}
		return needs;
	}
	
	protected boolean isErrorsBig() {
		
		for (int i = 0; i < 81; i++)
		{
			if(isBigErrorInSection(getRowBig(i)))
			{
				return true;
			}
			else if(isBigErrorInSection(getColBig(i)))
			{
				return true;
			}
			else if(isBigErrorInSection(getBoxBig(i)))
			{
				return true;
			}
		}
		return false;
	}
	
	public int countErrorsBig() {
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

	protected int checkSectionBig(int row[]) {
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
	
	public boolean chooseandSwapBig()
	{
		int c1 = (int)(Math.random()*81);
		int r1 = (int)(Math.random()*81);
		
		//or find neighbor
		
		int c2 = (int)(Math.random()*81);
		int r2 = (int)(Math.random()*81);
		return swapBig(r1,c1,r2,c2);
	}
	
	private boolean swapBig(int i,int j,int m,int n)
	{
		if(big[i][j].getFlag() || big[m][n].getFlag())
		{
			return false;
		}
		//while(!big[i][j].tryLock());
		if(big[i][j].tryLock())
		{
			try {
				//while(!big[m][n].tryLock());
				if(big[m][n].tryLock())
				{
					try {
						//System.out.println(m + "," + n);
						int temp = big[i][j].getVal();
						big[i][j].setVal(big[m][n].getVal());
						big[m][n].setVal(temp);
					} finally {
						big[m][n].unlock();
					}
				}
				else 
				{
					return false;
				}
			} finally{
				big[i][j].unlock();
			}
		}
		else
		{
			return false;
		}
		return true;
	}
	
	protected Cell [][] copyPuzzleBig(Cell[][] puz)
	{
		Cell[][] copy = new Cell[81][81];
		for (int i = 0; i < 81; i++)
		{
			//go through the 9 chars in line
			for (int j = 0; j < 81; j++)
			{
				copy[i][j] = new Cell(puz[i][j].getVal(),puz[i][j].getFlag());	
			}
		}
		return copy;
	}
}
