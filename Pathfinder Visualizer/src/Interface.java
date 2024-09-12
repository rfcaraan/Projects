import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;

public class Interface extends JFrame{
	Pathfinder pathfinder;
	
	Interface(){
		this.setTitle("Pathfinder Visualizer");
		this.setResizable(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pathfinder = new Pathfinder();
		this.setLayout(new BorderLayout());
		this.add(menu(), BorderLayout.WEST);
		this.add(pathfinder, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private JPanel menu() {
		JPanel menuPanel = new JPanel();
		int width = 200;
		int placement = 0;
		
		JRadioButton start = new JRadioButton("Start");
		JRadioButton end = new JRadioButton("End");
		JRadioButton wall = new JRadioButton("Wall");
			
		JRadioButton dij = new JRadioButton("Dijkstra\'s");
		JRadioButton aStar = new JRadioButton("A*");
		
		menuPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		menuPanel.setPreferredSize(new Dimension(width, Pathfinder.SCREENHEIGHT));
		int[] speed = {25, 50, 75, 100};
		int[] size = {25, 50, 100};
		
		JButton randBtn = new JButton("Random");
		randBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (start.isSelected())
					pathfinder.place_random(0);
				else if (end.isSelected())
					pathfinder.place_random(1);
				else if (wall.isSelected())
					pathfinder.place_random(2);
			}
		});
		gbc.gridx = 0;
		gbc.gridy = placement;
		placement++;
		gbc.ipady = 10;
		gbc.insets = new Insets(20, 0, 0, 0);	
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		menuPanel.add(randBtn, gbc);
		
		//Sets Option to place Start Point of Pathfinder
		JPanel pane1 = new JPanel();
		pane1.setLayout(new BoxLayout(pane1, BoxLayout.Y_AXIS));
		pane1.add(new JLabel("Algorithm Controls"));
		
		start.setSelected(true);
		start.setActionCommand("Start");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start Point");
				pathfinder.change_place_type(0);
			}
		});
		pane1.add(start);
		//Sets Option to place End Point of Pathfinder
		end.setSelected(false);
		end.setActionCommand("End");
		end.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("End Point");
				pathfinder.change_place_type(1);
			}
		});
		pane1.add(end);
		
		//Sets Option to Add Obstacles
		wall.setSelected(false);
		wall.setActionCommand("Wall");
		wall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Set Wall");
				pathfinder.change_place_type(2);
			}
		});
		pane1.add(wall);
		
		//Remove Wall Option
		JRadioButton remWall = new JRadioButton("Remove Wall");
		remWall.setSelected(false);
		remWall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Remove Wall");
				pathfinder.change_place_type(3);
			}
		});
		pane1.add(remWall);
		
		ButtonGroup selector = new ButtonGroup();
		selector.add(start);
		selector.add(end);
		selector.add(wall);
		selector.add(remWall);
		
		gbc.gridx = 1;
		gbc.gridy = placement;
		placement++;
		menuPanel.add(pane1, gbc);
		
		
		//Speed Selector
		JSplitPane speedSelect = new JSplitPane();
		
		speedSelect.setLeftComponent(new JLabel("Speed:"));
		JComboBox speedBox = new JComboBox(); 
		speedBox.setPreferredSize(new Dimension(15, 25));
		for (int i: speed) 
			speedBox.addItem(i); 
		speedBox.setSelectedIndex(0);
		speedBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pathfinder.set_speed(speed[speedBox.getSelectedIndex()]);
			}
		});
		speedSelect.setRightComponent(speedBox);
		gbc.gridx = 0;
		gbc.gridy = placement;
		placement++;
		menuPanel.add(speedSelect, gbc);
		
		//Change Grid Size
		JSplitPane gridSize = new JSplitPane();
		gridSize.setLeftComponent(new JLabel("Grid Size:"));
		JComboBox sizeBox = new JComboBox();
		for (int i: size)
			sizeBox.addItem(i);
		sizeBox.setSelectedIndex(1);
		sizeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pathfinder.new_unit_size(size[sizeBox.getSelectedIndex()]);
			}
		});
		gridSize.setRightComponent(sizeBox);
		gbc.gridx = 0;
		gbc.gridy = placement;
		placement++;
		menuPanel.add(gridSize, gbc);
		
		//Start, pause or stop pathfinder algorithm
		JPanel pane = new JPanel();
		//pane.setPreferredSize(new Dimension(15, 150));
		pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
		
		//start Pathfinder algorithm
		JButton startBtn  = new JButton("Str");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Starting");
				if (dij.isSelected())
					pathfinder.start(0);
				else if (aStar.isSelected())
					pathfinder.start(1);
			}
		});
		pane.add(startBtn);
		
		//Pause Pathfinder Algorithm
		JButton pauseBtn = new JButton("Pse");
		pauseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pausing");
				pathfinder.pause();
			}
		});
		pane.add(pauseBtn);
		
		//Stop Pathfinder Algorithm
		JButton stopBtn = new JButton("Stp");
		stopBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Stopping");
				pathfinder.stop();
			}
		});
		pane.add(stopBtn);
		gbc.gridx = 0;
		gbc.gridy = placement;
		placement++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 3;
		menuPanel.add(pane, gbc);
		
		//Algorithm Selector
		JPanel algPanel = new JPanel();
		algPanel.setLayout(new BoxLayout(algPanel, BoxLayout.Y_AXIS));
		ButtonGroup alg = new ButtonGroup();
		
		//Dijkstra's Algorithm
		dij.setActionCommand("Dij");
		dij.setSelected(true);
		alg.add(dij);
		algPanel.add(dij);
		
		//A* Algorithm
		aStar.setActionCommand("A*");
		aStar.setSelected(false);
		alg.add(aStar);
		algPanel.add(aStar);
		
		
		gbc.gridx = 1;
		gbc.gridy = placement;
		placement++;
		menuPanel.add(algPanel, gbc);
		
		return menuPanel;
	}
	
	public static void main(String[] args) {
		new Interface();
	}

}
