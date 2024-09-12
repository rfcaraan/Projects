import java.sql.ResultSet;

//Authenticates Log In Inforamtion by Connecting to Log In Database
public class LogIn extends Database{
	private String dbAccess;
	
	public LogIn() {		
		create_connection();
	}
	
	public boolean log_in(String userName, String password) {
		try {
			s = conn.createStatement();
			//ResultSet rs = s.executeQuery("SELECT [Username] From " + employeeLogIn + " WHERE [Username] = '" + userName + "'");
			ResultSet rs = s.executeQuery("SELECT * From " + employeeLogIn + " WHERE [Username] = '" + userName + "'");
			rs.next();
			if (rs.getString(1).equals(userName) && rs.getString(2).equals(password)) {
				//System.out.println("Log in Successfull. " + rs.getString(1) + ": " + rs.getString(2) + ": " + rs.getString(3));
				System.out.println("Log In Successfull");
				dbAccess = rs.getString(3);
				return true;
			} else {
				System.out.println("Log in Failed: Invalid Password");
			}
			s.close();
		} catch (Exception e) {
			//System.out.println("Unable to Connect To Database");
			//e.printStackTrace();
			System.out.println("Log in Failed: Invalid Username.");
			return false;
		}
		return false;
	}
	
	public String get_database() {
		return dbAccess;
	}

}
