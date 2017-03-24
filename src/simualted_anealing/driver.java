package simualted_anealing;

import generator.Sudoku.Cell;

import java.io.IOException;

//import backtracking.ThreadID;

public class driver {
	public static SimAneal sud;
	public String diff;
	public int num;
	public static Thread[] aneals;
	public static int num_threads = 100;
	public static LogAnealData log;
	SimAneal[] AnealArray;
	
	//anealing parameters
	static double a=.99;
	static double min_t=.00001;
	static int temp_change = 100;
	static double k=1;
	static double t=1;
	boolean flag;
	/**
	 * @param args
	 * @throws IOException 
	 */
	
	public driver(String diff, int num){
		this.diff=diff;
		this.num=num;
		this.flag=false;
	}
	
	public void concurrent() throws IOException{
		final long startTime = System.nanoTime();
		System.out.println("being concurrent");
		final SimAneal AnealArray[] = new SimAneal[10];
		Thread aneals[] = new Thread[10];
		for(int j=0; j<10; j++){
		
			aneals[j]= new Thread(new Runnable(){
				public void run(){
					int mythreadID= ThreadID.get();
					try {
						int correct=0;
						AnealArray[mythreadID] =new SimAneal(diff, num);
						while(correct!=81 && flag!=true){
							AnealArray[mythreadID].solve(a, min_t, temp_change, k, t);
							correct=AnealArray[mythreadID].countCorrect(AnealArray[mythreadID].getPuzzle());
							System.out.println(correct);
						}
						if(correct==81){
							flag=true;
							System.out.print("finished");
							AnealArray[mythreadID].printPuzzle();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
//			aneals[j].run();			
		}
		for(int k=0; k<10; k++){
			aneals[k].start();
		
		}
		for(int k=0; k<10; k++){
			try {
				aneals[k].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final double duration = System.nanoTime() - startTime;
		System.out.println("duration: " + (duration/1000000000) + " s");
	}
	
	
	public void sequential() throws IOException {
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
