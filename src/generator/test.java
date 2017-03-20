package generator;

import java.io.IOException;

public class test {
	public static int zeros = 0;
	public static int succ = 0;
	public static int num = 1000;
	public static int tot = 1000;
	public static int min = Integer.MAX_VALUE;
	public static Sudoku sud;
	public static Thread[] swaps;
	public static String diff = "expert";
	public static int p = 4;
	
	public static void main(String[] args) throws IOException {
		final long startTime = System.nanoTime();
		//make generator
		System.out.println("Method 1: Import a Puzzle");
		sud = new Sudoku(diff, p);
		sud.printPuzzle();
		sud.fillNeeds();
		sud.printPuzzle();
		System.out.println("errors: " + sud.countErrors());
		//Generator generator = new Generator();
		swaps = new Thread[num];
		for (int j = 0; j < num; j++)
		{
			swaps[j] = new Thread(new Runnable(){
				public void run() {
					for(int i = 0; i < tot; i++)
					{
						if(sud.chooseandSwap())
						{
							succ++;
						}
						//while(!sud.chooseandSwap());
						int m = sud.countErrors();
						if (m < min)
						{
							min = m;
						}
					}
					//System.out.println(succ);	
				}
			});
			swaps[j].start();
		}
		for (int j = 0; j < num; j++)
		{
			try {
				swaps[j].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(diff + "Sol"+sud.num);
		sud.printSolution();
		System.out.println(diff+"Puz"+sud.num);
		//print the puzzle
		sud.printPuzzle();
		//find number of empty
		System.out.println("empty: " + sud.countEmpty());
		//count errors
		System.out.println("errors: " + sud.countErrors());
		System.out.println("err: " + sud.getErr());		
		System.out.println("successful swaps: " + succ);	
		System.out.println("min errors: " + min);
		System.out.println("puzzle still valid: " + sud.isValid());
		final double duration = System.nanoTime() - startTime;
		System.out.println("duration: " + (duration/1000000000) + " s");
	}
}



//System.out.println(diff + "Sol"+sud.num);
//print solutions
//sud.printPuzzle();
//System.out.println("errors: " + sud.countErrors());	
//sud.fillNeeds();
//sud.printPuzzle();
//System.out.println("errors: " + sud.countErrors());
//int g = 0;
////while(sud.countErrors() > 100)
////{
////	boolean w = sud.chooseandSwap();
////	//System.out.println(w);
////	if(w)
////	{
////		g++;
////	}
////	if(g % 100000 == 0)
////		System.out.println(sud.countErrors());
////}
//sud.printPuzzle();
//System.out.println("errors: " + sud.countErrors());
//System.out.println("Successful Swaps" + g);
//System.out.println("\n\n \33[33m Method 2: Generator Function");

//get a puzzle
//int[][] puz = sud.intPuzzleGen(zeros);
////print it out
//sud.printPuzzle(puz);
////count how many empty values there are
//System.out.println("empty: " + generator.countEmpty(puz));
//System.out.println("errors: " + sud.countErrors(puz));
////create an error
//puz[0][0] = puz[8][8];
////print the puzzle
//sud.printPuzzle(puz);
////count the errors
//System.out.println("empty: " + generator.countEmpty(puz));
//System.out.println("errors: " + sud.countErrors(puz));