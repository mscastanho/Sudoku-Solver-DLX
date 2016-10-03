import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DancingLinks {
	
	DLXColumn h;
	List<DLXNode> answer;
	int sudokuSize;
	
	public DancingLinks (int sudokuSize, int[][] matrix){
		this.h = createDLXMatrix(matrix);
		this.answer = new ArrayList<DLXNode>();
		this.sudokuSize = sudokuSize; 
	}
	
	public static DLXColumn createDLXMatrix (int[][] m){
		
		//**First, create the main header and all columns
		
		//HashMap to quickly access the created columns during initialization
		//of other nodes
		Map<Integer,DLXColumn> columns = new HashMap<Integer,DLXColumn>();
		
		DLXColumn h = new DLXColumn(0);
		DLXColumn previous = h;
		h.m = m.length;			//Save number of rows in the matrix
		h.n = m[0].length;		//Save number of columns in the matrix
		
		for(int i = 0 ; i < m[0].length ; i++){
			DLXColumn c = new DLXColumn(i+1);
			
			previous.linkRight(c);
			previous = c;
			
			//If the column is empty, the column header
			//is connected to itself up and down
			c.linkDown(c);
			
			columns.put(c.id,c);
		}
		
		//Last element has to link back to h (circular list)
		previous.linkRight(h);
		
		//***********************
		
		//Create all the nodes, linking them horizontally and vertically	
		
		for(int i = 0 ; i < m.length ; i++){
			
			DLXNode left = null;
			
			for(int j = 0 ; j < m[i].length ; j++){
				
				DLXColumn c = columns.get(j+1);
				
				if(m[i][j] != 0){
					DLXNode n = new DLXNode();
					
					if(left != null){
						left.linkRight(n);
					}
					
					if(c.up != null){
						c.up.linkDown(n);
					}else{
						n.linkUp(c);
					}
					
					n.linkDown(c);
					n.linkColumn(c);
				
					left = n;
					
					n.position = new Tuple (i,j);
				}
			}
			
			// Need to find the first node in this row
			// make the "wrap around link", to make a horizontal circular list
			
			if(left != null){
				
				if(left.left == null)
					left.linkRight(left);
				else{
					DLXNode temp = left;
						
					while(true){
						if(temp.left == null)
							break;
						else
							temp = temp.left;
							
					}
					
					left.linkRight(temp);
				}
			}
		}
		
		return h;
	}
	
	public static DLXColumn createDLXMatrixVersion2 (List<Tuple> ones, int rows, int columns){
		//**First, create the main header and all columns
		
		//HashMap to quickly access the created columns during initialization
		//of other nodes
		Map<Integer,DLXColumn> columnsHashMap = new HashMap<Integer,DLXColumn>();
				
		DLXColumn h = new DLXColumn(-1);
		DLXColumn previous = h;
		h.m = rows;		
		h.n = columns;		
				
		for(int i = 0 ; i < columns ; i++){
			DLXColumn c = new DLXColumn(i+1);
					
			previous.linkRight(c);
			previous = c;
			
			//If the column is empty, the column header
			//is connect to itself up and down
			c.linkDown(c);
					
			columnsHashMap.put(c.id,c);
		}
				
		//Last element has to link back to h (circular list)
		previous.linkRight(h);
				
		//***********************
	
		DLXNode left = null;
		DLXNode firstInRow = null;
		DLXNode n = null;
		
		for(int i = 0 ; i < ones.size() ; i++){
			
			Tuple position = ones.get(i);
			
			n = new DLXNode(position);
			
			if(firstInRow == null)
				firstInRow = n;
			
			DLXColumn c = columnsHashMap.get(ones.get(i).y + 1);
			
			if(left != null){
				left.linkRight(n);
			}
			
			if(c.up != null){
				c.up.linkDown(n);
			}else{
				n.linkUp(c);
			}
				
			n.linkDown(c);
			n.linkColumn(c);
		
			left = n;	
		
			//If this is the last element in this row
			if((i+1 < ones.size() && ones.get(i+1).x != ones.get(i).x) || i+1 > ones.size()){
				n.linkRight(firstInRow);
				firstInRow = null;
				left = null;
			}
		}
		
		n.linkRight(firstInRow);
				
		return h;
	}

	public void DLX (int k, List<DLXNode> solution){
		
		if(answer.size() < sudokuSize){
			if(h.right == h){
				//printSolution(solution);
				if(answer.size() < h.m*h.n){
					for(DLXNode n : solution)
						answer.add(n);
				}
				
				return;
			}
			else{
				DLXColumn c = chooseColumn(h);
				coverColumn(c);
				
				for(DLXNode r = c.down ; r != c ; r = r.down){
					solution.add(r);
					
					for(DLXNode j = r.right ; j != r ; j = j.right)
						coverColumn(j.columnHeader);
					
					//System.out.println("Matrix after covering column c and the others:");
					//printMatrix(h);
					DLX(k+1,solution);
					
					//Gets the last element in solution.
					//not sure if this is right
					r = solution.remove(solution.size()-1);
					c = r.columnHeader;
					
					for(DLXNode j = r.left ; j != r ; j = j.left)
						uncoverColumn(j.columnHeader);
				}
				
				//System.out.println("Matrix after uncovering column c and the others:");
				//printMatrix(h);
				uncoverColumn(c);
				
				return;
				
			}
		}else{
			return;
		}
			
	}
	
	public static List<Integer> solveExactCover (int sudokuSize, int[][] restrictionsMatrix){
		
		DancingLinks dl = new DancingLinks(sudokuSize,restrictionsMatrix);
		List<DLXNode> temp = new ArrayList<DLXNode>();
		List<Integer> solution = new ArrayList<Integer>();
		
		//printMatrix(dl.h);
		
		dl.DLX(0,temp);
		
		for(DLXNode n : dl.answer)
			solution.add(new Integer(n.position.x));
		
		return solution;	
	}
	
	public static void printSolution(List<DLXNode> solution){
		
		System.out.println("The Solution is:");
		
		for(int i = 0 ; i < solution.size() ; i++){
			DLXNode n = solution.get(i);
			
			System.out.println(n.position.x);
			//System.out.println();
		}
	}
	
	//This method implements the heuristic described
	//in Donald Knuth's paper to minimize the branching
	//factor
	public static DLXColumn chooseColumn (DLXColumn h){
		int minSize = Integer.MAX_VALUE;
		DLXColumn chosenColumn = null;
		
		for(DLXColumn c = (DLXColumn) h.right ; c != h ; c = (DLXColumn) c.right){
			if(c.size < minSize){
				chosenColumn = c;
				minSize = c.size;
			}
		}
		
		return chosenColumn;
		
	}
	
	public static void coverColumn (DLXColumn c){
		
		c.right.left = c.left;
		c.left.right = c.right;
		
		for(DLXNode i = c.down ; i != c ; i = i.down){
			for(DLXNode j = i.right ; j != i ; j = j.right){
				j.coverNode();
			}
		}

	}
	
	public static void uncoverColumn (DLXColumn c){
		for(DLXNode i = c.up ; i != c ; i = i.up ){
			for (DLXNode j = i.left ; j != i ; j = j.left){
				j.uncoverNode();
			}
		}
		
		c.left.right = c;
		c.right.left = c;
	}
	
	public static void printMatrix (DLXColumn h){
		
		List<Tuple> ones = new ArrayList<Tuple>();
		
		for(DLXColumn c = (DLXColumn) h.right ; c != h ; c = (DLXColumn) c.right){
			for(DLXNode n = c.down ; n != c ; n = n.down)
				n.marked = false;
		}
		
		for(DLXColumn c = (DLXColumn) h.right ; c != h ; c = (DLXColumn) c.right){
			for(DLXNode n = c.down ; n != c ; n = n.down){
				if(!n.marked){
					DLXNode r = n;
					
					do{
						//System.out.println(r.position);
						ones.add(r.position);
						r.marked = true;
						r = r.right;
					}while(r!=n);
					
					n.marked = true;
					//System.out.println();
				}
			}
				
		}
		
		printDLX(ones,h.m,h.n);
		
		//System.out.println();
	
	}
	
	private static void printDLX(List<Tuple> list, int rows, int columns){
		int[][] matrix = new int[rows][columns];
		
		PrintWriter writer;
		
		try{
			writer = new PrintWriter("DLXmatrix.txt","UTF-8");
		}catch(Exception e){
			System.out.println("Problem");
			return;
		}
		//Fill matrix with zeros
		for(int i = 0 ; i < rows ; i++){
			for(int j = 0 ; j < columns ; j++)
				matrix[i][j] = 0;
		}
		
		for(Tuple t : list)
			matrix[t.x][t.y] = 1;
		
		for(int i = 0 ; i < rows ; i++){
			for (int j = 0 ; j < columns ; j++){
				if(matrix[i][j] != 0)
					writer.print(" " + matrix[i][j]);
				else
					writer.print("  ");
			}
			writer.println();
		}	
		
		writer.close();
	}
}
