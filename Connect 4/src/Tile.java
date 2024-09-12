
public class Tile {
	public int locX;
	public int locY;
	
	//Neighbors = {Up, Up Right, Right, Down Right, Down, Down Left, Left, Up Left}
	public int[] neighbors = {0, 0, 0, 0, 0, 0, 0, 0};
	
	public int type;
	
	Tile(int x, int y){
		locX = x;
		locY = y;
	}
}
