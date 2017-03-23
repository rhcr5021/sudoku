package simualted_anealing;

import generator.Sudoku.Cell;

import java.io.IOException;

//import backtracking.ThreadID;

public class driver {
	public static SimAneal sud;
	public static String diff = "simple";
	public static int num = 2;
	public static Thread[] aneals;
	public static int num_threads = 100;
	public static LogAnealData log;
	
	//anealing parameters
	static double a=.99;
	static double min_t=.00001;
	static int temp_change = 100;
	static double k=1;
	static double t=1;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void concurrent() throws IOException{
		System.out.println("being concurrent");
	}
	
	
	public static void sequential() throws IOException {
		final long startTime = System.nanoTime();
		log = new LogAnealData();
		int total=0;
//		int max=0;
		int correct;
		int loops=0;
//		for(int inc=0;inc<20;inc++)
		while(true){
			System.out.println(loops);
			loops++;
			sud = new SimAneal(diff,num);
			sud.solve(a,min_t,temp_change,k,t);
			correct=sud.countCorrect(sud.getPuzzle());
			System.out.println("inicides cor: " + correct);
//			total+=correct;
//			if(max<correct){
//				max=correct;
//			}
			if(correct==81){
				break;
			}
			
		}
//		sud = new SimAneal(diff,num);
//		sud.printPuzzle();
//		aneals = new Thread[num];
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
//		int[] data = sud.solve(a,min_t,temp_change,k,t);
		sud.printSolution();
		sud.printPuzzle();
//		System.out.println("Minimum Number of Errors: " + data[0]);
		System.out.println("Number of Loop Iterations: " + loops);
		System.out.println("Errors in Puzzle: " + sud.countErrors());
		System.out.println("Puzzle is Valid: " + sud.isValid());
		System.out.println("Correct Indices: " + sud.countCorrect(sud.getPuzzle()));
		System.out.println("is Solution?: " + sud.isSol());
		final double duration = System.nanoTime() - startTime;
//		log.logInit(a,min_t,temp_change,k,t);
//		log.log_results(data,sud.countErrors(),sud.isValid(),sud.countCorrect(sud.getPuzzle()),sud.isSol(),duration/1000000000);
		System.out.println("duration: " + (duration/1000000000) + " s");
//		System.out.println(total/20);
//		System.out.println(max);
	}

}
