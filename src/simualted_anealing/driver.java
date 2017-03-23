package simualted_anealing;

import generator.Sudoku.Cell;

import java.io.IOException;

import backtracking.ThreadID;

public class driver {
	public static SimAneal sud;
	public static String diff = "expert";
	public static int num = 12;
	public static Thread[] aneals;
	public static int num_threads = 100;
	public static LogAnealData log;
	
	//anealing parameters
	static double a=.999;
	static double min_t=.00000001;
	static int temp_change = 100;
	static double k=0.9;
	static double t=1;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final long startTime = System.nanoTime();
		log = new LogAnealData();
		sud = new SimAneal(diff,num);
		sud.printPuzzle();
		aneals = new Thread[num];
//		for (int j = 0; j < num; j++)
//		{
//			aneals[j] = new Thread(new Runnable(){
//				public void run() {
//					for(int i = 0; i < num; i++)
//					{
//						sud.solve();
//					}
//					System.out.println(ThreadID.get());	
//				}
//			});
//			aneals[j].start();
//		}
//		for (int j = 0; j < num; j++)
//		{
//			try {
//				aneals[j].join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		//int[] data = sud.solveOnCorrect();
		int[] data = sud.solve(a,min_t,temp_change,k,t);
		sud.printSolution();
		sud.printPuzzle();
		System.out.println("Minimum Number of Errors: " + data[0]);
		System.out.println("Number of Loop Iterations: " + data[1]);
		System.out.println("Errors in Puzzle: " + sud.countErrors());
		System.out.println("Puzzle is Valid: " + sud.isValid());
		System.out.println("Correct Indices: " + sud.countCorrect(sud.getPuzzle()));
		System.out.println("is Solution?: " + sud.isSol());
		final double duration = System.nanoTime() - startTime;
		log.logInit(a,min_t,temp_change,k,t);
		log.log_results(data,sud.countErrors(),sud.isValid(),sud.countCorrect(sud.getPuzzle()),sud.isSol(),duration/1000000000);
		System.out.println("duration: " + (duration/1000000000) + " s");
	}

}
