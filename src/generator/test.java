package generator;

public class test {
	static int zeros = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Generator generator = new Generator();
		int[][] puz = generator.intPuzzleGen(zeros);
		generator.printPuzzle(puz);
		System.out.println(generator.countEmpty(puz));
		System.out.println(countErrors(puz));
	}
	public static int countErrors(int[][] puz)
	{
		int row = 0, col = 0, box = 0;
		for (int i = 0; i < 9; i++)
		{
			row += checkRow(getRow(puz,i));
			col += checkCol(getCol(puz,i));
			box += checkBox(getBox(puz,i));
		}
		return (row+box+col);
	}
	private static int checkRow(int row[]) {
		
		return 0;
	}
	private static int checkBox(int[][] box) {
		
		return 0;
	}
	private static int checkCol(int[] col) {
		
		return 0;
	}
	private static int[][] getBox(int[][] puz, int i) {
		//use index i to get col and row indexes for the box
		/*  _____
		 * |1|2|3|
		 * |4|5|6|
		 * |7|8|9|
		 */
		int row = 0,col = 0;
		//get row and col with case statement, external method, or expression
		int box[][] = new int[3][3];
		for (int j = row; j < row+3;j++)
		{
			for (int k = col; k < col+3;k++)
			{
				box[j-row][k-col] = puz[j][k];
				
			}
		}
		return puz;
	}
	private static int [] getCol(int[][] puz, int i) {
		int [] col = new int[9];
		for (int j = 0; j < 9; j++)
		{
			col[j] = puz[j][i];
		}
		return col;
	}
	private static int[] getRow(int[][] puz, int i) {
		int [] row = new int[9];
		for (int j = 0; j < 9; j++)
		{
			row[j] = puz[i][j];
		}
		return row;
	}

}
