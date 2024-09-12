import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener{
	private final int SCREENWIDTH = 700;
	private final int SCREENHEIGHT = 600;
	private final int UNITSIZE = 100;
	private final int MAXTILES = (SCREENWIDTH/UNITSIZE)*(SCREENHEIGHT*UNITSIZE);
	private final int DELAY = 100;
	private Timer timer;
	
	private Tile[] tile = new Tile[MAXTILES];
	
	//Stores tiles placed
	private Tile[][] board = new Tile[SCREENWIDTH/UNITSIZE][SCREENHEIGHT/UNITSIZE];
	
	//Stores Current Height for Each Row
	private int[] boardHeight = new int[SCREENWIDTH/UNITSIZE];
	
	//Shows Preview
	private Tile hover;
	
	//Current Turn
	private boolean maxTurn;
	
	//Holds Winner Tiles
	private Tile[] winner = new Tile[4];
	
	Game(){
		this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
		this.setFocusable(true);
		timer = new Timer(DELAY, this);
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//int index = get_index(e.getX(), e.getY());
				//System.out.println(e.getX() + ", " + e.getY() + ", Index: " + index);
				if (maxTurn) {
					if (place(e.getX(), 1)) {
						maxTurn = false;
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent arg0) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				//System.out.println(e.getX() + ", " + e.getY());
//				int index = get_index(e.getX(), e.getY());
//				if (e.getY() > 0 && index >= 0) {
//					hover = tile[index];
//				}
				//System.out.println(get_index(e.getX(), e.getY()));
			}
			
		});
		new_game();
	}
	
	//Resets Game
	public void new_game() {
		int current = 0;
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			//Initialize Each Tile and its Neighbors
			for (int j = 0; j <SCREENHEIGHT/UNITSIZE; j++) {
				tile[current] = new Tile(i*UNITSIZE, j*UNITSIZE);
				tile[current].type = 0;
				
				//Add Neighbors Indexes to tiles. If Index = -1 there is no neighbor in that direction
				//up neighbor
				if (j > 0)
					tile[current].neighbors[0] = current-1;
				else
					tile[current].neighbors[0] = -1;
				
				//Up Right Neighbor
				if (j > 0 && i < SCREENWIDTH/UNITSIZE - 1)
					tile[current].neighbors[1] = (current-1) + (SCREENHEIGHT/UNITSIZE);
				else
					tile[current].neighbors[1] = -1;
				
				//Right
				if (i < SCREENWIDTH/UNITSIZE - 1)
					tile[current].neighbors[2] = current + (SCREENHEIGHT/UNITSIZE);
				else
					tile[current].neighbors[2] = -1;
				
				//Down Right
				if (j < SCREENHEIGHT/UNITSIZE-1 && i < SCREENWIDTH/UNITSIZE-1)
					tile[current].neighbors[3] = (current+1) + (SCREENHEIGHT/UNITSIZE);
				else
					tile[current].neighbors[3] = -1;
				
				//Down
				if (j < SCREENHEIGHT/UNITSIZE-1)
					tile[current].neighbors[4] = current + 1;
				else
					tile[current].neighbors[4] = -1;
				
				//Down Left
				if (j < SCREENHEIGHT/UNITSIZE-1 && i > 0)
					tile[current].neighbors[5] = (current + 1) - (SCREENHEIGHT/UNITSIZE);
				else
					tile[current].neighbors[5] = -1;
				
				//Left
				if (i > 0)
					tile[current].neighbors[6] = current-(SCREENHEIGHT/UNITSIZE);
				else
					tile[current].neighbors[6] = -1;
				
				//Up Left
				if (i > 0 && j > 0)
					tile[current].neighbors[7] = (current-1) - (SCREENHEIGHT/UNITSIZE);
				else
					tile[current].neighbors[7] = -1;
		
				board[i][j] = tile[current];
				current++;
			}
			
			//Initialize Column Heights
			boardHeight[i] = SCREENHEIGHT/UNITSIZE-1;
		}
		
		hover = null;
		maxTurn = true;
		timer.start();
	}
	
	//Helper Function
	//Returns the Index Given the X and Y Position
	private int get_index(int xPos, int yPos) {
		return (xPos*(SCREENHEIGHT/UNITSIZE)) + yPos;
	}
	
	//Helper Function
	//Returns the X index Given the X Position
	private int get_x_pos(int xPos) {
		return (xPos/UNITSIZE);
	}
	
	//Places a new Piece on the Top Given an X position
	private boolean place(int x, int type) {
		x = get_x_pos(x);
		int height = boardHeight[x];
		if (boardHeight[x] < SCREENHEIGHT/UNITSIZE && height >= 0) {
			board[x][height] = tile[get_index(x, height)];
			board[x][height].type = type;
			//System.out.println(board[x][height].neighbors[0] + "|");
			boardHeight[x]--;
			//System.out.println("placeing: " + x + ", Height: " + boardHeight[x]);
			return true;
		} else {
			return false;
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		//Set Background Color
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SCREENWIDTH, SCREENHEIGHT);
		
		//Draws Current Board State
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			for (int j = 0; j < SCREENHEIGHT/UNITSIZE; j++) {
				if (board[i][j] != null) {
					if (board[i][j].type == 1) {
						//System.out.println(board[i][j].type + ", " + board[i][j].locX);
						g.setColor(Color.RED);
						g.fillOval(board[i][j].locX, board[i][j].locY, UNITSIZE, UNITSIZE);
					} else if (board[i][j].type == -1) {
						g.setColor(Color.YELLOW);
						g.fillOval(board[i][j].locX, board[i][j].locY, UNITSIZE, UNITSIZE);
					} else if (board[i][j].type == 0) {
						g.setColor(Color.GREEN);
						g.fillOval(board[i][j].locX, board[i][j].locY, UNITSIZE, UNITSIZE);
					}
				}
			}
		}
		
		//Comp Turn
		//Uses MiniMax up to Depth of 5
		if (!maxTurn) {
			int bestIndex = 0;
			int bestValue = Integer.MAX_VALUE;
			for (int i = 0; i < SCREENWIDTH; i += UNITSIZE) {
				if (place(i, -1)) {
					int value = minimax(0, true, 5);
					System.out.println(i + " Best Value: " + bestValue + ", Value: " + value);
					if (value < bestValue) {
						bestIndex = i;
						bestValue = value;
					}
					System.out.println("Best Index: " + bestIndex + ", Best Value: " + bestValue + "\n");
					remove_tile(i);
				} else {
					System.out.println("Cannot Place");
					bestIndex += UNITSIZE;
				}
			}
			
			if (place(bestIndex, -1)) {
				maxTurn = true;
			}
		}
		
		//Checks for A winner After Placement
		if (check_winner() == 1) {
			draw_winner(g, 1);
			timer.stop();
		} else if (check_winner() == -1) {
			draw_winner(g, -1);
		}
	}
	
	//Draws the Winning Set
	private void draw_winner(Graphics g, int type) {
		g.setColor(Color.BLUE);
		int indent = UNITSIZE/4;
		for (Tile win: winner) {
			g.fillRect(win.locX+indent, win.locY + indent, UNITSIZE/2, UNITSIZE/2);
		}
	}
	
	//Removes the Top Piece Given the X Position
	private void remove_tile(int x) {
		x = get_x_pos(x);
		int height = boardHeight[x] + 1;
		//System.out.println("Removing: " + x + ", Height: " + height);
		if (height < SCREENHEIGHT/UNITSIZE && height >= 0) {
			board[x][height] = tile[get_index(x, height)];
			board[x][height].type = 0;
			//System.out.println(board[x][height].locY + "| " + board[x][height].type);
			boardHeight[x]++;
		} 
	}
	
	//Checks for Winners
	private int check_winner() {
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			for (int j = SCREENHEIGHT/UNITSIZE - 1; j > 0; j--) {
				//Checks for vertical winners
				if (j - 3 >= 0) {
					if ((board[i][j].type == 1 || board[i][j].type == -1) &&
							board[i][j].type == board[i][j - 1].type && 
							board[i][j].type == board[i][j - 2].type &&
							board[i][j].type == board[i][j - 3].type) {
							//System.out.println("Found Vertical Winner");
						winner[0] = board[i][j];
						winner[1] = board[i][j - 1];
						winner[2] = board[i][j - 2];
						winner[3] = board[i][j - 3];
						return board[i][j].type;
						}
				}
				
				//Checks for Horizontal Winners
				if (i + 3 < SCREENWIDTH/UNITSIZE) {
					if ((board[i][j].type == 1 || board[i][j].type == -1) &&
							board[i][j].type == board[i + 1][j].type && 
							board[i][j].type == board[i + 2][j].type &&
							board[i][j].type == board[i + 3][j].type) {
								//System.out.println("Found Horizontal Winner");
						winner[0] = board[i][j];
						winner[1] = board[i + 1][j];
						winner[2] = board[i + 2][j];
						winner[3] = board[i + 3][j];
						return board[i][j].type;
					}
				}
				
				//Checks for Up Diagonal Winners
				if (i + 3 < SCREENWIDTH/UNITSIZE && j - 3 >= 0) {
					if ((board[i][j].type == 1 || board[i][j].type == -1) &&
							board[i][j].type == board[i + 1][j - 1].type && 
							board[i][j].type == board[i + 2][j - 2].type &&
							board[i][j].type == board[i + 3][j - 3].type) {
								//System.out.println("Found Diagonal Winner");
						winner[0] = board[i][j];
						winner[1] = board[i + 1][j - 1];
						winner[2] = board[i + 2][j - 2];
						winner[3] = board[i + 3][j - 3];
						return board[i][j].type;
					}
				}
				
				//Check for Down Diagonal Winners
				if (i + 3 < SCREENWIDTH/UNITSIZE && j + 3 < SCREENHEIGHT/UNITSIZE) {
					if ((board[i][j].type == 1 || board[i][j].type == -1) &&
							board[i][j].type == board[i + 1][j + 1].type && 
							board[i][j].type == board[i + 2][j + 2].type &&
							board[i][j].type == board[i + 3][j + 3].type) {
						//System.out.println("Found Down Diagonal Winner");
						winner[0] = board[i][j];
						winner[1] = board[i + 1][j + 1];
						winner[2] = board[i + 2][j + 2];
						winner[3] = board[i + 3][j + 3];
						return board[i][j].type;
					}
				}
			}
		}
		
		return 0;
	}
	
	//MiniMax Algorithm
	private int minimax(int depth, boolean isMaximizing,int max) {
		//Recursive End
		if (check_winner() == 1) {
			//System.out.println("Found Red Win: " + depth);
			return 1;
		} else if (check_winner() == -1) {
			//System.out.println("Found Yellow Win: " + depth);
			return -1;
		}
		
		//Max Depth Reached
		if (max == depth) {
			//System.out.println("Max Reached");
			return 0;
		}

		if (isMaximizing) {
			int bestVal = Integer.MIN_VALUE;
			for (int i = 0; i < SCREENWIDTH; i += UNITSIZE) {
				if (place(i, 1)) {
					int value = minimax(depth + 1, false, max);
					//System.out.println(depth + ": " + bestVal + ", " + value);
					bestVal = Integer.max(bestVal, value);
					remove_tile(i);
				}
			}
			//System.out.println("Returning:" + bestVal + "|Depth:" + depth);
			return bestVal;
		} else {
			int bestVal = Integer.MAX_VALUE;
			for (int i = 0; i < SCREENWIDTH; i += UNITSIZE) {
				if (place(i, -1)) {
					int value = minimax(depth + 1, true, max);
					//System.out.println(depth + ": " + bestVal + ", " + value);
					bestVal = Integer.min(bestVal, value);
					remove_tile(i);
				}
				
			}
			//System.out.println("Returning:" + bestVal + "|Depth:" + depth);
			return bestVal;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}
