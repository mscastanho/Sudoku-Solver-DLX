import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MatrixInput {

	public static int[][] generateRestrictionsMatrix(int[][] a){

		int n = a.length;
		
		int b[][] = new int[n*n*n][4*n*n];
		
		//part one
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				
				//System.out.println(a[i][j]);
				
					for(int k = 0; k < n; k++)
					{
					
						if(a[i][j] != 0){
							
							if( a[i][j] == (k+1)) {
							
							b[(i*(n*n))+(j*n)+k][(i*n)+j] = 1;
							
							}
							else
							{
								b[(i*(n*n))+(j*n)+k][(i*n)+j] = 0;
							}
						}else 
						{
							
						b[(i*(n*n))+(j*n)+k][(i*n)+j] = 1;
						
							}
					}	
				
				
				
			}
			//System.out.println("\n");
		}
		
		//part two
		//focus on columns
		int col1 = n;
		for(int i = 0; i < n*n*n; i++ ) //rows
		{
         int shift = (i/(n*n))*(n);	
         //System.out.println(shift);
         int tranverse =  (((i+col1)%(n)));
        // System.out.println(tranverse+shift+(n*n));
         b[i][shift + tranverse + (n*n)] = 1;
		}
		//part three
		int col2 = 2*(n*n);
		for(int i = 0; i < n*n*n; i++)
		{
			
			b[i][((i+col2)%(n*n))+col2] = 1;
		}
		//part four
		/*int col3 = 3*(n*n);
		for(int i = 0; i < n*n*n; i++ ) //rows
		{
			
			int shift1 = (i%(n));
			int shift2 = (i/((n*n*n)/ (int) Math.sqrt(n)))*((int) Math.sqrt(n*n*n));
			int shift3 = ((i/ (int) Math.sqrt(n*n*n))% (int) Math.sqrt(n))*n;
			
			//int shift2 =  (i%( (int) Math.sqrt(n)));
			
			//System.out.println(shift1 + shift2 +shift3 + col3  );
			b[i][shift1 + shift2 +shift3 + col3 ] = 1;
		}*/
		
		int sizeSq = n*n;
		int size = n;
		int sqrtSize = (int) Math.sqrt(n);
		
		int startColumn = 3*sizeSq;
		int startRow = 0;
		
		for(int r = 0 ; r < size ; r++){
			
			int rDivSqrtSize = r/sqrtSize; //1
			int rModSqrtSize = r%sqrtSize; //1
			
			startRow = rDivSqrtSize*sizeSq*sqrtSize + rModSqrtSize*sqrtSize*size; // 1*16*2 + 1*2*4 = 
			
			fillRegion(startRow,startColumn,size,b);
			
			startColumn += size;
		}
		
		/*for(int i = 0; i < n*n*n; i++)
		{
			for(int j = 0; j < n*n*n; j++)
			{
				if(b[i][j] == 0){
					System.out.print(" ");
				}
				else {	
					System.out.print(b[i][j]); 
				}
				
				System.out.print(" ");
			}
			
			System.out.println("\n");
		}*/
		
		return b;
	}
	
	public static int[][] readInput() throws FileNotFoundException{
		File matrixfile = new File("matrixfile.txt");
		Scanner s = new Scanner(matrixfile);
		int n = s.nextInt();
		int a[][] = new int[n][n];
		//int r = s.nextInt();
		//int r = s.nextInt();
		//int i = n;
		//System.out.println(n + " " + r);
		s.nextLine();
		
		//s.next();*/
		//int n = l;
		
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				//System.out.println("hi");
				a[i][j] = s.nextInt();
			}
		}
		
		//print shit
