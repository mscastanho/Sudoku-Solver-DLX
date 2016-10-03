public class SudokuValue {
	int value;
	Tuple position;
	
	public SudokuValue(int value, Tuple position){
		this.value = value;
		this.position = position;
	}
	
	@Override
	public String toString (){
		return value + " -> " + position;
	}
}
