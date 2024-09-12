import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Sudoku extends JPanel implements ActionListener{
	private final int SCREENWIDTH = 900;
	private final int SCREENHEIGHT = 900;
	private final int UNITSIZE = 100;
	private Timer timer;
	
	//Main Game Board
	private int[][] board = new int[SCREENWIDTH/UNITSIZE][SCREENHEIGHT/UNITSIZE];
	
	//Example Board
	private int[][] baseBoard = {	
			{5, 4, 0, 0, 0, 6, 0, 0, 8},
			{7, 6, 3, 5, 0, 0, 1, 0, 0},
			{1, 0, 8, 3, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 3, 5, 7, 0, 1},
			{0, 0, 1, 0, 0, 0, 9, 0, 0},
			{6, 0, 5, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 4, 3, 0, 5},
			{0, 0, 4, 0, 0, 3, 6, 1, 9},
			{9, 0, 0, 2, 0, 0, 0, 8, 7}	};
	
	private boolean solve = false;
	private boolean show = false;
	
	private int xPos, yPos;
	
	private int[] stackX = new int[(SCREENWIDTH/UNITSIZE) * (SCREENHEIGHT/UNITSIZE)];
	private int[] stackY = new int[(SCREENWIDTH/UNITSIZE) * (SCREENHEIGHT/UNITSIZE)];
	private int[] stackVal = new int[(SCREENWIDTH/UNITSIZE) * (SCREENHEIGHT/UNITSIZE)];
	private int stackSize;
	
	private int currentNum;
	
	Sudoku(){
		this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
		this.setFocusable(true);
		
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent arg0) {}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				xPos = (e.getX()/UNITSIZE)*UNITSIZE;
				yPos = (e.getY()/UNITSIZE)*UNITSIZE;
				//System.out.println(xPos + ", " + yPos);
			}
		});
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				xPos = e.getX()/UNITSIZE;
				yPos = e.getY()/UNITSIZE;
				if (valid_pos()) {
					System.out.println("Valid Pos");
					String input = JOptionPane.showInputDialog("Input Number:");
					if (input != null) {
						switch (input) {
						case "0":
						case "1":
						case "2":
						case "3":
						case "4":
						case "5":
						case "6":
						case "7":
						case "8":
						case "9":
							board[yPos][xPos] = Integer.parseInt(input);
							break;
						}
					}
				} else
					System.out.println(baseBoard[yPos][xPos]);
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		clear_board();
	}
	
	private boolean valid_pos() {
		if (board[yPos][xPos] == baseBoard[yPos][xPos] && board[yPos][xPos] > 0)
			return false;
		return true;
	}
	
	//Clears Board back to its unfilled state
	public void clear_board() {
		solve = false;
		show = false;
		stackSize = -1;
		currentNum = 1;
		timer = new Timer(0, this);
		for (int i = 0; i < SCREENHEIGHT/UNITSIZE; i++) {
			for (int j = 0; j < SCREENWIDTH/UNITSIZE; j++) {
				board[j][i] = baseBoard[j][i];
				//System.out.print(newBoard[j][i] + " ");
			}
			//System.out.println();
		}
		
		find_next();
		repaint();
	}
	
	//Generates new boards until it finds a solvable board
	public void new_board() {
		do {
			generate_board();
		} while(!solve_sudoku());
		clear_board();
	}
	
	//Generates a new Board
	private void generate_board() {
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			for (int j = 0; j < SCREENHEIGHT/UNITSIZE; j++) {
				board[j][i] = 0;
				baseBoard[j][i] = 0;
			}
		}
		
		int num = get_num();
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			for (int j = 0; j < SCREENHEIGHT/UNITSIZE; j++) {
				if (!in_row(j, num) && !in_col(i, num) && !in_grid(i, j, num) && get_num()%3 != 0) {
					baseBoard[j][i] = num;
					board[j][i] = num;
				}
				num = get_num();
			}
		}
		
		clear_board();
	}
	
	//Helper Classes for Generating a new board
	//Checks if number is already in the row
	private boolean in_row(int col, int num) {
		for (int i = 0; i < board.length; i++) {
			if (baseBoard[col][i] == num)
				return true;
		}
		return false;
	}
	
	//checks if the number is already in the column
	private boolean in_col(int row, int num) {
		for (int i = 0; i < board.length; i++) {
			if (baseBoard[i][row] == num)
				return true;
		}
		return false;
	}
	
	//checks if number is already in the grid
	private boolean in_grid(int row, int col, int num) {
		int numX = (row/3) * 3;
		int numY = (col/3) * 3;
		//System.out.println("x = " + x + ", y = " + y);
		for (int i = numX; i < numX + 3; i++) {
			for (int j = numY; j < numY + 3; j++) {
				//System.out.println(j + ", " + i + ": " + board[j][i]);
				if (baseBoard[j][i] == num && row != i && col != j) {
					return true;
				}
			}
		}
		return false;
	}
	
	//generates new number
	private int get_num() {
		Random rand = new Random();
		return rand.nextInt(9 - 1 + 1) + 1;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		//Paints Each Grid in alternating colors
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, UNITSIZE*3, UNITSIZE*3);
		g.fillRect(600, 0, UNITSIZE*3, UNITSIZE*3);
		g.fillRect(300, 300, UNITSIZE*3, UNITSIZE*3);
		g.fillRect(0, 600, UNITSIZE*3, UNITSIZE*3);
		g.fillRect(600, 600, UNITSIZE*3, UNITSIZE*3);
		
		//Generates Table
		g.setColor(Color.BLACK);
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			g.drawLine(i*UNITSIZE, 0, i*UNITSIZE, SCREENWIDTH);
			g.drawLine(0, i*UNITSIZE, SCREENHEIGHT, i*UNITSIZE);
		}
		
		//Solves the Sudoku using Recursion
		if (solve && !show) {
			solve_sudoku();
		} else if (solve && show) {
			//Solves the Sudoku using an iterative Solution
			
			g.setColor(Color.BLUE);
			g.setFont(new Font("Times New Roman", Font.BOLD, UNITSIZE));
			
			if (currentNum <= 9) {
				//Places Current Number on the Board
				board[stackY[stackSize]][stackX[stackSize]] = currentNum;
				g.drawString(board[stackY[stackSize]][stackX[stackSize]] + "", stackX[stackSize]*UNITSIZE + (UNITSIZE/4), (stackY[stackSize] + 1)*UNITSIZE - 20);
				
				//Checks if Current Number is a valid Placement
				if (check_valid(stackX[stackSize], stackY[stackSize])) {
					stackVal[stackSize] = currentNum;
					find_next();
					currentNum = 1;
				} else {
					//if Number is not a valid choice remove it from board
					board[stackY[stackSize]][stackX[stackSize]] = 0;
					
					//If all numbers are checked for current Tile pop from stack until the placed Number is not 9
					if (currentNum == 9) {
						do {
							stackSize--;
							board[stackY[stackSize]][stackX[stackSize]] = 0;
							currentNum = stackVal[stackSize];
							System.out.println("Stack Size: " + stackSize);
						} while (currentNum == 9);
					}
					currentNum++;
				}
				
			}
		}
		
		draw_numbers(g);
	}
	
	//Helper for Iterative Solution
	//Finds the next Unfilled Location and add it to stack
	private void find_next() {
		boolean isEmpty = true;
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			for (int j = 0; j < SCREENHEIGHT/UNITSIZE; j++) {
				if (board[j][i] == 0) {
					//If Spot is Open add it to stack
					stackSize++;
					stackX[stackSize] = i;
					stackY[stackSize] = j;
					
					isEmpty = false;
					break;
				}
			}
			if (!isEmpty)
				break;
		}
		
		//Check if 
		if (isEmpty) {
			System.out.println("All Spaces Filled");
			solve = false;
			show = false;
			timer.stop();
		}
	}
	
	//Draws the Numbers on the Grid
	//Black = Base Numbers
	//Green = Placed Numbers
	private void draw_numbers(Graphics g) {
		g.setFont(new Font("Times New Roman", Font.BOLD, UNITSIZE));
		for (int col = 0; col < SCREENHEIGHT/UNITSIZE; col++) {
			for (int row = 0; row < SCREENWIDTH/UNITSIZE; row++) {
				if (board[col][row] > 0 && baseBoard[col][row] == board[col][row]) {
					g.setColor(Color.BLACK);
					g.drawString(board[col][row] + "", row*UNITSIZE + (UNITSIZE/4), (col + 1)*UNITSIZE - 20);
				} else if (board[col][row] > 0) {
					if (check_valid(row, col))
						g.setColor(Color.GREEN);
					else
						g.setColor(Color.RED);
					g.drawString(board[col][row] + "", row*UNITSIZE + (UNITSIZE/4), (col + 1)*UNITSIZE - 20);
				}
				
			}
		}
	}
	
	//Checks if the number in the specified Row and Column is Valid
	//Must place number in board before Checking
	private boolean check_valid(int x, int y) {
		//Check If Valid Row
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			if (x != i && board[y][i] == board[y][x])
				return false;
		}
		
		//Check if Valid Column
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			if (y != i && board[i][x] == board[y][x])
				return false;
		}
		
		//Check Grid
		int numX = (x/3) * 3;
		int numY = (y/3) * 3;
		//System.out.println("x = " + x + ", y = " + y);
		for (int i = numX; i < numX + 3; i++) {
			for (int j = numY; j < numY + 3; j++) {
				//System.out.println(j + ", " + i + ": " + board[j][i]);
				if (board[j][i] == board[y][x] && x != i && y != j) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	//Recursive Backtracking Solution
	public boolean solve_sudoku() {
		int row = -1, col = -1;
		boolean isEmpty = true;
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			for (int j = 0; j < SCREENHEIGHT/UNITSIZE; j++) {
				if (board[j][i] == 0) {
					row = i;
					col = j;
					
					isEmpty = false;
					break;
				}
			}
			if (!isEmpty)
				break;
		}
		
		//If Board is filled The Board is Solved
		if (isEmpty)
			return true;
		
		for (int num = 1; num <= board.length; num++) {
			board[col][row] = num;
			if (check_valid(row, col)) {
				if (solve_sudoku()) {
					return true;
				} else {
					board[col][row] = 0;
				}
			} else {
				board[col][row] = 0;
			}
		}
		
		return false;
	}
	
	//Clears all Placed Numbers and Solves the Sudoku
	public void solve() {
		clear_board();
		solve = true;
		show = false;
		timer.stop();
		repaint();
	}
	
	//Enables the Sudoku to be Solved Iteratively
	public void show_solve(int delay) {
		clear_board();
		show = true;
		solve = true;
		timer = new Timer(delay, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}
	
	
}
