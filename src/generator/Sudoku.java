package generator;

import generator.Cell;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

public class Sudoku {
	protected Cell[][] puzzle;
	protected final Cell[][] solution;
	private int[] counts;
	protected int num;
	protected String dif;
	protected int err;
	protected AtomicInteger fin;
	private RowErr[] ro;
	private ColErr[] co;
	private BoxErr[] bo;
	protected Cell[][] big;
//	ExecutorService service = Executors.newFixedThreadPool(9*3);
	private ExecutorService service = Executors.newFixedThreadPool(5);
	private BoundedQueue<Integer> q;
	private List<RowErr> RowList = new ArrayList<RowErr>();
	private List<ColErr> ColList = new ArrayList<ColErr>();
	private List<BoxErr> BoxList = new ArrayList<BoxErr>();
	private Future<Integer>[] rowe = new Future[9];
	private Future<Integer>[] cole = new Future[9];
	private Future<Integer>[] boxe = new Future[9];
	
	public Cell[][] getPuzzle()
	{
		return puzzle;
	}
	
	public Sudoku(String diff) throws IOException {
		super();
		dif = diff;
		counts = new int[10];
		num = (int)(Math.random() * 20);
		puzzle = importPuzzle(diff,num);
		solution = importSolution(diff, num);
		err = countErrors();
		fin = new AtomicInteger(0);
		ro = new RowErr[9];
		co = new ColErr[9];
		bo = new BoxErr[9];
		for (int i = 0; i < 9; i++)
		{
			ro[i] = new RowErr(i);
			co[i] = new ColErr(i);
			bo[i] = new BoxErr(i);
		}
	}
	
	public Sudoku(String diff, int n) throws IOException {
		super();
		counts = new int[10];
		num = n;
		dif = diff;
		puzzle = importPuzzle(diff,n);
		solution = importSolution(diff, n);
		fin = new AtomicInteger(0);
		ro = new RowErr[9];
		co = new ColErr[9];
		bo = new BoxErr[9];
		for (int i = 0; i < 9; i++)
		{
			ro[i] = new RowErr(i);
			co[i] = new ColErr(i);
			bo[i] = new BoxErr(i);
		}
	}
	
	public void updateErr() {
		this.err = countErrors();
	}
	
	public int getErr() {
		return this.err;
	}
	
