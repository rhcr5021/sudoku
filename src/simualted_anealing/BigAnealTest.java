package simualted_anealing;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class BigAnealTest {
	static double a=.99;
	static double min_t=.00001;
	static int temp_change = 100;
	static double k=1;
	static double t=1;
	public static SimAnealBig sud;
	public static void main(String[] args) throws IOException {
		final long startTime = System.nanoTime();
		int loops=0;
		while(true){
			System.out.println(loops);
			loops++;
			sud = new SimAnealBig("puzzles/puz_81_81_ex.csv");
			sud.solve(a,min_t,temp_change,k,t);
			int err = sud.countErrorsBig();
			if (err == 0)
				break;
			System.out.println("Errors in Puzzle: " + err);
			
		}
		sud.printBigPuzzle();
		System.out.println("Number of Loop Iterations: " + loops);
		System.out.println("Errors in Puzzle: " + sud.countErrorsBig());
		final double duration = System.nanoTime() - startTime;
		System.out.println("duration: " + (duration/1000000000) + " s");
	}

}
