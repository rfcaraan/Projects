import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Simple GUI
public class Interface extends JFrame{
	
	Interface(){
		this.setTitle("Sudoku Solver With Backtracking Algorithm");
		this.setResizable(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		//Fill Components
		Sudoku sudoku = new Sudoku();
		this.add(menu(sudoku), BorderLayout.SOUTH);
		this.add(sudoku, BorderLayout.CENTER);
		
		//Build Components
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private JPanel menu(Sudoku board) {
		//Build Menu Panel
		JPanel menuPanel = new JPanel();
		
		JButton newBoard = new JButton("New Board");
		newBoard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.new_board();
			}
		});
		menuPanel.add(newBoard);
		
		JButton clearBoard = new JButton("Clear Board");
		clearBoard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.clear_board();
			}
		});
		menuPanel.add(clearBoard);
		
		JButton solve = new JButton("Solve");
		solve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.solve();
			}
		});
		menuPanel.add(solve);
		
		int[] speedSettings = {25, 50, 75, 100};
		JComboBox speed = new JComboBox();
		for (int item: speedSettings)
			speed.addItem(item);
		speed.setSelectedIndex(0);
		menuPanel.add(speed);
		
		JButton showSolve = new JButton("Show Solution");
		showSolve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.show_solve((int) speed.getSelectedItem());
			}
		});
		menuPanel.add(showSolve);
		
		return menuPanel;
	}

	public static void main(String[] args) {
		new Interface();
	}

}
