package simualted_anealing;

import java.io.IOException;

public class AnnealingCaller {
	public static String diff = "simple";
	public static int num = 2;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		driver anneal= new driver(diff,num);
//		anneal.sequential();
		anneal.concurrent();
	}

}
