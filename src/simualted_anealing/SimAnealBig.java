package simualted_anealing;

import java.io.IOException;

import generator.BigSudoku;
import generator.Cell;

public class SimAnealBig extends BigSudoku{
	private Cell[][] tempPuzzle;
	
	public SimAnealBig(String filename) throws IOException {
		super(filename);
		// TODO Auto-generated constructor stub
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
	
	
	public int countErrorsBig(Cell[][] mypuzz)
	{
		//count errors for all rows, columns, and boxes
		int row = 0, col = 0, box = 0;
		for (int i = 0; i < 9; i++)
		{
			row += checkSectionBig(getRowBig(i,mypuzz));
			col += checkSectionBig(getColBig(i, mypuzz));
			box += checkSectionBig(getBoxBig(i, mypuzz));
		}
		return (row+box+col);
	}
	
	private int[] getBoxBig(int i,Cell[][] mypuzz) {
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
				box[m] = mypuzz[j][k].getVal();
				m++;
				
			}
		}
		return box;
	}
	private int [] getColBig(int i,Cell[][] mypuzz) {
		int [] col = new int[81];
		for (int j = 0; j < 81; j++)
		{
			col[j] = mypuzz[j][i].getVal();
		}
		return col;
	}
	
	private int[] getRowBig(int i,Cell[][] mypuzz) {
		int [] row = new int[81];
		for (int j = 0; j < 81; j++)
		{
			row[j] = mypuzz[i][j].getVal();
		}
		return row;
	}
	
	public int[] solve(double a, double min_t, int temp_change, double k, double t){
		int[] ret = new int[2];
		fillNeedsBig();
		big=copyPuzzleBig(scramblePuzzle(big,500));
		printBigPuzzle();
		int minerror=Integer.MAX_VALUE;
		int i=0;
		int loop_counter=0;
		Cell[][] temp;
		while(t>min_t){
			//System.out.println(loop_counter);
			temp = copyPuzzleBig(big);
			loop_counter++;
			if(i==temp_change){
				i=0;
				t=t*a;
			}
			i+=1;
			chooseandSwapBig(temp);
			int oldError=countErrorsBig();
			int newError=countErrorsBig(temp);
			
			if(newError < oldError){
				big=copyPuzzleBig(temp);
			}
			else if(Math.random() < Math.exp((oldError-newError)/(k*t)) ){
				big=copyPuzzleBig(temp);
			}
			if(newError<minerror){
				minerror=newError;
			}	
			if(countErrorsBig() == 0)
			{
				ret[0] = minerror;
				ret[1] = loop_counter;
				return ret;
			}
			//System.out.println(newError);
			if(minerror==0){
				break;
			}
		}
		
		ret[0] = minerror;
		ret[1] = loop_counter;
		return ret;
	}
	
	public boolean chooseandSwapBig(Cell[][] puz)
	{
		int c1 = (int)(Math.random()*9);
		int r1 = (int)(Math.random()*9);
		
		//or find neighbor
		
		int c2 = (int)(Math.random()*9);
		int r2 = (int)(Math.random()*9);
		return tempSwap(puz,r1,c1,r2,c2);
	}
	
	private boolean tempSwap(Cell[][] puz, int i,int j,int m,int n)
	{
		if(puz[i][j].getFlag() || puz[m][n].getFlag())
		{
			return false;
		}
		if(puz[i][j].tryLock())
		{
			try {
				if(puz[m][n].tryLock())
				{
					try {
						//System.out.println(m + "," + n);
						int temp = puz[i][j].getVal();
						puz[i][j].setVal(puz[m][n].getVal());
						puz[m][n].setVal(temp);
					} finally {
						puz[m][n].unlock();
					}
				}
				else 
				{
					return false;
				}
			} finally{
				puz[i][j].unlock();
			}
		}
		else
		{
			return false;
		}
		return true;
	}
	
	protected Cell [][] scramblePuzzle(Cell[][] puz, int scrambles)
	{
		Cell[][] scrambled = new Cell[81][81];
		for (int i = 0; i < 81; i++)
		{
			//go through the 9 chars in line
			for (int j = 0; j < 81; j++)
			{
				scrambled[i][j] = new Cell(puz[i][j].getVal(),puz[i][j].getFlag());	
				
			}
		}
		for(int k=0; k<scrambles; k++){
			chooseandSwapBig(scrambled);
		}
		return scrambled;
	}
	
}
