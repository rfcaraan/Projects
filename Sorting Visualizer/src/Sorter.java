import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Sorter extends JPanel implements ActionListener{
	static final int SCREENWIDTH = 1000;
	static final int SCREENHEIGHT = 600;
	static final int UNITSIZE = 5;
	private int[] numbers;
	private final int DELAY = 175;
	private Timer timer;
	
	private int[] stack;
	private int top;
	private int high;
	private int low;
	//Selects Sort Type: 1. Quick Sort, 2. Bubble Sort, 3. Merge Sort, 4, Insertion Sort
	private int sort = 0;

	Sorter(){
		this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
		this.setFocusable(true);
		timer = new Timer(DELAY, this);
	}
	
	public void add_array(int[] array) {
		numbers = array;
		repaint();
	}
	
	public void choose_sort(int i) {
		sort = i;
		switch (sort) {
		case 1:
			//Quick Sort
			stack = new int[numbers.length - 1];
			top = -1;
			stack[++top] = 0;
			stack[++top] = numbers.length-1;
			high = numbers.length-1;
			low = 0;
			break;
		case 2:
			//Bubble Sort
			high = numbers.length;
			low = 0;
			break;
		case 3:
			//Heap Sort
			high = 1;
			low = 0;
			top = 0;
			break;
		case 4:
			//Insertion Sort
			high = numbers.length;
			low = 1;
			break;
		}
		
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if (numbers != null && sort == 0) {
			for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
				g.setColor(Color.GREEN);
				g.fillRect(i*UNITSIZE, 0, UNITSIZE, numbers[i]);
				g.setColor(Color.BLACK);
				g.drawRect(i*UNITSIZE, 0, UNITSIZE, numbers[i]);
			}
		}
		switch (sort) {
		case 1:
			quick_sort(g);
			break;
		case 2:
			if (low < high) {
				bubble_sort(g);				
				low++;
			} else {
				sort_finish();
			}
			break;
		case 3:
			//Outer Loop and Innter Loop
			if (high <= numbers.length-1 && low < numbers.length - 1) {
				int mid = Math.min(low + high - 1, numbers.length - 1);
				
				int right = Math.min(low + (2*high) - 1, numbers.length - 1);
				
				merge_sort(g, low, mid, right);
				
				//Iterates Inner Loop
				low += 2*high;
			} else if (high <= numbers.length-1) {
				//Iterates Outer Loop
				//System.out.println("Iterating Outer Loop");
				high = 2 * high;
				low = 0;
				for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
					g.setColor(Color.BLUE);
					g.fillRect(i*UNITSIZE, 0, UNITSIZE, numbers[i]);
					g.setColor(Color.BLACK);
					g.drawRect(i*UNITSIZE, 0, UNITSIZE, numbers[i]);
				}
				Interface.progress.append("Preparing for next Merge Sort\n");
				
			} else {
				sort_finish();
			}
			//System.out.println("Left = " + low + "|High = " + high);
			break;
		case 4:
			if (low < high) {
				insertion_sort(g);
				low++;
			} else {
				sort_finish();
			}
			break;
		}
		
	}
	
	private void draw_rect(Graphics g, int loc) {
		g.setColor(Color.YELLOW);
		g.fillRect(loc*UNITSIZE, 0, UNITSIZE, numbers[loc]);
		g.setColor(Color.BLACK);
		g.drawRect(loc*UNITSIZE, 0, UNITSIZE, numbers[loc]);
	}
	
	private void clear_rect(Graphics g, int loc) {
		g.setColor(getBackground());
		g.fillRect(loc*UNITSIZE, 0, UNITSIZE, SCREENHEIGHT);
		g.drawRect(loc*UNITSIZE, 0, UNITSIZE, SCREENHEIGHT);
	}
	
	private void sort_finish() {
		sort = 0;
		timer.stop();
		repaint();
		Interface.showNumbers.setText("");
		for (int i: numbers) {
			Interface.showNumbers.append(i + "\n");
			//System.out.println(i);
			
		}
		Interface.progress.append("Done!");
	}
	
	private void quick_sort(Graphics g) {
		if (top >= 0) {
			high = stack[top--];
			low = stack[top--];
			
			int p = partition(g);
			
			if (p-1 > low) {
				stack[++top] = low;
				stack[++top] = p-1;
			}
			
			if (p + 1 < high) {
				stack[++top] = p + 1;
				stack[++top] = high;
			}
		} else {
			sort_finish();
		}
	}
	
	private int partition(Graphics g) {
		int pivot = numbers[high];
		
		int i = (low - 1);
		
		for(int j = low; j <= high - 1; j++) {
			for (int x = 0; x < SCREENWIDTH/UNITSIZE; x++) {
				if (x != j) {
					clear_rect(g, x);
					draw_rect(g, x);
				
			}
				
			}
			if (numbers[j] <= pivot) {
				i++;
				
				int temp = numbers[i];
				numbers[i] = numbers[j];
				numbers[j] = temp;
				
				clear_rect(g, i);
				draw_rect(g, i);
				g.setColor(Color.GREEN);
				g.fillRect(i*UNITSIZE, 0, UNITSIZE, numbers[i]);
				g.setColor(Color.BLACK);
				g.drawRect(i*UNITSIZE, 0, UNITSIZE, numbers[i]);
				
				clear_rect(g,j);
				g.setColor(Color.RED);
				g.fillRect(j*UNITSIZE, 0, UNITSIZE, numbers[j]);
				g.setColor(Color.BLACK);
				g.drawRect(j*UNITSIZE, 0, UNITSIZE, numbers[j]);
				Interface.progress.append("Swapping:" + numbers[i] + " And " + numbers[j] + "\n");
				
			}
		}
		
		int temp = numbers[i + 1];
		numbers[i + 1] = numbers[high];
		numbers[high] = temp;
		
		for (int x = 0; x < SCREENWIDTH/UNITSIZE; x++) {
			clear_rect(g, x);
			draw_rect(g, x);
		}
		
		clear_rect(g, i + 1);
		draw_rect(g, i + 1);
		g.setColor(Color.GREEN);
		g.fillRect((i + 1)*UNITSIZE, 0, UNITSIZE, numbers[i + 1]);
		g.setColor(Color.BLACK);
		g.drawRect((i + 1)*UNITSIZE, 0, UNITSIZE, numbers[i + 1]);
		
		clear_rect(g, high);
		g.setColor(Color.RED);
		g.fillRect(high*UNITSIZE, 0, UNITSIZE, numbers[high]);
		g.setColor(Color.BLACK);
		g.drawRect(high*UNITSIZE, 0, UNITSIZE, numbers[high]);
		
		
		return i+1;
	}
	
	private void bubble_sort(Graphics g) {
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			clear_rect(g, i);
			draw_rect(g, i);
		}
		for (int j = 0; j < high - low - 1; j++) {
			if (numbers[j] > numbers[j + 1]) {
				int temp = numbers[j];
				numbers[j] = numbers[j + 1];
				numbers[j + 1] = temp;
				Interface.progress.append("Swapping:" + numbers[j] + " and " + numbers[j+1] + "\n");
				
				clear_rect(g, j);
				g.setColor(Color.BLUE);
				g.fillRect(j*UNITSIZE, 0, UNITSIZE, numbers[j]);
				g.setColor(Color.BLACK);
				g.drawRect(j*UNITSIZE, 0, UNITSIZE, numbers[j]);
			} else {
				g.setColor(Color.RED);
				g.fillRect(j*UNITSIZE, 0, UNITSIZE, numbers[j]);
				g.setColor(Color.BLACK);
				g.drawRect(j*UNITSIZE, 0, UNITSIZE, numbers[j]);
			}
			
			
		}
		
	}
	
	private void merge_sort(Graphics g, int l, int m, int r) {
		timer.stop();
		int i, j, k;
		top = 0;
		//left array size
		int n1 = m - l + 1;
		
		//right array size
		int n2 = r - m;
		Interface.progress.append("Create Array of size " + n1 + "\n");
		//temp Arrays
		int L[] = new int[n1];
		int R[] = new int[n2];
		
		//Fill Temp Arrays
		for (i = 0; i < n1; i++) {
			L[i] = numbers[l + i];
		}
		for (j = 0; j < n2; j++) {
			R[j] = numbers[m + 1 + j];
		}
		
		i = 0;
		j = 0;
		Interface.progress.append("Adding from Temp Arrays\n");
		for (top = 0; top < l; top++) {
			draw_rect(g, top);
		}
		k = l;
		top = l;
		while (i < n1 && j < n2) {
			if (L[i] <= R[j]) {
				numbers[k] = L[i];
				
				draw_left(g, L, i);
				top++;
				
				i++;
			} else {
				numbers[k] = R[j];
				
				draw_right(g, L, j);
				top++;
				
				j++;
			}
			k++;
		}
		
		//Copy Remaining Elements
		while (i < n1) {
			draw_left(g, numbers, k);
			top++;
			numbers[k] = L[i];
			i++;
			k++;
		}
		while (j < n2) {
			draw_right(g, numbers, k);
			top++;
			numbers[k] = R[j];
			j++;
			k++;
		}
		
		for (top = k; top < numbers.length; top++) {
			draw_rect(g, top);
		}
		Interface.progress.append("Merging Arrays\n");
		//System.out.println("Merge sort loop: " + k + ": " + (numbers.length-1) + "\n\n");
		timer.restart();
	}
	
	private void draw_left(Graphics g, int[] arr, int length) {
		g.setColor(Color.RED);
		g.fillRect(top*UNITSIZE, 0, UNITSIZE, arr[length]);
		g.setColor(Color.BLACK);
		g.drawRect(top*UNITSIZE, 0, UNITSIZE, arr[length]);
	}
	
	private void draw_right(Graphics g, int[] arr, int length) {
		g.setColor(Color.BLUE);
		g.fillRect(top*UNITSIZE, 0, UNITSIZE, arr[length]);
		g.setColor(Color.BLACK);
		g.drawRect(top*UNITSIZE, 0, UNITSIZE, arr[length]);
	}
	
	private void insertion_sort(Graphics g) {
		for (int i = 0; i < SCREENWIDTH/UNITSIZE; i++) {
			draw_rect(g, i);
		}
		int key = numbers[low];
		int prev = low - 1;
		
		while (prev >= 0 && numbers[prev] > key) {
			if (prev + 1 < high && prev >= 0) {
				clear_rect(g, prev + 1);
				g.setColor(Color.BLUE);
				g.fillRect((prev + 1)*UNITSIZE, 0, UNITSIZE, numbers[prev + 1]);
				g.setColor(Color.BLACK);
				g.drawRect((prev + 1)*UNITSIZE, 0, UNITSIZE, numbers[prev + 1]);
				
				clear_rect(g, prev);
				g.setColor(Color.RED);
				g.fillRect(prev*UNITSIZE, 0, UNITSIZE, numbers[prev]);
				g.setColor(Color.BLACK);
				g.drawRect(prev*UNITSIZE, 0, UNITSIZE, numbers[prev]);
			}
			numbers[prev + 1] = numbers[prev];
			Interface.progress.append("Inserting: " + numbers[prev] + " and " + key + "\n");
			prev -= 1;
		}
		
		numbers[prev + 1] = key;
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
}
