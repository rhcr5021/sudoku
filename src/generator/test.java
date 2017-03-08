package generator;

import java.io.IOException;

public class test {
	static int zeros = 0;
	public static void main(String[] args) {
		//make generator
		Generator generator = new Generator();
		Sudoku sud = new Sudoku();
//		try {
//			sud.importCSVpuzzle("puz.csv");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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