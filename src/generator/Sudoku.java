package generator;

import java.io.*;
import java.util.Scanner;

public class Sudoku {
	public int countErrors(int[][] puz)
	{
		//count errors for all rows, columns, and boxes
		int row = 0, col = 0, box = 0;
		for (int i = 0; i < 9; i++)
		{
			row += checkSection(getRow(puz,i));
			col += checkSection(getCol(puz,i));
			box += checkSection(getBox(puz,i));
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
				if(i > j && row[i] == row[j])
				{
					count++;
				}
			}
		}
		return count;
	}
	
	private int[] getBox(int[][] puz, int i) {
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
				box[m] = puz[j][k];
				m++;
				
			}
		}
		return box;
	}
	
	private int [] getCol(int[][] puz, int i) {
		int [] col = new int[9];
		for (int j = 0; j < 9; j++)
		{
			col[j] = puz[j][i];
		}
		return col;
	}
	
	private int[] getRow(int[][] puz, int i) {
		int [] row = new int[9];
		for (int j = 0; j < 9; j++)
		{
			row[j] = puz[i][j];
		}
		return row;
	}

	public void printPuzzle(int[][] puzzle)
	{
		  int i,j;
		  System.out.print("-------------------------\n");
		  for ( i = 0; i < 9; i++)
			{
				for ( j = 0; j < 9; j++)
				{
					if (j % 3 == 0)
					{
						System.out.print("- ");
					}
					System.out.print(puzzle[i][j] + " ");
				}
				if (i % 3 == 2)
				{
					System.out.print("-\n------------------------");
				}
				System.out.println("-");
			}
	}
	public int[][] intPuzzleGen(int num)
	{
		Generator generator = new Generator();
		Grid grid = generator.generate(num);
		int[][] puzzle = new int[9][9];
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				puzzle[i][j] = grid.grid[i][j].getValue();
			}
		}
		return puzzle;
	}
	
	public int[][] importCSVpuzzle(String filename) throws IOException
	{
		int puz[][] = new int[9][9];
		File f = new File(filename);
		Scanner infile = new Scanner(f);
		infile.nextLine();
		String s = infile.nextLine();
		System.out.println(s);
		for (int i = 0; i < 81; i++)
		{
			
		}
		return puz;
	}
}

	
