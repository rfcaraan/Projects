import java.sql.*;

//Class responsible for connecting to the Database and Holding the Table Names
public class Database {

	protected Connection conn;
	protected String url;
	protected Statement s;
	protected String database;
	protected String employeeLogIn;
	
	public Database() {
		//These are the Database Tables being Accessed
		//Change as Needed		
		employeeLogIn = "[Risk_Log_In]";
		//database = "[Risk_Manager]";
		
		create_connection();
	}
	
	
	protected void create_connection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			url = "jdbc:sqlserver://localhost;databaseName=Tools;user=newuser;password=login";
			//Connection
			//	DriverManager
			conn = DriverManager.getConnection(url);
			
			//clear_log();
			} catch (Exception e) {
				System.out.println("Unable to Connect");
				e.printStackTrace();
			}
	}
	
}
