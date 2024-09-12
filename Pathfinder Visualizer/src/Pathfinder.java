import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Pathfinder extends JPanel implements ActionListener{
	static final int SCREENWIDTH = 1000;
	static final int SCREENHEIGHT = 600;
	private Timer timer;
	private int algorithm = 10;
	
	private Tile start;
	private Tile end;
	private Tile[] tile;
	
	private int placeType = 0;
	
	private Tile[] open;
	private int openSpots = 0;
	
	private int closeSpots = -1;
	private Tile[] close;
	
	int wallSize = -1;
	private Tile[] wall;
	
	private int maxtiles;
	private int unitsize;
	private boolean noPath = false;
	
	Pathfinder(){
		this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
		this.setFocusable(true);
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Clicked: " + e.getX() + ", " + e.getY());
				int x = e.getX();
				int y = e.getY();
				switch (placeType) {
				//Places Either a Start, Wall or End Location
				case 0:
					place_start(get_index(x, y));
					break;
				case 1:
					end = tile[get_index(x, y)];
					remove_wall(get_index(x, y));
					break;
				case 2:
					place_wall(get_index(x, y));
					break;
				case 3:
					remove_wall(get_index(x, y));
					break;
				}
				algorithm = 10;
				openSpots = 0;
				closeSpots = -1;
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (e.getX() < SCREENWIDTH && e.getX() > 0 && e.getY() < SCREENHEIGHT && e.getY() > 0) {
					if (placeType == 2) {
						place_wall(get_index(e.getX(), e.getY()));
					} else if (placeType == 3) {
						if (wallPlaced(get_index(e.getX(), e.getY())))
							remove_wall(get_index(e.getX(), e.getY()));
					}
					algorithm = 10;
					openSpots = 0;
					closeSpots = -1;
				}
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {}
		});
		
		new_unit_size(50);
	}
	
	//Resizes the Grid
	public void new_unit_size(int newSize) {
		arr_size(newSize);
		wall = new Tile[maxtiles];
		tile = new Tile[maxtiles];
		open = new Tile[maxtiles];
		close = new Tile[maxtiles];
		build();
		set_speed(25);
		timer.start();
	}
	
	//Helper Function
	//Sets new Sizes for the Arrays
	private void arr_size(int newSize) {
		maxtiles = (SCREENWIDTH/newSize)*(SCREENHEIGHT/newSize);
		unitsize = newSize;
	}
	
	public void change_place_type(int x) {
		placeType = x;
	}
	
	//Changes The timer Speed
	public void set_speed(int speed) {
		timer = new Timer(speed, this);
	}
	
	//Clears the Grid
	public void build() {
		openSpots = 0;
		closeSpots = -1;
		wallSize = -1;
		noPath = false;
		int current = 0;
		for (int i = 0; i < SCREENWIDTH/unitsize; i++) {
			for (int j = 0; j < SCREENHEIGHT/unitsize; j++) {
				tile[current] = new Tile(i*unitsize, j*unitsize);
				tile[current].visited = false;
				tile[current].g = Integer.MAX_VALUE;
				tile[current].h = Integer.MAX_VALUE;
				tile[current].f = Integer.MAX_VALUE;
				//Left Neighbor
				if (i > 0) {
					tile[current].add_neighbors(current-(SCREENHEIGHT/unitsize));
				}
				//Right Neighbor
				if (i < SCREENWIDTH/unitsize-1) {
					tile[current].add_neighbors(current+(SCREENHEIGHT/unitsize));
				}
				//Up Neighbor
				if (j > 0) {
					tile[current].add_neighbors(current-1);
				}
				//Down Neighbor
				if (j < SCREENHEIGHT/unitsize-1) {
					tile[current].add_neighbors(current+1);
				}
				current++;
			}
			//System.out.println("");
		}
		//System.out.print("Current: " + current + " Max: " + maxtiles);
		
		
		//Debug Remove when done
		end = tile[maxtiles-1];
		place_start(0);
	}	
	
	//Places Start Location
	private void place_start(int index) {
		openSpots = 0;
		open[openSpots] = tile[index];
		open[openSpots].g = 0;
		open[openSpots].f = 0;
		open[openSpots].h = 0;
		open[openSpots].prev = null;
		start = open[openSpots];
		remove_wall(index);
	}
	
	//Places Walls at Index
	private void place_wall(int index) {
		try {
			//Check if wall is already Placed
			if (!wallPlaced(index)) {
				wallSize++;
				wall[wallSize] = tile[index];
				wall[wallSize].prev = null;	
			}
		} catch (Exception e) {
			wallSize--;
		}
	}
	
	//Removes Wall at Index
	private void remove_wall(int index) {
		if (wallSize >= 0 && wallPlaced(index)) {
			int j = 0;
			for (int i = 0; i <= wallSize; i ++) {
				if (wall[i].locX == tile[index].locX && wall[i].locY == tile[index].locY) {
					continue;
				}
				wall[j] = wall[i];
				j++;
			}
			wallSize--;
		}
	}
	
	private boolean wallPlaced(int index) {
		for (int i = 0; i <= wallSize; i++) {
			if (wall[i].locX == tile[index].locX && wall[i].locY == tile[index].locY) {
				return true;
			}
		}
		return false;
	}
	
	private int get_index(int xPos, int yPos) {
		return ((xPos/unitsize)*(SCREENHEIGHT/unitsize)) + (yPos/unitsize);
	}
	
	public void place_random(int alg) {
		Random rand = new Random();
		int loc = 0;
		switch (alg) {
		case 0:
			loc = rand.nextInt((maxtiles-1) - 0) + 0;
			place_start(loc);
			remove_wall(loc);
			break;
		case 1:
			loc = rand.nextInt((maxtiles-1) - 0) + 0;
			end = tile[loc];
			remove_wall(loc);
			break;
		case 2:
			for (int i = 0; i < rand.nextInt((maxtiles/4));) {
				place_wall(rand.nextInt(wall.length));
			}
			break;
		}
		
		algorithm = 10;
		openSpots = 0;
		closeSpots = -1;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		for (int i = 0; i < SCREENWIDTH/unitsize; i++) {
			g.drawLine(i*unitsize, 0, i*unitsize, SCREENHEIGHT);
			g.drawLine(0, i*unitsize, SCREENWIDTH, i*unitsize);
		}
		
		//Finds the Shortest Path
		switch(algorithm) {
		case 0:
			dijstras(g);
			break;
		case 1:
			a_star(g);
			break;
		}
		draw_visited(g);
		
		//Draw Start point and End Point
		draw_wall(g);
		draw_start(g);
		draw_end(g);
		
		//Draw Path if Algorithm is done
		if (algorithm == -1) {
			draw_path(g);
			timer.stop();
		} else if (noPath) {
			no_path(g);
		}
	}
	
	private void dijstras(Graphics g) {
		//Dijstras Algorithm
		
		//Find the Unvisited Node with the smallest g
		int smallIndex = -1;
		int smallG = Integer.MAX_VALUE;
		if (openSpots > 0) {
			for (int i = 0; i < openSpots; i++) {
				if (open[i].g < smallG) {
					smallIndex = i;
					smallG = open[i].g;
				}
			}
		} else {
			smallIndex = openSpots;
		}
		//set current as the smallest distance
		Tile curr = open[smallIndex];
		curr.visited = true;
		
		int index = 0;
		for (int i = 0; i <= openSpots; i++) {
			if (i != smallIndex) {
				open[index] = open[i];
				index++;
			}
		}
		
		openSpots--;
		
		
		closeSpots++;
		close[closeSpots] = curr;		
		
		int temp = curr.g + 1;
		//Check if there are open spots
		for (int i = 0; i < curr.numNeighbors; i++) {
			Tile neighbor = tile[curr.get_neighbor(i)];
			//check if neighbor is the end point
			if (neighbor.locX == end.locX && neighbor.locY == end.locY) {
				openSpots++;
				open[openSpots] = neighbor;
				open[openSpots].prev = curr;
				algorithm = -1;
				break;
			} else if (check_wall(neighbor)) {
				neighbor.prev = null;
				neighbor.g = Integer.MAX_VALUE;
				neighbor.numNeighbors = 0;
				continue;
			} else if (!neighbor.visited) {
				//check if neighbor is in open
				boolean inOpen = false;
				for (int j = 0; j <= openSpots; j++) {
					if (open[j].locX == neighbor.locX && open[j].locY == neighbor.locY) {
						inOpen = true;
						break;
					}
				}
				
				//if Neighbor is not already in open
				if (!inOpen) {
					//Add Neighbor to Open Spots
					openSpots++;
					open[openSpots] = neighbor;
					open[openSpots].prev = curr;
					open[openSpots].g = temp;
					//System.out.println("G: " + open[openSpots].g);
				}
			//Neighbor is already visited
			} else {
				//start of test
				
				
				//end of test
				if (neighbor.g > temp) {
					neighbor.g = temp;
					neighbor.prev = curr;
				}
				//System.out.println("Visited");
			}
		}
		
		if (openSpots < 0) {
			//No Possible Path
			noPath = true;
		}
		
		//Draw Open Spots
		draw_open(g);
		//System.out.println("Open Spots: " + openSpots);
		//timer.stop();
	}
	
	private void a_star(Graphics g) {
		//A* Algorithm
		if (openSpots >= 0) {
			int smallestF = Integer.MAX_VALUE;
			int smallestIndex = maxtiles-1;
			
			//Find Smallest F Value in Open Set
			Tile current = new Tile();
			for (int i = 0; i <= openSpots; i++) {
				if (open[i].f < smallestF) {
					smallestIndex = i;
					smallestF = open[i].f;
				}
			}
			current = open[smallestIndex];
			current.visited = true;
			
			//Remove Current for Open Spots
			int k = 0;
			for (int i = 0; i <= openSpots; i++) {
				if (i == smallestIndex)
					continue;
				open[k] = open[i];
				k++;
			}
			openSpots--;
			
			closeSpots++;
			close[closeSpots] = current;
			System.out.println(current.numNeighbors);
			int tempG = current.g + 1;
			
			//Checks each Unvisited Neighbor
			for (int i = 0; i < current.numNeighbors; i++) {
				Tile neighbor = tile[current.get_neighbor(i)];
				
				if (neighbor.locX == end.locX && neighbor.locY == end.locY) {
					openSpots++;
					open[openSpots] = neighbor;
					open[openSpots].prev = current;
					algorithm = -1;
					break;
				} else if (check_wall(neighbor)) {
					neighbor.prev = null;
					neighbor.g = Integer.MAX_VALUE;
					neighbor.f = Integer.MAX_VALUE;
					neighbor.h = Integer.MAX_VALUE;
					continue;
				//Check if Neighbor is Already Evaluated
				} else if (!neighbor.visited) {
					
					//Checks if already in Open Spots
					boolean inOpen = false;
					for (int j = 0; j <= openSpots; j++) {
						if (open[j].locX == neighbor.locX && open[j].locY == neighbor.locY) {
							inOpen = true;
							break;
						}
					}
					
					//Change to current G Value
					if (inOpen) {
						if (tempG < neighbor.g) {
							neighbor.g = tempG;
						}
					} else {
						neighbor.g = tempG;
						neighbor.h = heuristic(neighbor);
						neighbor.f = neighbor.g + neighbor.h;
						openSpots++;
						open[openSpots] = neighbor;
						open[openSpots].prev = current;
					}
				}
			}
		}
		if (openSpots < 0) {
			//No Possible Path
			noPath = true;
		}
		
		draw_open(g);
		//timer.stop();
	}
	
	//Calculates Heuristics for the A* Algorithm
	private int heuristic(Tile neighbor) {
		int score = 0;
		score = (int) Math.sqrt(Math.pow((neighbor.locX - end.locX), 2) + Math.pow((neighbor.locY - end.locY), 2));
		return score;
	}
	
	//Draws Unvisited Locations
	private void draw_open(Graphics g) {
		for(int i = 0; i <= openSpots; i++) {
			g.setColor(Color.BLUE);
			g.fillRect(open[i].locX, open[i].locY, unitsize, unitsize);
			g.setColor(Color.BLACK);
			g.drawRect(open[i].locX, open[i].locY, unitsize, unitsize);
			System.out.println("open: " + i + " x: " + open[i].locX + ", Y: " + open[i].locY);
		}
	}
	
	//Draws All Visited Locations
	private void draw_visited(Graphics g) {
		//Draw Visited Paths
		for (int i = 0; i <= closeSpots; i++) {
			g.setColor(Color.YELLOW);
			g.fillRect(close[i].locX, close[i].locY, unitsize, unitsize);
			g.setColor(Color.BLACK);
			g.drawRect(close[i].locX, close[i].locY, unitsize, unitsize);
		}
	}
	
	//Draws Path Once Shortest Path is Found
	//Draws in Reverse Order
	private void draw_path(Graphics g) {
		Tile current = open[openSpots];
		int counter = 0;
		System.out.println("Found End");
		//while (current.locX != start.locX && current.locY != start.locY) {
		while (current.prev != null) {
			g.setColor(Color.ORANGE);
			g.fillRect(current.locX, current.locY, unitsize, unitsize);
			g.setColor(Color.BLACK);
			g.drawRect(current.locX, current.locY, unitsize, unitsize);
			System.out.println("Current: " + current.locX + ", " + current.locY);
			current = current.prev;
			counter++;
		}
		draw_start(g);
		draw_end(g);
		distance(g, counter);
		timer.stop();
	}
	
	//Helper Function
	//Shows Amount of Blocks Traveled
	private void distance(Graphics g, int counter) {
		g.setColor(Color.MAGENTA);
		g.setFont(new Font("Times New Roman", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Distance = " + counter + " Tiles", (SCREENWIDTH - metrics.stringWidth("Distance = " + counter + " Tiles"))/2, SCREENHEIGHT/2);
	}
	
	//Draws if No Path to End is Found
	private void no_path(Graphics g) {
		draw_wall(g);
		draw_start(g);
		draw_end(g);

		g.setColor(Color.MAGENTA);
		g.setFont(new Font("Times New Roman", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("No Valid Path", (SCREENWIDTH - metrics.stringWidth("No Valid Path"))/2, SCREENHEIGHT/2);
		timer.stop();
	}
	
	//Checks if Neighbor is a wall
	private boolean check_wall(Tile temp) {
		if (wallSize >= 0 ) {
			for (int i = 0; i < wallSize; i++) {
				if (temp.locX == wall[i].locX && temp.locY == wall[i].locY)
					return true;
			}
		}
		return false;
	}
	
	//Draws The Start Location
	public void draw_start(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(start.locX, start.locY, unitsize, unitsize);
		g.setColor(Color.BLACK);
		g.drawRect(start.locX, start.locY, unitsize, unitsize);
	}
	
	//Draws End Locations
	public void draw_end(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(end.locX, end.locY, unitsize, unitsize);
		g.setColor(Color.BLACK);
		g.drawRect(end.locX, end.locY, unitsize, unitsize);
	}
	
	//Draws All the Walls
	private void draw_wall(Graphics g) {
		if (wallSize >= 0) {
			for (int i = 0; i <= wallSize; i++) {
				g.setColor(Color.BLACK);
				g.fillRect(wall[i].locX, wall[i].locY, unitsize, unitsize);
				g.setColor(Color.WHITE);
				g.drawRect(wall[i].locX, wall[i].locY, unitsize, unitsize);
			}
		}
	}
	
	//Starts Algorithm
	//Dijstras = 0
	//A* = 1
	public void start(int alg) {
		//System.out.println("Restarting Timer");
		algorithm = alg;
		timer.start();
	}
	
	public void pause() {
		timer.stop();
	}
	
	//Resets The Grid
	public void stop() {
		timer.stop();
		build();
		algorithm = 10;
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}

}
