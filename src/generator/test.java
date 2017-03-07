package generator;

public class test {
	//public static int puzzle[][];
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Generator generator = new Generator();
		int[][] puz = generator.intPuzzleGen(42);
		generator.printPuzzle(puz);
	}
}
