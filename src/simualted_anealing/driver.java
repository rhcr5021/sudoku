package simualted_anealing;

import generator.Cell;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

//import backtracking.ThreadID;

public class driver {
	public static SimAneal sud;
	public String diff;
	public int num;
	public static int winner;
	public static double duration;
	public static Thread[] aneals;
	public int num_threads;
	public static LogAnealData log;
	SimAneal[] AnealArray;
	AtomicInteger c = new AtomicInteger(0);
	
	//anealing parameters
	static double a=.99;
	static double min_t=.00001;
	static int temp_change = 100;
	static double k=1;
	static double t=1;
	public static AtomicBoolean flag;
	/**
	 * @param threads 
	 * @param args
	 * @throws IOException 
	 */
	
	public driver(String diff, int num, int threads){
		this.diff=diff;
		this.num=num;
		driver.flag = new AtomicBoolean(false);
		this.num_threads = threads;
	}
	
	public boolean concurrent() throws IOException{
		final long startTime = System.nanoTime();
		System.out.println("being concurrent");
		final SimAneal AnealArray[] = new SimAneal[num_threads];
		aneals = new Thread[num_threads];
		for(int j=0; j<num_threads; j++){
		
			aneals[j]= new Thread(new Runnable(){
				public void run(){
					int mythreadID= ThreadID.get();
					try {
						int correct=0;
						AnealArray[mythreadID] =new SimAneal(diff, num);
						while(correct!=81 && flag.get() != true){
							c.getAndIncrement();
							AnealArray[mythreadID].solve(a, min_t, temp_change, k, t);
							correct=AnealArray[mythreadID].countCorrect(AnealArray[mythreadID].getPuzzle());
							System.out.println(correct + ": " + AnealArray[mythreadID].countErrors());
						}
						if(correct==81){
							flag.set(true);
							System.out.print("finished");
							winner = mythreadID;
							duration = System.nanoTime() - startTime;

						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});		
		}
		for(int k=0; k<num_threads; k++){
			aneals[k].start();
		
		}
		for(int k=0; k<num_threads; k++){
			try {
				aneals[k].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Errors in Puzzle: " + AnealArray[winner].countErrors());
		System.out.println("Puzzle is Valid: " + AnealArray[winner].isValid());
		System.out.println("Correct Indices: " + AnealArray[winner].countCorrect(AnealArray[winner].getPuzzle()));
		System.out.println("is Solution?: " + AnealArray[winner].isSol());
		System.out.println("Aneals: " + c.get());
		AnealArray[winner].printPuzzle();
		System.out.println("duration: " + (duration/1000000000) + " s");
		return AnealArray[winner].isSol();
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
