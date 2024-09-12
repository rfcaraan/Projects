import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class Interface extends JFrame{
	private Sorter sort;
	private Random random;
	private int[] numbers;
	int arraySize = Sorter.SCREENWIDTH/Sorter.UNITSIZE;
	static JTextArea showNumbers;
	static JTextArea progress;
	
	Interface(){
		this.setTitle("Sorting Visualizer");
		this.setResizable(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.add(menu(), BorderLayout.WEST);
		sort = new Sorter();
		this.add(sort, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private JPanel menu() {
		JPanel menuPanel = new JPanel();
		int width = 150;
		int height = 150;
		int minimum = 10;
		
		menuPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		menuPanel.setPreferredSize(new Dimension(width, Sorter.SCREENHEIGHT));
		
		showNumbers = new JTextArea();
		progress = new JTextArea();
		
		//Generates Random Numbers
		numbers= new int[arraySize];
		random = new Random();
		JButton generateArray = new JButton("Generate Numbers");
		generateArray.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Generating New Numbers");
				showNumbers.setText("");
				for (int i = 0; i < numbers.length; i++) {
					numbers[i] = random.nextInt(Sorter.SCREENHEIGHT - minimum) + minimum;
					if (i != numbers.length-1)
						showNumbers.append(numbers[i] + "\n");
					else
						showNumbers.append(numbers[i] + "");
					sort.add_array(numbers);
					progress.setText("New Numbers Generated!" + "\n");
				}
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipady = 10;
		gbc.insets = new Insets(10, 0, 0, 0);	
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		menuPanel.add(generateArray, gbc);
		
		//Shows Generated Numbers
		
		showNumbers.setEditable(false);
		showNumbers.setWrapStyleWord(true);
		showNumbers.setLineWrap(true);
		gbc.gridx = 0;
		gbc.gridy = 1;
		JScrollPane scroll = new JScrollPane(showNumbers, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(width, height));
		//scroll.add(showNumbers);
		menuPanel.add(scroll, gbc);
		
		//Shows Sorted Numbers
		progress.setEditable(false);
		progress.setWrapStyleWord(true);
		progress.setLineWrap(true);
		JScrollPane scroll2 = new JScrollPane(progress, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll2.setPreferredSize(new Dimension(width, height));
		
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		menuPanel.add(scroll2, gbc);
		
		//Shows Sorting Options
		//quicksort
		JButton quickSort = new JButton("Quick Sort");
		quickSort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quick Sort");
				sort.choose_sort(1);
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 3;
		menuPanel.add(quickSort, gbc);
		
		//Bubblesort
		JButton bubbleSort = new JButton("Bubble Sort");
		bubbleSort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Bubble Sort");
				sort.choose_sort(2);
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 4;
		menuPanel.add(bubbleSort, gbc);
		
		//Merge Sort
		JButton mergeSort = new JButton("Merge Sort");
		mergeSort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Merge Sort");
				sort.choose_sort(3);
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 5;
		menuPanel.add(mergeSort, gbc);
		
		//Insertion Sort
		JButton insertionSort = new JButton("Insertion Sort");
		insertionSort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Insertion Sort");
				sort.choose_sort(4);
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 6;
		menuPanel.add(insertionSort, gbc);
		
		
		return menuPanel;
	}
	
	public static void main(String[] args) {
		new Interface();
	}
}
