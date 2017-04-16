package backtracking;

import java.io.IOException;

public class BackTrackBigTest {
	public static int zeros = 0;
	public static String diff = "expert";
	public static int n = 6;
	public static int num = 100;
	public static void main(String[] args) throws IOException {
		final long startTime = System.nanoTime();
		BigBackTracking sud = new BigBackTracking("puzzles/puz_81_81_ex.csv");
		sud.printBigPuzzle();
		boolean t = sud.solveBig();
		sud.printBigPuzzle();
		System.out.println(sud.countErrorsBig());
		System.out.println(t);
		final double duration = System.nanoTime() - startTime;
		System.out.println("duration: " + (duration/1000000000) + " s");
	}
		
}
