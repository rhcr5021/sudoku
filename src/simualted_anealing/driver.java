package simualted_anealing;

import java.io.IOException;

import backtracking.ThreadID;

public class driver {
	public static SimAneal sud;
	public static String diff = "expert";
	public static int num = 12;
	public static Thread[] aneals;
	public static int num_threads = 100;
	public static LogAnealData log;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final long startTime = System.nanoTime();
		log = new LogAnealData();
		log.logInit();
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
		int[] data = sud.solve();
		sud.printSolution();
		sud.printPuzzle();
		System.out.println("Minimum Number of Errors: " + data[0]);
		System.out.println("Number of Loop Iterations: " + data[1]);
		System.out.println("Errors in Puzzle: " + sud.countErrors());
		System.out.println("Puzzle is Valid: " + sud.isValid());
		System.out.println("Correct Indices: " + sud.countCorrect(sud.getPuzzle()));
		System.out.println("is Solution?: " + sud.isSol());
		final double duration = System.nanoTime() - startTime;
		System.out.println("duration: " + (duration/1000000000) + " s");
	}

}
