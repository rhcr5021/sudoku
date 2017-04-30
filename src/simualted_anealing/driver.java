package simualted_anealing;

import generator.BoundedQueue;
import generator.Cell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

//import backtracking.ThreadID;

public class driver {
	public static SimAneal sud;
	public String diff;
	public int num;
	public int size = 10;
	public static int winner;
	private BoundedQueue<Aneal> anealers = new BoundedQueue<Aneal>(20);
	private List<Aneal> aneal_list = new ArrayList<Aneal>();
	public static double duration;
	public static Thread[] aneals;
	private List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
	public int num_threads;
	SimAneal test;
	public static LogAnealData log;
	SimAneal[] AnealArray;
	private ExecutorService service = Executors.newCachedThreadPool();
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
	
	private class Aneal implements Callable<Integer>
	{
		public Integer call() throws Exception {
			return test.solve(a, min_t, temp_change, k, t)[0];
		}
	}
	
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
	
	public boolean concurrent_pool() throws IOException{
		final long startTime = System.nanoTime();
		test = new SimAneal(diff, num);
		System.out.println("being concurrent");
		Aneal l = new Aneal();
		for (int i = 0; i < anealers.getCapacity(); i++)
		{
			anealers.enq(l);
			aneal_list.add(l);
			service.submit(l);
		}
		int err = Integer.MAX_VALUE;
		while(true)
		{
			try {
				err = service.invokeAny(aneal_list);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (err == 0)
			{
				System.out.println("Errors in Puzzle: " + test.countErrors());
				System.out.println("Puzzle is Valid: " + test.isValid());
				System.out.println("Correct Indices: " + test.countCorrect(AnealArray[winner].getPuzzle()));
				System.out.println("is Solution?: " + test.isSol());
				test.printPuzzle();
				service.shutdown();
				System.out.println("duration: " + (duration/1000000000) + " s");
				return true;
			}
			anealers.deq();
			while(anealers.getSize() < 20)
			{
				anealers.enq(l);
				service.submit(l);
			}
			System.out.println(err);
			service.submit(new Aneal());
		}
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
