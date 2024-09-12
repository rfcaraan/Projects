import java.awt.Color;
import java.awt.Component;
import java.sql.*;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Risk_Manager extends Database{
	
	/*Expected Risk Database Format:
	 * 
	 * Risk ID(int) | Risk Title | Risk Statement | Risk Level
	 * 123				example		Sample Stmnt		1
	 */
	private final Color[] tableColor = {Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED}; 
	
	public Risk_Manager() {
		super();
	}
	
	//Creates table model for the JTable
	public JTable create_table(DefaultTableModel tableModel) {
		tableModel.addColumn("Risk ID");
		tableModel.addColumn("Risk Title");
		tableModel.addColumn("Risk Statement");
		tableModel.addColumn("Risk Level");
		JTable riskTable = new JTable(tableModel);
		return riskTable;
	}
	
	
	//Returns new filled table
	//Called whenever the Database is updated in order to get the updated table
	public JTable fill_table() {
		JTable table = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel();
		try {
			s = conn.createStatement();
			table = create_table(tableModel);
			
			table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				@Override
			    public Component getTableCellRendererComponent(JTable table,
			            Object value, boolean isSelected, boolean hasFocus, int row, int col) {

			        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

			        int level = (int) table.getModel().getValueAt(row, 3);
			        switch (level) {
					case 1:
						setBackground(tableColor[0]);
						setForeground(Color.BLACK);
						break;
					case 2:
						setBackground(tableColor[1]);
						setForeground(Color.BLACK);
						break;
					case 3:
						setBackground(tableColor[2]);
						setForeground(Color.BLACK);
						break;
					case 4:
						setBackground(tableColor[2]);
						setForeground(Color.BLACK);
						break;
					case 5:
						setBackground(tableColor[3]);
						setForeground(Color.WHITE);
						break;
					}

			        return this;
			    }
			});
			
			tableModel.setRowCount(0);
			ResultSet rs = s.executeQuery("SELECT * FROM " + database);
			while(rs.next()) {
				tableModel.addRow(new Object[] {rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)});
				//System.out.println(rs.getString(1) + ", " + rs.getInt(2) + rs.getString(3));
			}
			s.close();
		} catch (Exception e) {
			System.out.println("Unable to fill table");
			System.out.println(conn);
			e.printStackTrace();
		}
		//System.out.println("Returning Table");
		return table;
	}
	
	//Add new Risk to Risk Manager Table
	public void add_risk(int id, String title, String statement, int level) {
		try {
			s = conn.createStatement();
			s.executeQuery("INSERT INTO " + database + "([Risk ID] , [Risk Title], [Risk Statement], [Risk Level])"
					+ "VALUES (" + id + ", \'" + title + "\', \'" + statement + "\', " + level + ");");
			s.close();
		} catch (Exception e){
			System.out.println("Unable to Add");
			System.out.println(conn);
			e.printStackTrace();
		}
	}
	
	//Remove Risk From Risk Manager Table
	public void remove_risk(JTable tableMain) {
		try {
			int row = tableMain.getSelectedRow();
			if (row >= 0) {
				//System.out.println(row);
				String name = (String) tableMain.getValueAt(row, 1);
				s = conn.createStatement();
				s.executeQuery("DELETE FROM " + database + " WHERE [Risk Title]=\'" + name + "\'");
				//System.out.println(name);
				s.close();
			}  else {
				JOptionPane.showMessageDialog(tableMain.getParent(), "Nothing Selected");
			}
		} catch (Exception e) {
			System.out.println("Remove Failed");
			e.printStackTrace();
		}
	}
	
	//Modifies Selected Risk in Risk Table
	public void modify_risk(JTable tableMain) {
		try {
			int row = tableMain.getSelectedRow();
			int column = tableMain.getSelectedColumn();
			String colName = "";
			if (row >= 0 || column >= 0) {
				//System.out.println(row);
				boolean intInput = false;
				String strValue = "";
				int intValue = 0;
				
				try {
					switch (column) {
					case 1:
						colName = "[Risk Title]";
						strValue = JOptionPane.showInputDialog(tableMain.getParent(), "Enter New Risk Title:");
						break;
					case 2:
						colName = "[Risk Statement]";
						strValue = JOptionPane.showInputDialog(tableMain.getParent(), "Enter New Risk Statement:");
						break;
					case 0:
						colName = "[Risk ID]";
						intValue = Integer.parseInt(JOptionPane.showInputDialog(tableMain.getParent(), "Enter New Risk ID:"));
						intInput = true;
						break;
					case 3:
						colName = "[Risk Level]";
						while (intValue < 1 || intValue > 5) {
							intValue = Integer.parseInt(JOptionPane.showInputDialog(tableMain.getParent(), "Enter New Risk Level(1-5):"));
							intInput = true;
						}
						break;
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(tableMain.getParent(), "Inputs must be numbers for Risk ID and Risk Levels");
				}
				String name = (String) tableMain.getValueAt(row, 1);
				if (intInput) {
					//int name = (int) tableMain.getValueAt(row, 1);
					System.out.println("UPDATE " + database
							+ "SET " + colName + " = " + intValue + " " 
							+ "WHERE [Risk Title] = \'"+ name + "\'");
					s = conn.createStatement();
					s.executeQuery("UPDATE " + database
							+ "SET " + colName + " = " + intValue + " " 
							+ "WHERE [Risk Title] = \'"+ name + "\'");
					
					s.close();
				} else {
					s = conn.createStatement();
					s.executeQuery("UPDATE " + database
							+ "SET " + colName + " = \'" + strValue + "\' " 
							+ "WHERE [Risk Title] = \'"+ name + "\'");
					
					s.close();
				}
				
					
			}  else {
				JOptionPane.showMessageDialog(tableMain.getParent(), "Nothing Selected");
			}
		} catch (Exception e) {
			System.out.println("Editing Failed");
			e.printStackTrace();
		}
	}
	
	public void set_database(String databaseName) {
		database = "[" + databaseName + "]";
	}
}
