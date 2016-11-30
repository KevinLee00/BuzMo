import java.sql.*;

public class User {
    private static String name, phone_num, email, pword, screen_name; // Used to login
    private static int is_manager;

    public static void setInfo(String name, String phone_num, String email, String pword, String screen_name, int is_manager) {
	User.name = name;
	User.phone_num = phone_num;
	User.email = email;
	User.pword = pword;
	User.screen_name = screen_name;
	User.is_manager = is_manager;

    }
    
    public static String getEmail() {
	return email;
    }

    public static String getName() {
	return name;
    }

    public static String getScreenName() {
	return screen_name;
    }

    public static int isManager() {
    	return is_manager;
    }

    
    public static String NameGivenEmail(String email) {
	String sql = "SELECT name, screen_name FROM Users WHERE email = '" + email + "'";
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	String screen_name = null;
	String name = null;
	try {
	    while (rs.next()) {
		name = rs.getString(1);
		screen_name = rs.getString(2);
	    }
	} catch (Exception e) {System.out.println(e);}


	SQLHelper.Close();

	if (screen_name != null)
	    return screen_name.trim();

	return name.trim();

    }

    public void setNewManager() { // NOT TESTED YET
    	ArrayList<String> userNames = new ArrayList<String>();
    	ArrayList<String> userEmails = new ArrayList<String>();
    	String sql = "SELECT * FROM Users WHERE isManager = '0'";
    	ResultSet rs = SQLHelper.ExecuteSQL(sql);
    	try {
    		while (rs.next()) {
    			userNames.add(rs.getString(1));
    			userEmails.add(rs.getString(3));
    		}
    	} 
    	catch (Exception e) {
    		System.out.println(e);
    	}

    	SQLHelper.Close();

    	if (userNames.size() == 0) {
    		System.out.println("There are no users that can be promoted to manager.");
    	}
    	else {
    		int answer = Menu.DisplayMenu("Who would you like to promote to manager?", userNames.toArray(new String[0]));
    		sql = "UPDATE Users SET isManager = '1' WHERE email = '" + userEmails.get(answer-1) + "'";
    		SQLHelper.ExecuteSQL(sql);
    		SQLHelper.Close();
    	}
    }

    public void runAnalytics() {
    	String items[] = {"Find the active users", "Find top messages", "Show number of inactive users", "Show complete report"};
    	int answer = Menu.DisplayMenu("What would you like to do?", items);

    	if (answer == 1) {

    	}
    	else if (answer == 2) {

    	}

    	else if (answer == 3) {
    		String sql = "SELECT sender FROM Messages GROUP BY sender HAVING 4 > COUNT(*)" 
    	}
    	else {
    		System.out.println("Invalid response");
    		HomeScreen.UserOptions();
    	}
    }


    

}
