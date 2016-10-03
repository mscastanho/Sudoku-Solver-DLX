import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SamuraiSudokuSolver {

	int[][] topLeft;
	int[][] topRight;
	int[][] center;
	int[][] bottomLeft;
	int[][] bottomRight;

	public SamuraiSudokuSolver(int[][] topLeft, int[][] topRight,
			int[][] center, int[][] bottomLeft, int[][] bottomRight) {

		this.topLeft = topLeft;
		this.topRight = topRight;
		this.center = center;
		this.bottomLeft = bottomLeft;
		this.bottomRight = bottomRight;
	}
	
	public void solve(){
				
		//Solving Center Board
		
		int[][] cRestrictions = MatrixInput.generateRestrictionsMatrix(center);
		
		//Top Left restrictions
		//System.out.println("TopLeft Vertical");
		findAndApplyExtraRestrictions(-6,-6,new Tuple(0,6),new Tuple(5,8),new Tuple(0,0),topLeft,center,cRestrictions,Direction.Vertical);
		
		//System.out.println("TopLeft Horizontal");
		findAndApplyExtraRestrictions(-6,-6,new Tuple(6,0),new Tuple(8,5),new Tuple(0,0),topLeft,center,cRestrictions,Direction.Horizontal);
		
		//Top Right restrictions
		//System.out.println("TopRight Vertical");
		findAndApplyExtraRestrictions(-6,6,new Tuple(0,0),new Tuple (5,2),new Tuple(0,6),topRight,center,cRestrictions,Direction.Vertical);
		
		//System.out.println("TopRight Horizontal");
		findAndApplyExtraRestrictions(-6,6,new Tuple(6,3),new Tuple(8,8),new Tuple(0,6),topRight,center,cRestrictions,Direction.Horizontal);
		
		//Bottom Left restrictions
		//System.out.println("BottomLeft Vertical");
		findAndApplyExtraRestrictions(6,0,new Tuple(3,6),new Tuple(8,8),new Tuple(6,0),bottomLeft,center,cRestrictions,Direction.Vertical);
		
		//System.out.println("BottomLeft Horizontal");
		findAndApplyExtraRestrictions(6,0,new Tuple(0,0),new Tuple(2,5),new Tuple(6,0),bottomLeft,center,cRestrictions,Direction.Horizontal);
		
		//Bottom Right restrictions
		//System.out.println("BottomRight Vertical");
		findAndApplyExtraRestrictions(6,6,new Tuple(3,0),new Tuple(8,2),new Tuple(6,6),bottomRight,center,cRestrictions,Direction.Vertical);
		
		//System.out.println("BottomRight Horizontal");
		findAndApplyExtraRestrictions(6,6,new Tuple(0,3),new Tuple(2,8),new Tuple(6,6),bottomRight,center,cRestrictions,Direction.Horizontal);
		
		List<Integer> solution = DancingLinks.solveExactCover(9,cRestrictions);
		List<SudokuValue> trAnswer = SudokuSolver.getSudokuAnswers(solution,9);
		//System.out.println("size of solution: " + trAnswer.size() );
		
		for(SudokuValue sv : trAnswer){
			//System.out.println(sv);
			int i = sv.position.x;
			int j = sv.position.y;
			int value = sv.value;
			
			center[i][j] = value;		
		}
		
		
		
		//Copy solved corners to the other boards
		for(int i = 0 ; i < 3 ; i++){
			for(int j = 0 ; j < 3 ; j++)
				topLeft[i+6][j+6] = center[i][j];
		}
		
		for(int i = 0 ; i < 3 ; i++){
			for(int j = 6 ; j < 9 ; j++)
				topRight[i+6][j-6] = center[i][j];
		}
		
		for(int i = 6 ; i < 9 ; i++){
			for(int j = 0 ; j < 3 ; j++)
				bottomLeft[i-6][j+6] = center[i][j];
		}
		
		for(int i = 6 ; i < 9 ; i++){
			for(int j = 6 ; j < 9 ; j++)
				bottomRight[i-6][j-6] = center[i][j];
		}
		
		SudokuSolver tlSolver = new SudokuSolver(9,SudokuType.NORMAL,topLeft);
		tlSolver.solve();
		//tlSolver.printSolution();
		
		SudokuSolver trSolver = new SudokuSolver(9,SudokuType.NORMAL,topRight);
		trSolver.solve();
		//trSolver.printSolution();
		
		SudokuSolver blSolver = new SudokuSolver(9,SudokuType.NORMAL,bottomLeft);
		blSolver.solve();
		//blSolver.printSolution();
		
		SudokuSolver brSolver = new SudokuSolver(9,SudokuType.NORMAL,bottomRight);
		brSolver.solve();
		//brSolver.printSolution();
		
		
		
		// Bottom Left
		
		
		
		List<int[][]> list = new ArrayList<int[][]>();
		list.add(topLeft);
		list.add(topRight);
		list.add(center);
		list.add(bottomLeft);
		list.add(bottomRight);
		
		MatrixInput.printMatrixListToFile(list, "DLXMatrix.txt");
	}
	
	public static void findAndApplyExtraRestrictions(int rowOffset, int columnOffset, Tuple restrictionStart, Tuple restrictionEnd, Tuple regionInit, int[][] extraRestBoard, int[][] board, int[][] restMatrix, Direction direction){
		
		//System.out.println("Here starts top Right");
		
		int startRow = restrictionStart.x;
		int endRow = restrictionEnd.x;
		
		int startColumn = restrictionStart.y;
		int endColumn = restrictionEnd.y;
		
		
		List<SudokuValue> extraRestrictions = generateExtraRestrictions(startRow,endRow,startColumn,endColumn,extraRestBoard);
		List<SudokuValue> adjustedExtraRestrictions = adjustExtraRestrictions(rowOffset,columnOffset,regionInit,extraRestrictions,direction);
		
		applyExtraRestrictions(adjustedExtraRestrictions,restMatrix);
		
		/*List<Integer> solution = DancingLinks.solveExactCover(9,restMatrix);
		List<SudokuValue> trAnswer = SudokuSolver.getSudokuAnswers(solution,9);
		
		for(SudokuValue sv : trAnswer){
			//System.out.println(sv);
			int i = sv.position.x;
			int j = sv.position.y;
			int value = sv.value;
			
			board[i][j] = value;		
		}*/
	}

	// This method generates a list of restrictions based on a section
	// of a certain input matrix
	public static List<SudokuValue> generateExtraRestrictions(int startRow,
			int endRow, int startColumn, int endColumn, int[][] basedOn) {

		List<SudokuValue> restrictions = new ArrayList<SudokuValue>();
		
		if(endRow - startRow > endColumn - startColumn){
			for (int i = startRow; i <= endRow; i++) {
				for (int j = startColumn; j <= endColumn; j++) {
	
					if (basedOn[i][j] != 0) {
						SudokuValue sv = new SudokuValue(basedOn[i][j], new Tuple(
								i, j));
						restrictions.add(sv);
					}
				}
			}
		}else{
			for (int i = startRow; i <= endRow; i++) {
				for (int j = startColumn; j <= endColumn; j++) {
	
					if (basedOn[i][j] != 0) {
						SudokuValue sv = new SudokuValue(basedOn[i][j], new Tuple(
								i, j));
						restrictions.add(sv);
					}
				}
			}
		}

		
		/*for (SudokuValue sv : restrictions)
			System.out.println(sv);
		
		System.out.println();*/

		return restrictions;
	}
	
	public static int valueToRow(SudokuValue sv, int size){
		
		Tuple position = sv.position;
		int value = sv.value;
		
		int sizeSq = size*size;
		
		return position.x*sizeSq + position.y*size + (value-1);
	}
	
	public static int valueToColumn(SudokuValue sv, int size){
		Tuple position = sv.position;
		
		return position.x*size + position.y;
	}
	
	public static List<SudokuValue> adjustExtraRestrictions(int rowOffset, int columnOffset, Tuple regionInit, List<SudokuValue> restrictions, Direction direction){
		List<SudokuValue> adjustedRestrictions = new ArrayList<SudokuValue>();
		
		for(SudokuValue sv : restrictions){
			sv.position.x += rowOffset;
			sv.position.y += columnOffset;
		}
		
		switch(direction){
			case Vertical:
				for(SudokuValue sv : restrictions){
					for(int i = regionInit.x ; i < regionInit.x + 3 ; i++){
						adjustedRestrictions.add(new SudokuValue(sv.value,new Tuple(i,sv.position.y)));
					}
				}
				break;
			case Horizontal:
				for(SudokuValue sv : restrictions){
					for(int j = regionInit.y ; j < regionInit.y + 3 ; j++){
						adjustedRestrictions.add(new SudokuValue(sv.value,new Tuple(sv.position.x,j)));
					}
				}
				break;
		}
		
		/*for (SudokuValue sv : adjustedRestrictions)
			System.out.println(sv);
		*/
		//System.out.println();
		
		return adjustedRestrictions;	
	}

	public static void applyExtraRestrictions(List<SudokuValue> restrictions,
			int[][] restrictionsMatrix) {
			
		for(SudokuValue sv : restrictions){
			int i = valueToRow(sv,9);
			int j = valueToColumn(sv,9);
			
			restrictionsMatrix[i][j] = 0;
		}
	}
	
	public void printSolutionToFile(String fileName){
		File outputFile;
		PrintWriter writer;
		
		try{
			outputFile = new File(fileName);
			writer = new PrintWriter(outputFile);
		}catch(FileNotFoundException fnfe){
			System.out.println("Adios muchachos...");
			return;
		}
		
		//Read first 6 lines
		for(int i = 0 ; i < 6 ; i++){
			for(int j = 0 ; j < 9 ; j++)
				writer.print(topLeft[i][j] + " ");
			
				writer.print("      ");
				
			for(int k = 0 ; k < 9 ; k++)
				writer.print(topRight[i][k] + " ");
			
			writer.println();
		}
		
		//Read first 3 overlapping lines		
		
		for(int i = 0 ; i < 3 ; i++){
			
			int j,c,k;
			
			for(j = 0 ; j < 6 ; j++)
				writer.print(topLeft[i + 6][j] + " ");
			
			j = 6;
			
			for(c = 0 ; c < 3 ; c++,j++){
				//int next = s.nextInt();
				//writer.print(topLeft[i + 6][j] + " ");
				writer.print(center[i][c] + " ");
			}
			
			for(c = 3 ; c < 6 ; c++)
				writer.print(center[i][c] + " ");
			
			k = 0;
			
			for(c = 6 ; c < 9 ; c++,k++){
				//int next = s.nextInt();
				//writer.print(center[i][c] + " ");
				writer.print(topRight[i + 6][k] + " ");
			}
			
			for(k = 3 ; k < 9 ; k++)
				writer.print(topRight[i + 6][k] + " ");
			
			writer.println();
		}
		
		//Read 3 middle center rows
		
		for(int i = 3 ; i < 6 ; i++){
			writer.print("            ");
			for(int j = 0 ; j < 9 ; j++)
				writer.print(center[i][j]  + " ");
			
			writer.println();
		}
		
		
		
		//Read bottom 3 overlapping rows
		for(int i = 0 ; i < 3 ; i++){
			int j,c,k;
			
			for(j = 0 ; j < 6 ; j++)
				writer.print(bottomLeft[i][j] + " ");
			
			j = 6;
			
			for(c = 0 ; c < 3 ; c++,j++){
				//int next = s.nextInt();
				//writer.print(bottomLeft[i][j]);
				writer.print(center[i+6][c] + " ");
			}
			
			for(c = 3 ; c < 6 ; c++)
				writer.print(center[i+6][c] + " ");
			
			k = 0;
			
			for(c = 6 ; c < 9 ; c++,k++){
				//int next = s.nextInt();
				//writer.print(center[i+6][c]);
				writer.print(bottomRight[i][k] + " ");
			}
			
			for(k = 3 ; k < 9 ; k++)
				writer.print(bottomRight[i][k] + " ");	
			
			writer.println();
		}
		
		//Read last 6 lines
		for(int i = 3 ; i < 9 ; i++){
			for(int j = 0 ; j < 9 ; j++)
				writer.print(bottomLeft[i][j] + " ");
			
			writer.print("      ");
					
			for(int k = 0 ; k < 9 ; k++)
				writer.print(bottomRight[i][k] + " ");
			
			writer.println();
		}
		
		/*List<int[][]> list = new ArrayList<int[][]>();
		
		list.add(topLeft);
		list.add(topRight);
		list.add(center);
		list.add(bottomLeft);
		list.add(bottomRight);
		*/
		//SamuraiSudokuSolver.generateExtraRestrictions(6, 8, 0, 8, topRight);
		
		//printMatrixListToFile(list,"output.txt");
		
		
		writer.close();
	}

}