	public void fillNeeds()
	{
		int [] needs = getNeeds();
		int filler = 1;
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				if (puzzle[i][j].getFlag() == false)
				{
					if(needs[filler] == 9)
					{
						filler++;
					}
					puzzle[i][j].setVal(filler); 
					needs[filler]++;
				}
			}
		}
		updateErr();
	}
	
	public boolean isSol()
	{
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				if (puzzle[i][j].getVal() != solution[i][j].getVal())
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isValid()
	{
		for(int i = 1; i <= 9; i++)
		{
			if(countVal(i) != 9)
			{
				return false;
			}
		}
		return true;
	}

	private int countVal(int v) {
		int count = 0;
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				if (puzzle[i][j].getVal() == v)
				{
					count++;
				}
			}
		}
		return count;
		
	}
	
	public int[] getNeeds()
	{
		int [] needs = new int[10];
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				if (puzzle[i][j].getFlag())
				{
					needs[puzzle[i][j].getVal()]++;
				}
			}
		}
		return needs;
	}
	
	public boolean chooseandSwap()
	{
		int c1 = (int)(Math.random()*9);
		int r1 = (int)(Math.random()*9);
		
		//or find neighbor
		
		int c2 = (int)(Math.random()*9);
		int r2 = (int)(Math.random()*9);
		return swap(r1,c1,r2,c2);
	}

	private boolean swap(int i,int j,int m,int n)
	{
		if(puzzle[i][j].getFlag() || puzzle[m][n].getFlag())
		{
			return false;
		}
		//while(!puzzle[i][j].tryLock());
		if(puzzle[i][j].tryLock())
		{
			try {
				//while(!puzzle[m][n].tryLock());
				if(puzzle[m][n].tryLock())
				{
					try {
						//System.out.println(m + "," + n);
						int temp = puzzle[i][j].getVal();
						puzzle[i][j].setVal(puzzle[m][n].getVal());
						puzzle[m][n].setVal(temp);
					} finally {
						puzzle[m][n].unlock();
					}
				}
				else 
				{
					return false;
				}
			} finally{
				puzzle[i][j].unlock();
			}
		}
		else
		{
			return false;
		}
		updateErr();
		return true;
	}
	
	private class RowErr implements Callable<Integer>
	{
		int r;
		public RowErr(int r)
		{
			this.r = r;
			//row = getRow(r);
		}
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			return checkSection(getRow(r));
		}
	}
	
	private class ColErr implements Callable<Integer>
	{
		int c;
		public ColErr(int c)
		{
			this.c = c;
		}
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			return checkSection(getCol(c));
		}
	}
	
	private class BoxErr implements Callable<Integer>
	{
		int b;
		public BoxErr(int b)
		{
			this.b = b;
		}

		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			return checkSection(getBox(b));
		}
	}
	
	private class SumBox implements Callable<Integer>
	{
		public Integer call() throws Exception {
			int err = 0;
			for (int i = 0; i < 9; i++)
			{
				err += boxe[i].get();
			}
			return err;
		}
	}
	
	private class SumRow implements Callable<Integer>
	{
		public Integer call() throws Exception {
			int err = 0;
			for (int i = 0; i < 9; i++)
			{
				err += rowe[i].get();
			}
			return err;
		}
	}
	
	private class SumCol implements Callable<Integer>
	{
		public Integer call() throws Exception {
			int err = 0;
			for (int i = 0; i < 9; i++)
			{
				err += cole[i].get();
			}
			return err;
		}
	}
	
	int countErrConcurrent()
	{
		int err = 0;
		try {
			for (int i = 0; i < 9; i++)
			{
				rowe[i] = service.submit(ro[i]);
				cole[i] = service.submit(bo[i]);
				boxe[i] = service.submit(co[i]);
			}
			service.awaitTermination(10000, TimeUnit.MICROSECONDS);
			Future<Integer> row_total = service.submit(new SumRow());
			Future<Integer> box_total = service.submit(new SumBox());
			Future<Integer> col_total = service.submit(new SumCol());
			err += row_total.get() + box_total.get() + col_total.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return err;
	}
	
	public int countErrors()
	{
		//count errors for all rows, columns, and boxes
		int row = 0, col = 0, box = 0;
		for (int i = 0; i < 9; i++)
		{
			row += checkSection(getRow(i));
			col += checkSection(getCol(i));
			box += checkSection(getBox(i));
		}
		return (row+box+col);
	}
	
	protected boolean isErrors() {
		for (int i = 0; i < 9; i++)
		{
			if(isErrorInSection(getRow(i)))
			{
				return true;
			}
			else if(isErrorInSection(getCol(i)))
			{
				return true;
			}
			else if(isErrorInSection(getBox(i)))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean isErrorInSection(int[] row) {
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
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
	public int countEmpty()
	{
		int i,j,count = 0;
		for ( i = 0; i < 9; i++)
		{
			for ( j = 0; j < 9; j++)
			{
				if(puzzle[i][j].getVal() == 0)
					count++;
			}
		}
		return count;
	}
	
	public int countSet()
	{
		int i,j,count = 0;
		for ( i = 0; i < 9; i++)
		{
			for ( j = 0; j < 9; j++)
			{
				if(puzzle[i][j].getFlag() == true)
					count++;
			}
		}
		return count;
	}
	
	protected int checkSection(int row[]) {
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
	
	private int[] getBox(int i) {
		//use index i to get col and row indexes for the box
		/* =======
		 * |0|1|2|
		 * |3|4|5|
		 * |6|7|8|
		 * =======
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
				box[m] = puzzle[j][k].getVal();
				m++;
				
			}
		}
		return box;
	}
	
	private int [] getCol(int i) {
		int [] col = new int[9];
		for (int j = 0; j < 9; j++)
		{
			col[j] = puzzle[j][i].getVal();
		}
		return col;
	}
	
	private int[] getRow(int i) {
		int [] row = new int[9];
		for (int j = 0; j < 9; j++)
		{
			row[j] = puzzle[i][j].getVal();
		}
		return row;
	}

	public void printPuzzle()
	{
	  int i,j;
	  System.out.println("\n"+dif+"Puz"+num);
	  System.out.print("-------------------------\n");
	  for ( i = 0; i < 9; i++)
		{
			for ( j = 0; j < 9; j++)
			{
				if (j % 3 == 0)
				{
					System.out.print("- ");
				}
				if(puzzle[i][j].getFlag())
				{
					System.out.print(puzzle[i][j].getVal() + " ");
//					String a = "\033[33m" + Integer.toString(puzzle[i][j].val);
//					System.out.print(a + " ");
				}
				else
				{
					System.out.print(puzzle[i][j].getVal() + " ");
				}
			}
			if (i % 3 == 2)
			{
				System.out.print("-\n------------------------");
			}
			System.out.println("-");
		}
	}
	
	public void printInputedPuzzle(Cell[][] puz)
	{
	  int i,j;
	  System.out.println("\n"+dif+"Puz"+num);
	  System.out.print("-------------------------\n");
	  for ( i = 0; i < 9; i++)
		{
			for ( j = 0; j < 9; j++)
			{
				if (j % 3 == 0)
				{
					System.out.print("- ");
				}
				if(puz[i][j].getFlag())
				{
					System.out.print(puz[i][j].getVal() + " ");
//					String a = "\033[33m" + Integer.toString(puzzle[i][j].val);
//					System.out.print(a + " ");
				}
				else
				{
					System.out.print(puz[i][j].getVal() + " ");
				}
			}
			if (i % 3 == 2)
			{
				System.out.print("-\n------------------------");
			}
			System.out.println("-");
		}
	}
	
	public void printSolution()
	{
		  int i,j;
		  System.out.println(dif+"Sol"+num);
		  System.out.print("-------------------------\n");
		  for ( i = 0; i < 9; i++)
			{
				for ( j = 0; j < 9; j++)
				{
					if (j % 3 == 0)
					{
						System.out.print("- ");
					}
					System.out.print(solution[i][j].getVal() + " ");
				}
				if (i % 3 == 2)
				{
					System.out.print("-\n------------------------");
				}
				System.out.println("-");
			}
	}
	
	public Cell[][] importPuzzle(String diff, int num) throws IOException
	{
		Cell[][] puz = new Cell[9][9];
		//pass difficulty and puzzle reference
		//return puzzle number
		//twenty puzzles for each difficulty
		//get random puzzle number
		//scale puzzle between 1 and 20

		//parse file name
		String filename = "puzzles/" + diff + "/" + diff + "Puz" + num + ".txt";
		File f = new File(filename);
		Scanner infile = new Scanner(f);
		String line;
		//go through 9 lines in puzzle
		for (int i = 0; i < 9; i++)
		{
			//go through the 9 chars in line
			line = infile.nextLine();
			for (int j = 0; j < 9; j++)
			{
				if (line.charAt(j) == '.')
				{
					//'.'s are 0s
					puz[i][j] = new Cell(0,false);
				}
				else
				{
					//convert char to int
					int n = Character.getNumericValue(line.charAt(j));
					puz[i][j] = new Cell(Character.getNumericValue(line.charAt(j)),true);
					counts[n]++;
				}
			}
		}
		infile.close();
		//return puzzle id
		return puz;
	}
	
	public Cell[][] importSolution(String diff, int num) throws IOException
	{
		//return solution for specified difficulty and puzzle number
		Cell[][] puz = new Cell[9][9];
		//parse file name and path
		String filename = "puzzles/" + diff + "/" + diff + "Sol" + num + ".txt";
		File f = new File(filename);
		Scanner infile = new Scanner(f);
		String line;
		for (int i = 0; i < 9; i++)
		{
			line = infile.nextLine();
			for (int j = 0; j < 9; j++)
			{
				if (line.charAt(j) == '.')
				{
					//'.'s are zeros are unfilled cells
					puz[i][j] = new Cell(0,false);
				}
				else
				{
					//convert to int
					puz[i][j] = new Cell(Character.getNumericValue(line.charAt(j)),true);
				}
			}
		}
		infile.close();
		return puz;
	}
	
	public void resetPuzzle() throws IOException
	{
		puzzle = importPuzzle(dif,num);
	}
	
	public boolean isEmptyLocation(int row,int col)
	{
		if (puzzle[row][col].getVal() == 0)
		{
			return true;
		}
		return false;
	}
	
	protected Cell [][] copyPuzzle(Cell[][] puz)
	{
		Cell[][] copy = new Cell[9][9];
		for (int i = 0; i < 9; i++)
		{
			//go through the 9 chars in line
			for (int j = 0; j < 9; j++)
			{
				copy[i][j] = new Cell(puz[i][j].getVal(),puz[i][j].getFlag());	
			}
		}
		return copy;
	}
	
	
	
	public int countCorrect(Cell[][] puz)
	{
		int count = 0;
		for (int i = 0; i < 9; i++)
		{
			//go through the 9 chars in line
			for (int j = 0; j < 9; j++)
			{
				if(solution[i][j].getVal() == puz[i][j].getVal())
				{
					count++;
				}
			}
		}
		return count;
	}
}
