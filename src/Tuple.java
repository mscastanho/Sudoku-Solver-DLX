public class Tuple {
	int x;
	int y;
	
	public Tuple(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override 
	public String toString(){
		return "(" + this.x + "," + this.y + ")";
	}
}
