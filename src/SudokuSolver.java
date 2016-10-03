import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SudokuSolver {
	
	int size;
	SudokuType type;
	int[][] board;
	
	public SudokuSolver (int size, SudokuType type, int[][] board){
		this.size = size;
		this.type = type;
		this.board = board;
	}
	
	public void solve (){
		
		//generateRestrictionsMatrix
		//Alter rest matrix based on input
		//Do the next step based on the altered rest matrix
		
		int[][] restrictionsMatrix = MatrixInput.generateRestrictionsMatrix(board);
		
		/*for(int i = 0 ; i < restrictionsMatrix.length ; i++){
			for(int j = 0 ; j < restrictionsMatrix[0].length ; j++)
				System.out.print(restrictionsMatrix[i][j] + " ");
			
			System.out.println();
		}*/
		
		List<Integer> ecSolution = DancingLinks.solveExactCover(size,restrictionsMatrix);
		
		List<SudokuValue> answers = getSudokuAnswers(ecSolution,this.size);
		
		//System.out.println("This is the size of the answer: " + ecSolution.size());
		
		for(SudokuValue sv : answers){
			//System.out.println(sv);
			int i = sv.position.x;
			int j = sv.position.y;
			int value = sv.value;
			
			this.board[i][j] = value;		
		}
	}
	
	public static List<SudokuValue> getSudokuAnswers (List<Integer> solution, int size){	
		List<SudokuValue> answers = new ArrayList<SudokuValue>();
		
		for(Integer n : solution){
			int row = n.intValue();
			answers.add(rowToValue(row,size));
		}
		
		return answers;
	}
	
	public void printSolution (){
		for(int i = 0 ; i < size ; i++){
			for(int j = 0 ; j < size ; j++){
				if(board[i][j] != 0 )
					System.out.print(" " + board[i][j]);
				else
					System.out.print("  ");
			}
			System.out.println();
		}
	}
	
	private static SudokuValue rowToValue (int row, int size){
		
		int value = row%size + 1;
		
		//This is incremented by 1 every n^2
		int indexRow = row/(size*size);
		
		//This is incremented circularly every n
		int indexColumn = (row/size)%size;
		
		return new SudokuValue(value,new Tuple(indexRow,indexColumn));
	}
	
	public void printRestrictionsMatrixToFile(){
		int[][] rM = MatrixInput.generateRestrictionsMatrix(board);
		
		PrintWriter writer;
		
		try{
			writer = new PrintWriter("output.txt","UTF-8");
		}catch(Exception e){
			System.out.println("Problem");
			return;
		}
		
		//writer.print(" ");
		
		//for(int i = 0 ; i < rM.length ; i++)
			//writer.printf("%3d ", i+1);
		
		//writer.println();
		
		for(int i = 0 ; i < rM.length ; i++){
			//writer.printf("%3d", i+1);
			
			for(int j = 0 ; j < rM[0].length ; j++){
				char c;
				if(rM[i][j] == 1)
					c = '1';
				else
					c = ' ';
				writer.print(c + "");
			}
			writer.println();
		}
		//System.out.println((System.nanoTime() - time)/1000000000.0);

		writer.close();
	}
	
	public int[][] generateRestrictionsMatrix(){
		
		int m = size*size*size;
		int sizeSq = size*size;
		int sqrtSize = (int) Math.sqrt(size);
		
		int[][] matrix = new int[m][m];
		
		//Fill matrix with zeros in the beginning
		for(int i = 0 ; i < m ; i++){
			for(int j = 0 ; j < m ; j++)
				matrix[i][j] = 0;
		}
		
		//Fill section of number restriction
		int previousLine = 0;
		
		for(int j = 0 ; j < size*size ; j++){
			int i;
			
			for(i = previousLine ; i < previousLine + size ; i++)
				matrix[i][j] = 1;

			previousLine = i;
		}
		
		//Fill section of row restriction
		previousLine = 0;
	
		for(int l = sizeSq ; l < 2*sizeSq ; l += size){
			for(int k = 0 ; k < size ; k++){
				int i,j;
				for(i = previousLine, j = l ; i < previousLine + size ; i++,j++){
						matrix[i][j] = 1;
				}
				
				previousLine = i;
			}
		}
		
		//Fill section of column restriction
		
		previousLine = 0;
		
		for(int k = 0 ; k < size ; k++){
			int i,j;
			for(i = previousLine, j = 2*sizeSq ; i < previousLine + sizeSq ; i++,j++){
					matrix[i][j] = 1;
			}
			
			previousLine = i;
		}
		
		//Fill section of region restriction
		
		int startColumn = 3*sizeSq;
		int startRow = 0;
		
		for(int r = 0 ; r < size ; r++){
			
			int rDivSqrtSize = r/sqrtSize; //1
			int rModSqrtSize = r%sqrtSize; //1
			
			startRow = rDivSqrtSize*sizeSq*sqrtSize + rModSqrtSize*sqrtSize*size; // 1*16*2 + 1*2*4 = 
			
			fillRegion(startRow,startColumn,matrix);
			
			startColumn += size;
		}
		
		
		
		return matrix;
	}
	
	private void fillRegion(int startLine, int startColumn, int matrix[][]){
		
		int row = startLine;
		int column = startColumn;
		int sqrtSize = (int) Math.sqrt(size);
		
		for(int k = 0 ; k < sqrtSize ; k++){
			
			for(int counter = 0; counter < sqrtSize ; counter++){
				column = startColumn;
				
				for(int l = 0 ; l < size ; l++){
					matrix[row][column] = 1;
					row++;
					column++;
				}
			}
			
			row += (size - sqrtSize)*size;
		}
	}
	
}
