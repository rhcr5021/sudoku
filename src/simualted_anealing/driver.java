package simualted_anealing;

import java.io.IOException;

public class driver {
	public static SimAneal sud;
	public static String diff = "easy";
	public static int num = 10;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		sud = new SimAneal(diff,num);
		sud.aneal();
	}

}
