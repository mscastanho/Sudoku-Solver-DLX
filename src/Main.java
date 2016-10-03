import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Main {

	
	List<SudokuValue> answers = new ArrayList<SudokuValue>();
	
	public static void main(String[] args) {
		
		int[][] sudoku;
		
		try{
			sudoku = MatrixInput.readInput(); 
		}catch(FileNotFoundException e){
			sudoku = null;
			System.out.println("FAIL!");
			return;
		}
	
		System.out.println("This is the Sudoku we want to solve:");
		
		for(int i = 0 ; i < sudoku.length ; i++){
			for(int j = 0 ; j < sudoku[i].length ; j++){
				if(sudoku[i][j] != 0 )
					System.out.print(" " + sudoku[i][j]);
				else
					System.out.print(" _");
			}
			System.out.println();
		}
		
		System.out.println("\nSolution:");
		
		if(sudoku != null){
			SudokuSolver solver = new SudokuSolver(sudoku.length,SudokuType.NORMAL,sudoku);
			long time = System.nanoTime();
			solver.solve();
			solver.printRestrictionsMatrixToFile();
			solver.printSolution();
			System.out.println("\n The algorithm took " + (System.nanoTime() - time)/1000000000 + " seconds");
		}/*
		
		List<int[][]> boards = MatrixInput.readSamuraiInput();
		int[][] topLeft = boards.get(0);
		int[][] topRight = boards.get(1);
		int[][] center = boards.get(2);
		int[][] bottomLeft = boards.get(3);
		int[][] bottomRight = boards.get(4);
		
		SamuraiSudokuSolver ssSolver = new SamuraiSudokuSolver(topLeft,topRight,center,bottomLeft,bottomRight);
		ssSolver.solve();
		ssSolver.printSolutionToFile("output.txt");*/
	}
}
