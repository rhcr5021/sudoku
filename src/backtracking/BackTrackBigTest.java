package backtracking;

import generator.Sudoku.Cell;

import java.io.IOException;

public class BackTrackBigTest {
	public static int zeros = 0;
	public static String diff = "expert";
	public static int n = 6;
	public static int num = 100;
	public static void main(String[] args) throws IOException {
		final long startTime = System.nanoTime();
		Backtracking sud = new Backtracking(diff, n,num);
		sud.big = sud.massivePuzImport("puzzles/puz_81_81_ex.csv");
		sud.printBigPuzzle(sud.big);
		sud.solveBig();
		sud.printBigPuzzle(sud.big);
		final double duration = System.nanoTime() - startTime;
		System.out.println("duration: " + (duration/1000000000) + " s");
	}
		
}
