package backtracking;

import generator.Cell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ConcurrentBacktracking extends Backtracking {
	int num_threads;
	private ExecutorService service = Executors.newCachedThreadPool();
	private List<TryNew> first_triers = new ArrayList<TryNew>();

	public ConcurrentBacktracking(String diff, int t, boolean big)
			throws IOException {
		super(diff, t, big);
		// TODO Auto-generated constructor stub
	}

	public ConcurrentBacktracking(String diff, int n, int t) throws IOException {
		super(diff, n, t);
		// TODO Auto-generated constructor stub
	}
	
	private class TryNew implements Callable<Cell[][]>
	{
		int t;
		int r; int c;
		Cell[][] puz;
		public TryNew(Cell[][] puzzle, int i, int row, int col)
		{
			t = i;
			c = col;
			r = row;
			puz = puzzle;
		}
		public Cell[][] call() throws Exception {
			puz[c][r].setVal(t);
			int[] spot = findEmptyCell(puz);
			int row = spot[0], col = spot[1];
			return solve(puz);
				
//			System.out.pRINTLN(COUNTERRORS(PUZ));
//			RETURN NULL;
		}
	}
	
	public Cell[][] solveConcurrent2() throws ExecutionException
	{
	{
			int[] spot = findEmptyCell(puzzle);
			int row = spot[0], col = spot[1];
			for (int g = 0; g < 9; g++)
			{
				first_triers.add(new TryNew(puzzle,g,row,col));
				service.submit(first_triers.get(g));
			}
			try {
				List<Future<Cell[][]>> f = service.invokeAll(first_triers);
				for(int j=0; j<9; j++){
					Cell[][] tempsol=f.get(j).get();
					System.out.println("future " + j + " got");
					if(tempsol==null){
						System.out.println("null");
					}else{						
						printInputedPuzzle(f.get(j).get());
					}
					if(f.get(j).get()!=null){
						return f.get(j).get();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
	}
	
	public Cell[][] solve(Cell[][] puzzle){
		int[] spot = findEmptyCell(puzzle);
		if (spot == null)
		{
			return puzzle;
		}
		int row = spot[0], col = spot[1];
		for (int g = 9; g > 0; g--)
		{
			if(checkCell(puzzle, row, col, g))
			{
				int temp = puzzle[row][col].getVal();
				puzzle[row][col].setVal(g);
				Cell[][] tsol=solve(puzzle);
				if(tsol!=null)
				{
					System.out.println("sucess");
					return tsol;
				}
				puzzle[row][col].setVal(temp);
			}
		}
//		printInputedPuzzle(puzzle);
		return null;
	}
	public int[] findEmptyCell(Cell[][] puz)
	{
		int[] empty = new int[2];
		for(int i = 0; i < 9;i++)
		{
			for(int j = 0; j < 9; j++)
			{
				if(puz[i][j].getVal() == 0)
				{
					empty[0] = i;
					empty[1] = j;
					return empty;
				}
			}
		}
		return null;
	}

	
	public boolean checkCell(Cell[][] puz, int row, int col, int guess)
	{
		int temp = puz[row][col].getVal();
		puz[row][col].setVal(guess);
		if(countErrors() > 0)
		{
			puz[row][col].setVal(temp);
			return false;
		}
		puz[row][col].setVal(temp);
		return true;
		
	}
}
