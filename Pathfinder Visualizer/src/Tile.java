
public class Tile {
	public int locX;
	public int locY;
	
	public int g = 0;
	public int h = 0;
	public int f = 0;
	
	public int[] neighbors = {0, 0, 0, 0};
	public int numNeighbors = 0;
	public Tile prev;
	public boolean visited = false;
	
	Tile(){
		locX = 0;
		locY = 0;
	}
	
	Tile(int x, int y){
		this.locX = x;
		this.locY = y;
	}
	
	public void add_neighbors(int neigh) {
		neighbors[numNeighbors] = neigh;
		numNeighbors++;
	}
	
	public int get_neighbor(int x) {
		return neighbors[x];
	}

}
