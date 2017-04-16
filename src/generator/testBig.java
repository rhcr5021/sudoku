package generator;

import java.io.IOException;

public class testBig {

	public static int zeros = 0;
	public static int succ = 0;
	public static int num = 100;
	public static int tot = 100;
	public static int min = Integer.MAX_VALUE;
	public static BigSudoku big;
	public static Thread[] swaps;
	
	public static void main(String[] args) throws IOException {
		final long startTime = System.nanoTime();
		//make generator
		big = new BigSudoku("puzzles/puz_81_81_ex.csv");
		big.printBigPuzzle();

		big.fillNeedsBig();
		big.printBigPuzzle();
		System.out.println("errors: " + big.countErrorsBig());
		//Generator generator = new Generator();
		swaps = new Thread[num];
		for (int j = 0; j < num; j++)
		{
			swaps[j] = new Thread(new Runnable(){
				public void run() {
					for(int i = 0; i < tot; i++)
					{
						if(big.chooseandSwapBig())
						{
							succ++;
						}
						//while(!sud.chooseandSwap());
						int m = big.countErrorsBig();
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
		big.printBigPuzzle();
		//count errors
		System.out.println("errors: " + big.countErrorsBig());	
		System.out.println("successful swaps: " + succ);	
		System.out.println("min errors: " + min);
		final double duration = System.nanoTime() - startTime;
		System.out.println("duration: " + (duration/1000000000) + " s");
	}

}
