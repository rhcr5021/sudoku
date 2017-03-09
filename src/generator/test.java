package generator;

import java.io.IOException;

public class test {
	static int zeros = 0;
	static int puzNum;
	static String diff = "easy";
	public static void main(String[] args) {
		//make generator
		System.out.println("Method 1: Import a Puzzle");
		Generator generator = new Generator();
		Sudoku sud = new Sudoku();
		int[][] puzzle = new int[9][9];
		int[][] puzSol = new int[9][9];
		try {
			//get a puzzle
			puzNum = sud.importPuzzle(diff,puzzle);
			//get its solution
			puzSol = sud.importSolution(diff, puzNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(diff + "Puz" + puzNum);
		//print the puzzle
		sud.printPuzzle(puzzle);
		//find number of empty
		System.out.println("empty: " + generator.countEmpty(puzzle));
		//count errors
		System.out.println("errors: " + sud.countErrors(puzzle));	
		System.out.println(diff + "Sol" + puzNum);
		//print solutions
		sud.printPuzzle(puzSol);
		//find empty
		System.out.println("empty: " + generator.countEmpty(puzSol));
		//count errors
		System.out.println("errors: " + sud.countErrors(puzSol));	
		System.out.println("\n\nMethod 2: Generator Function");
		//get a puzzle
		int[][] puz = sud.intPuzzleGen(zeros);
		//print it out
		sud.printPuzzle(puz);
		//count how many empty values there are
		System.out.println("empty: " + generator.countEmpty(puz));
		System.out.println("errors: " + sud.countErrors(puz));
		//create an error
		puz[0][0] = puz[8][8];
		//print the puzzle
		sud.printPuzzle(puz);
		//count the errors
		System.out.println("empty: " + generator.countEmpty(puz));
		System.out.println("errors: " + sud.countErrors(puz));
	}
}