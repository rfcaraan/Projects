import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class Main extends JFrame{

	Main(){
		this.setTitle("Connect 4 With Minimax Algorithm");
		this.setResizable(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		//Main Components
		Game gamePanel = new Game();
		this.add(gamePanel, BorderLayout.CENTER);
		this.add(menu(gamePanel), BorderLayout.SOUTH);
		
		//Build JFrame
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private JPanel menu(Game gamePanel) {
		JPanel menuPanel = new JPanel();
		
		//New Game Button
		JButton newGame = new JButton("New Game");
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.new_game();
			}
		});
		menuPanel.add(newGame);
		return menuPanel;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
