package generator;

import generator.Sudoku.Cell;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.locks.*;

public class Sudoku {
	protected Cell[][] puzzle;
	protected final Cell[][] solution;
	private int[] counts;
	protected int num;
	protected String dif;
	protected int err;
	
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
	}
	public Sudoku(String diff, int n) throws IOException {
		super();
		counts = new int[10];
		num = n;
		dif = diff;
		puzzle = importPuzzle(diff,n);
		solution = importSolution(diff, n);
	}
	
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
				if (puzzle[i][j].flag == false)
				{
					if(needs[filler] == 9)
					{
						filler++;
					}
					puzzle[i][j].val = filler; 
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
				if (puzzle[i][j].val == v)
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
				if (puzzle[i][j].flag)
				{
					needs[puzzle[i][j].val]++;
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
						puzzle[i][j].setVal(puzzle[m][n].val);
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
				if(puzzle[i][j].flag)
				{
					System.out.print(puzzle[i][j].val + " ");
//					String a = "\033[33m" + Integer.toString(puzzle[i][j].val);
//					System.out.print(a + " ");
				}
				else
				{
					System.out.print(puzzle[i][j].val + " ");
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
	public Cell[][] intPuzzleGen(int num)
	{
		Generator generator = new Generator();
		Grid grid = generator.generate(num);
		Cell[][] puz = new Cell[9][9];
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				puz[i][j].setVal(grid.grid[i][j].getValue());
				if (puz[i][j].getVal() == 0)
				{
					puz[i][j].setFlag(false);
				}
				else
				{
					puz[i][j].setFlag(true);
				}
			}
		}
		return puz;
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

	public void printBigPuzzle(Cell[][] big) {
		int i,j;
		System.out.print("-------------------------\n");
		for ( i = 0; i < 81; i++)
		{
			for ( j = 0; j < 81; j++)
			{
				if (j % 9 == 0)
				{
					System.out.print("- ");
				}
				if(big[i][j].flag)
				{
					System.out.print(big[i][j].val + " ");
		//						String a = "\033[33m" + Integer.toString(puzzle[i][j].val);
		//						System.out.print(a + " ");
				}
				else
				{
					System.out.print(big[i][j].val + " ");
				}
			}
			if (i % 3 == 2)
			{
				System.out.print("-\n------------------------");
			}
			System.out.println("-");
		}
	}
	
}
	