//		for(int i = 0; i < n; i++)
//		{
//			for(int j = 0; j < n; j++)
//			{
//				System.out.print(a[i][j] + " "); 
//			}
//			System.out.println("\n");
//		}
		
		s.close();
		
		return a;
	}
	
	private static void fillRegion(int startLine, int startColumn, int size, int matrix[][]){
		
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
	
	public static List<int[][]> readSamuraiInput (){
		
		File inputFile;
		Scanner s;
		
		try{
			inputFile = new File("inputSamurai.txt");
			s = new Scanner(inputFile);
		}catch(FileNotFoundException fnfe){
			System.out.println("Adios muchachos...");
			return null;
		}
		
		int[][] center = new int[9][9];
		int[][] topLeft = new int[9][9];
		int[][] topRight = new int[9][9];
		int[][] bottomLeft = new int[9][9];
		int[][] bottomRight = new int[9][9];
		
		for(int i = 0 ; i < 9 ; i++){
			for(int j = 0 ; j < 0 ; j++){
				center[i][j] = 0;
				topLeft[i][j] = 0;
				topRight[i][j] = 0;
				bottomLeft[i][j] = 0;
				bottomRight [i][j] = 0;
			}
		}
		
		//Read first 6 lines
		for(int i = 0 ; i < 6 ; i++){
			for(int j = 0 ; j < 9 ; j++)
				topLeft[i][j] = s.nextInt();
			
			for(int k = 0 ; k < 9 ; k++)
				topRight[i][k] = s.nextInt();
		}
		
		//Read first 3 overlapping lines		
		
		for(int i = 0 ; i < 3 ; i++){
			
			int j,c,k;
			
			for(j = 0 ; j < 6 ; j++)
				topLeft[i + 6][j] = s.nextInt();
			
			j = 6;
			
			for(c = 0 ; c < 3 ; c++,j++){
				int next = s.nextInt();
				topLeft[i + 6][j] = next;
				center[i][c] = next;
			}
			
			for(c = 3 ; c < 6 ; c++)
				center[i][c] = s.nextInt();
			
			k = 0;
			
			for(c = 6 ; c < 9 ; c++,k++){
				int next = s.nextInt();
				center[i][c] = next;
				topRight[i + 6][k] = next;
			}
			
			for(k = 3 ; k < 9 ; k++)
				topRight[i + 6][k] = s.nextInt();
		}
		
		//Read 3 middle center rows
		
		for(int i = 3 ; i < 6 ; i++){
			for(int j = 0 ; j < 9 ; j++)
				center[i][j] = s.nextInt();
		}
		
		//Read bottom 3 overlapping rows
		for(int i = 0 ; i < 3 ; i++){
			int j,c,k;
			
			for(j = 0 ; j < 6 ; j++)
				bottomLeft[i][j] = s.nextInt();
			
			j = 6;
			
			for(c = 0 ; c < 3 ; c++,j++){
				int next = s.nextInt();
				bottomLeft[i][j] = next;
				center[i+6][c] = next;
			}
			
			for(c = 3 ; c < 6 ; c++)
				center[i+6][c] = s.nextInt();
			
			k = 0;
			
			for(c = 6 ; c < 9 ; c++,k++){
				int next = s.nextInt();
				center[i+6][c] = next;
				bottomRight[i][k] = next;
			}
			
			for(k = 3 ; k < 9 ; k++)
				bottomRight[i][k] = s.nextInt();	
		}
		
		//Read last 6 lines
		for(int i = 3 ; i < 9 ; i++){
			for(int j = 0 ; j < 9 ; j++)
				bottomLeft[i][j] = s.nextInt();
					
			for(int k = 0 ; k < 9 ; k++)
				bottomRight[i][k] = s.nextInt();
		}
		
		List<int[][]> list = new ArrayList<int[][]>();
		
		list.add(topLeft);
		list.add(topRight);
		list.add(center);
		list.add(bottomLeft);
		list.add(bottomRight);
		
		//SamuraiSudokuSolver.generateExtraRestrictions(6, 8, 0, 8, topRight);
		
		//printMatrixListToFile(list,"output.txt");
		
		s.close();
		
		return list;
		
	}
	
	public static void printMatrixListToFile(List<int[][]> list, String fileName){
		PrintWriter writer;
		try{
			writer = new PrintWriter(fileName,"UTF-8");
		}catch(Exception e){
			System.out.println("Problem");
			return;
		}
				
		for(int[][] matrix : list){

			for(int i = 0 ; i < matrix.length ; i++){
				
				for(int j = 0 ; j < matrix[0].length ; j++){
					writer.print(matrix[i][j] + " ");
				}
				writer.println();
			}
			
			writer.println("\n");
		}
		
		writer.close();
	}
	
	

	
}
