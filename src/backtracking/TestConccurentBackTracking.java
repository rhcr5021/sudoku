package backtracking;

import generator.Cell;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TestConccurentBackTracking {

	public static String diff = "expert";
	public static int n = 6;
	public static int num = 100;
	public static void main(String[] args) throws IOException, ExecutionException {
		final long startTime = System.nanoTime();
		ConcurrentBacktracking sud = new ConcurrentBacktracking(diff, n,num);
		sud.printPuzzle();
		sud.printSolution();
		System.out.println("---------Solving---------");
		//System.out.println(sud.solve());
		Cell[][] sol= sud.solveConcurrent2();
		if(sol== null){
			System.out.println("no solution found");
//			System.exit(0);
			return;
		}
		System.out.println(sol);
		
		sud.printInputedPuzzle(sol);
		//sud.printSolution();
		System.out.println("errors: " + sud.countErrors(sol));
//		System.out.println("err: " + sud.getErr(sol));		
//		System.out.println("puzzle still valid: " + sud.isValid(sol));
		final double duration = System.nanoTime() - startTime;
		System.out.println("duration: " + (duration/1000000000) + " s");
		System.exit(0);
	}

}
