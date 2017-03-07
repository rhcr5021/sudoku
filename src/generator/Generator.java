package generator;

import java.util.Random;

/**
 * A Generator to generate random Sudoku {@link Grid} instances.
 */
public class Generator {
  private Solver solver;

  /**
   * Constructs a new Generator instance.
   */
  public Generator() {
    this.solver = new Solver();
  }

  /**
   * Generates a random {@link Grid} instance with the given number of empty {@link Grid.Cell}s.
   * <br><br>
   * Note: The complexity for a human player increases with an higher amount of empty {@link Grid.Cell}s.
   * @param numberOfEmptyCells the number of empty {@link Grid.Cell}s
   * @return a randomly filled Sudoku {@link Grid} with the given number of empty {@link Grid.Cell}s
   */
  public Grid generate(int numberOfEmptyCells) {
    Grid grid = generate();

    eraseCells(grid, numberOfEmptyCells);

    return grid;
  }
  
  public int countEmpty(int[][] puzzle)
  {
	  int i,j,count = 0;
	  for ( i = 0; i < 9; i++)
		{
			for ( j = 0; j < 9; j++)
			{
				if(puzzle[i][j] == 0)
					count++;
			}
		}
	  return count;
  }
  
  public void printPuzzle(int[][] puzzle)
  {
	  int i,j;
	  for ( i = 0; i < 9; i++)
		{
			for ( j = 0; j < 9; j++)
			{
				System.out.print(puzzle[i][j] + " ");
			}
			if (i % 3 == 2)
			{
				System.out.print("\n-----------------");
			}
			System.out.println();
		}
  }
  public int[][] intPuzzleGen(int num)
  {
	// TODO Auto-generated method stub
	Generator generator = new Generator();
	Grid grid = generator.generate(num);
	int[][] puzzle = new int[9][9];
	for (int i = 0; i < 9; i++)
	{
		for (int j = 0; j < 9; j++)
		{
			puzzle[i][j] = grid.grid[i][j].getValue();
		}
	}
	return puzzle;
  }
  private void eraseCells(Grid grid, int numberOfEmptyCells) {
    Random random = new Random();
    for (int i = 0; i < numberOfEmptyCells; i++) {
      int randomRow = random.nextInt(9);
      int randomColumn = random.nextInt(9);

      Grid.Cell cell = grid.getCell(randomRow, randomColumn);
      if (!cell.isEmpty()) {
        cell.setValue(0);
      } else {
        i--;
      }
    }
}
    
private Grid generate() {
    Grid grid = Grid.emptyGrid();

    solver.solve(grid);

    return grid;
  }
}


