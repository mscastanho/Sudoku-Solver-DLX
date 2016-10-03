public class DLXColumn extends DLXNode {
	int size;
	int id;
	
	int m;
	int n;
	
	public DLXColumn (int id){
		super();
		this.size = 0;
		this.id = id;
		this.m = 0;
		this.n = 0;
	}
	
	public void linkRight (DLXColumn c){
		this.right = c;
		c.left = this;
	}
	
	public void linkLeft (DLXColumn c){
		this.left = c;
		c.right = this;
	}
	
}
