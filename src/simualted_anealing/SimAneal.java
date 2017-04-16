package simualted_anealing;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import sun.misc.Lock;

import generator.Sudoku;
import generator.Cell;

public class SimAneal extends Sudoku{
	private Cell[][] tempPuzzle;
	
	public SimAneal(String diff) throws IOException {
		super(diff);
		// TODO Auto-generated constructor stub
	}
	public SimAneal(String diff, int n) throws IOException {
		super(diff,n);
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
				box[m] = mypuzz[j][k].getVal();
				m++;
				
			}
		}
		return box;
	}
	
	private int [] getCol(int i, Cell[][] mypuzz) {
		int [] col = new int[9];
		for (int j = 0; j < 9; j++)
		{
			col[j] = mypuzz[j][i].getVal();
		}
		return col;
	}
	
	private int[] getRow(int i, Cell[][] mypuzz) {
		int [] row = new int[9];
		for (int j = 0; j < 9; j++)
		{
			row[j] = mypuzz[i][j].getVal();
		}
		return row;
	}
	
	public int[] solveOnCorrect(){
		int[] ret = new int[2];
		fillNeeds();
		int minerror=1000;
		double a=.9;
		double min_t=.00000001;
		int i=0;
		int loop_counter=0;
		double k=1;
		Cell[][] temp;
			double t=1;
		while(min_t<t){
			temp = copyPuzzle(puzzle);
			loop_counter+=1;
			if(i==100){
				i=0;
				t=t*a;
			}
			i+=1;
			chooseandSwap(temp);
			int oldError=countCorrect(puzzle);
			int newError=countCorrect(temp);
//			int oldError=countErrors();
//			int newError=countErrors(temp);
			
//			if(newError < oldError){
//				puzzle=copyPuzzle(temp);
//			}
			if(newError > oldError){
				puzzle=copyPuzzle(temp);
			}
			else if(Math.random() > Math.exp((oldError-newError)/(k*t)) ){
			puzzle=copyPuzzle(temp);
//			else if(Math.random() < Math.exp((oldError-newError)/(k*t)) ){
//				puzzle=copyPuzzle(temp);
			}
			if(newError<minerror){
				minerror=newError;
			}	
			if(countErrors() == 0)
			{
				ret[0] = minerror;
				ret[1] = loop_counter;
				return ret;
			}
		}
		ret[0] = minerror;
		ret[1] = loop_counter;
		return ret;
	}
	
	public int[] solve(double a, double min_t, int temp_change, double k, double t){
//		System.out.println("solving");
//		System.out.println(t);
		int[] ret = new int[2];
		fillNeeds();
//		System.out.println("scrambled : ");
		puzzle=copyPuzzle(scramblePuzzle(puzzle,500));
//		printPuzzle();
		int minerror=Integer.MAX_VALUE;
		int i=0;
		int loop_counter=0;
		Cell[][] temp;
		while(t>min_t && !driver.flag.get()){
//			System.out.println(loop_counter);
			temp = copyPuzzle(puzzle);
			loop_counter++;
			if(i==temp_change){
				i=0;
				t=t*a;
			}
			i+=1;
			chooseandSwap(temp);
			int oldError=countErrors();
			int newError=countErrors(temp);
			
			if(newError < oldError){
				puzzle=copyPuzzle(temp);
			}
			else if(Math.random() < Math.exp((oldError-newError)/(k*t)) ){
				puzzle=copyPuzzle(temp);
			}
			if(newError<minerror){
				minerror=newError;
			}	
			if(countErrors() == 0)
			{
				ret[0] = minerror;
				ret[1] = loop_counter;
				return ret;
			}
//		System.out.println(newError);
		if(minerror==0){
			break;
		}
		}
		
		ret[0] = minerror;
		ret[1] = loop_counter;
		return ret;
	}
	
	
	
	
	public boolean chooseandSwap(Cell[][] puz)
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
		Cell[][] scrambled = new Cell[9][9];
		for (int i = 0; i < 9; i++)
		{
			//go through the 9 chars in line
			for (int j = 0; j < 9; j++)
			{
				scrambled[i][j] = new Cell(puz[i][j].getVal(),puz[i][j].getFlag());	
				
			}
		}
		for(int k=0; k<scrambles; k++){
			chooseandSwap(scrambled);
		}
		return scrambled;
	}
}


