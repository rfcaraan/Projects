import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JTextPane;

public class Menu extends JFrame implements ActionListener{
	private Risk_Manager riskManager;
	private LogIn logInAuth;
	private CardLayout menuCards;
	
	//Log In Page
	private JPanel logInPanel;
	private JTextField userName;
	private JTextField password;
	private JButton btnLogIn;
	
	//Employee Page
	private JPanel riskPanel;
	private JTable tableMain;
	private JButton btnModifyRisk;
	private JButton btnAddRisk;
	private JButton btnLogOut;
	private JButton btnRemoveRisk;
	
	//Add Risk Panel
	private JPanel addPanel;
	
	//Risk Manager text
	private int id;
	private String title;
	private String	statement;
	private int level;
	
	
	private JPanel Cards;
	
	private String adminUserName;
	private String adminPassword;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setTitle("Risk Manager");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		setBounds(250, 250, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuCards = new CardLayout();
		Cards = new JPanel();
		Cards.setLayout(menuCards);
		getContentPane().add(Cards);
		
		riskManager = new Risk_Manager();
		logInAuth = new LogIn();
	
		adminUserName = "SUPERVISOR";
		adminPassword = "PASSWORD";
		
		btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(this);
		tableMain = new JTable();
		create_log_in();
		
		menuCards.first(Cards);
	}
	
	//Creates Log In User Interface
	private void create_log_in() {
		logInPanel = new JPanel();

		
		JSplitPane split = new JSplitPane();
		split.setBounds(100, 100, 300, 50);
		
		JSplitPane split1 = new JSplitPane();
		split1.setSize(50, 50);
		userName = new JTextField();
		password = new JTextField();
		split1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		split1.setLeftComponent(userName);
		split1.setRightComponent(password);
		
		JLabel lbl1 = new JLabel("Username: ");
		JLabel lbl2 = new JLabel("Password");
		JSplitPane split2 = new JSplitPane();
		split2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		split2.setLeftComponent(lbl1);
		split2.setRightComponent(lbl2);
		
		split.setLeftComponent(split2);
		split.setRightComponent(split1);
		
		btnLogIn = new JButton("Log In");
		btnLogIn.addActionListener(this);
		btnLogIn.setBounds(100, 175, 100, 25);
		
		//Default Log In and Password for Debugging
		/*JButton debug = new JButton("DEBUG(Temp Button)");
		debug.setBounds(100, 200, 175, 25);
		debug.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String msg = "Default Log In Information: \nUsername = " + adminUserName + "\nPassword = " + adminPassword;
				JOptionPane.showMessageDialog(logInPanel.getParent(), msg);
			}
		});
		//End of Log In Password for Debugging
		*/
		logInPanel.setLayout(null);
		logInPanel.add(split);
		logInPanel.add(btnLogIn);
		//logInPanel.add(debug);
		Cards.add(logInPanel, "logInMenu");
	}
	
	//Creates Risk Manager User Interface
	private void create_risk_table() {
		riskPanel = new JPanel();
		riskPanel.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		//btnLogOut = new JButton("Log Out");
		//btnLogOut.addActionListener(this);
		panel.add(btnLogOut);
		
		JSplitPane split = new JSplitPane();
		split.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		btnAddRisk = new JButton("Add Risk");
		btnAddRisk.addActionListener(this);
		btnRemoveRisk = new JButton("Remove Risk");
		btnRemoveRisk.addActionListener(this);
		
		split.setLeftComponent(btnAddRisk);
		split.setRightComponent(btnRemoveRisk);
		panel.add(split);
		
		btnModifyRisk = new JButton("Modify");
		btnModifyRisk.addActionListener(this);
		
		riskPanel.add(btnModifyRisk, BorderLayout.NORTH);
		riskPanel.add(tableMain, BorderLayout.CENTER);
		riskPanel.add(panel, BorderLayout.SOUTH);
		Cards.add(riskPanel, "riskPanel");
	}
	
