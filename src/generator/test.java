package generator;

import java.io.IOException;

public class test {
	static int zeros = 0;
	static String diff = "easy";
	public static void main(String[] args) {
		//make generator
		Generator generator = new Generator();
		Sudoku sud = new Sudoku();
		int[][] puzzle = new int[9][9];
		try {
			int puzNum = sud.importPuzzle(diff,puzzle);
			System.out.println(diff + "Puz" + puzNum);
			sud.printPuzzle(puzzle);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		//get a puzzle
//		int[][] puz = sud.intPuzzleGen(zeros);
//		//print it out
//		sud.printPuzzle(puz);
//		//count how many empty values there are
//		System.out.println("empty: " + generator.countEmpty(puz));
//		System.out.println("errors: " + sud.countErrors(puz));
//		//create an error
//		puz[0][0] = puz[8][8];
//		//print the puzzle
//		sud.printPuzzle(puz);
//		//count the errors
//		System.out.println("empty: " + generator.countEmpty(puz));
//		System.out.println("errors: " + sud.countErrors(puz));
	}
}