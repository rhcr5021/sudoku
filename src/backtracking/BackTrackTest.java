package backtracking;

import java.io.IOException;

import generator.Sudoku;

public class BackTrackTest {
	public static int zeros = 0;
	public static String diff = "expert";
	public static int n = 6;
	public static int num = 100;
	public static void main(String[] args) throws IOException {
		final long startTime = System.nanoTime();
		Backtracking sud = new Backtracking(diff, n,num);
		sud.printPuzzle();
		System.out.println("---------Solving---------");
		System.out.println(sud.solve());
//		System.out.println(sud.solveConcurrent());
		sud.printPuzzle();
		//sud.printSolution();
		System.out.println("errors: " + sud.countErrors());
		System.out.println("err: " + sud.getErr());		
		System.out.println("puzzle still valid: " + sud.isValid());
		final double duration = System.nanoTime() - startTime;
		System.out.println("duration: " + (duration/1000000000) + " s");
		
	}

}