	//Creates User Interface for Adding New Risks
	private void add_risk() {
		addPanel = new JPanel();
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
		addPanel.setBounds(100, 100, 100, 100);
		
		JSplitPane idSplit = new JSplitPane();
		idSplit.setSize(50,  50);
		idSplit.setResizeWeight(0.25);
		JLabel lbl1 = new JLabel("Risk ID:");
		lbl1.setSize(50, 50);
		JTextField idTxt = new JTextField();
		idTxt.setSize(50, 50);
		idSplit.setLeftComponent(lbl1);
		idSplit.setRightComponent(idTxt);
		
		JSplitPane titleSplit = new JSplitPane();
		titleSplit.setSize(50, 50);
		titleSplit.setResizeWeight(0.23);
		JLabel lbl2 = new JLabel("Risk Title:");
		JTextField titleTxt = new JTextField();
		titleSplit.setLeftComponent(lbl2);
		titleSplit.setRightComponent(titleTxt);
		
		JSplitPane stmtSplit = new JSplitPane();
		stmtSplit.setSize(50, 50);
		stmtSplit.setResizeWeight(0.15);
		JLabel lbl3 = new JLabel ("Risk Statement");
		JTextField stmtTxt = new JTextField();
		stmtSplit.setLeftComponent(lbl3);
		stmtSplit.setRightComponent(stmtTxt);
		
		JSplitPane lvlSplit = new JSplitPane();
		lvlSplit.setSize(50, 50);
		lvlSplit.setResizeWeight(0.22);
		JLabel lbl4 = new JLabel("Risk Level");
		JTextField lvlTxt = new JTextField();
		lvlSplit.setLeftComponent(lbl4);
		lvlSplit.setRightComponent(lvlTxt);
		
		JSplitPane button = new JSplitPane();
		button.setSize(25, 25);
		button.setResizeWeight(.5);
		JButton addRisk = new JButton("Add Risk");
		addRisk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean correctInput = false;
				if (idTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(addPanel.getParent(), "Invalid Risk ID");
				} else if (titleTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(addPanel.getParent(), "Invalid Risk Title");
				} else {
					try {
						id = Integer.parseInt(idTxt.getText());
						title = titleTxt.getText();
						if (stmtTxt.getText().isEmpty()) {
							statement = " ";
						} else 
							statement = stmtTxt.getText();
						if (lvlTxt.getText().isEmpty()) {
							level = 1;
						} else {
							level = Integer.parseInt(lvlTxt.getText());
							if (level < 1  || level > 5) {
								JOptionPane.showMessageDialog(addRisk.getParent(), "Risk Level Must be Between 1-5!");
							} else {
								correctInput = true;
							}
						}
						
						if (correctInput) {
							System.out.println("Adding: " + id + ", " + title + ", " + statement + ", " + level);
							riskManager.add_risk(id, title, statement, level);
							idTxt.setText("");
							titleTxt.setText("");
							stmtTxt.setText("");
							lvlTxt.setText("");
							setSize(750,750);
							risk_refresh();
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(addPanel.getParent(), "Risk ID and Risk Level Must be Numbers!");
					}
				}
				
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSize(750,750);
				risk_refresh();
			}
		});
		button.setLeftComponent(cancel);
		button.setRightComponent(addRisk);
		
		addPanel.add(idSplit);
		addPanel.add(titleSplit);
		addPanel.add(stmtSplit);
		addPanel.add(lvlSplit);
		addPanel.add(button);
		
		Cards.add(addPanel, "addPanel");
	}
	
		
		
	public void actionPerformed(ActionEvent e) {
		boolean report = true;
		//log out
		if (e.getSource() == btnLogOut) {
			setSize(450,300);
			menuCards.first(Cards);
			
		//Log In
		} else if (e.getSource() == btnLogIn) {
			
			if (logIn()) {
				create_risk_table();
				risk_refresh();
				add_risk();
				//System.out.println("Log In Successful");
				setSize(750,750);
			} else {
				JOptionPane.showMessageDialog(this, "Invalid Log In Information!");
			}
			
			
		//Modify Selected Risk in Table
		} else if (e.getSource() == btnModifyRisk) {
			//To Be Added
			riskManager.modify_risk(tableMain);
			risk_refresh();
			
		//Adding New Risk to Table
		} else if (e.getSource() == btnAddRisk) {
			System.out.println("Adding to Table");
			setSize(400, 300);
			//risk_refresh();
			//menuCards.first(Cards);
			menuCards.show(Cards, "addPanel");
			
		//Removing Selected Risk From Table
		} else if (e.getSource() == btnRemoveRisk) {
			//System.out.println("Item Returning");
			riskManager.remove_risk(tableMain);
			risk_refresh();
		}
	}
	
	//Refresh Risk Table
	private void risk_refresh() {
		menuCards.first(Cards);
		riskPanel.remove(tableMain);
		tableMain = riskManager.fill_table();
		//System.out.println("Table Filled");
		
		tableMain.setFont(new Font("Times New Roman", Font.BOLD, 15));
		riskPanel.add(tableMain, BorderLayout.CENTER);
		
		menuCards.show(Cards, "riskPanel");
	}
	
	//Checks if Username and Password are valid
	//Return true if Log in is Successful
	private boolean logIn() {
		//Debugging Log In Verification (No Database Connection Required)
		/*if (password.getText().contentEquals(adminPassword)) {
			if (userName.getText().equals(adminUserName)) {
				return true;
			}
		}*/
		//End of Debugging Log In Verification
		
		//Verify if Log In Information is Valid
		boolean logInStatus = logInAuth.log_in(userName.getText(), password.getText());
		riskManager.set_database(logInAuth.get_database());
		return logInStatus;
	}

		
}
