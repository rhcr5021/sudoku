package simualted_anealing;

import java.io.IOException;

public class AnnealingCaller {
	public static String diff = "expert";
	public static int num = 2;
	public static int threads = 10;
	public static void main(String[] args) throws IOException {
		driver anneal = new driver(diff,num,threads);
		//anneal.concurrent_pool();
//		anneal.sequential();
		System.out.println(anneal.concurrent());
	}

}
