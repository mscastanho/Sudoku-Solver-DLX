public class DLXNode {
	DLXNode right;
	DLXNode left;
	DLXNode up;
	DLXNode down;
	DLXColumn columnHeader;
	
	Tuple position;
	
	boolean marked;
	
	public DLXNode (){
		this.columnHeader = null;
		this.down = null;
		this.up = null;
		this.left = null;
		this.right = null;
		this.marked = false;
		this.position = null;
	}
	
	public DLXNode (Tuple position){
		this.columnHeader = null;
		this.down = null;
		this.up = null;
		this.left = null;
		this.right = null;
		this.marked = false;
		this.position = position;
	}
	
	public void linkRight (DLXNode n){
		this.right = n;
		n.left = this;
	}
	
	public void linkLeft (DLXNode n){
		this.left = n;
		n.right = this;
	}
	
	public void linkDown (DLXNode n){
		this.down = n;
		n.up = this;
	}
	
	public void linkUp (DLXNode n){
		this.up = n;
		n.down = this;
	}
	
	public void linkColumn (DLXColumn c){
		this.columnHeader = c;
		c.size++;
	}
	
	public void coverNode (){
		this.up.down = this.down;
		this.down.up = this.up;
		this.columnHeader.size--;
	}
	
	public void uncoverNode (){
		this.up.down = this;
		this.down.up = this;
		this.columnHeader.size++;
	}
}
