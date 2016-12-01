import java.sql.*;
import java.util.*;

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

    
    public static void updateDatabaseTime() {
    	// System.out.println("Please enter a new timestamp for the system. (ex. '2020-01-01 12:30:15.000')");
    	String prompts[] = {"Please enter a new timestamp for the system. (ex. '2020-01-01 12:30:15.000')"};
    	String answer[] = Menu.PromptUser(prompts);
    	String sql = "UPDATE CurrentTS SET current_timestamp = (TO_TIMESTAMP('" + answer[0] + "', 'YYYY-MM--DD HH24:MI:SS.FF'))";
   		SQLHelper.ExecuteSQL(sql);
   		SQLHelper.Close();
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

    public static void setNewManager() { // NOT TESTED YET
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

    public static void runAnalytics() {
    	String items[] = {"Find the active users", "Find top messages", "Show number of inactive users", "Show complete report"};
    	int answer = Menu.DisplayMenu("What would you like to do?", items);

    	if (answer == 1) { //Find most active users
    		// String sql = "SELECT sender FROM Messages WHERE "
    	}
    	else if (answer == 2) { //Find most viewed messages

    	}

    	else if (answer == 3) { // Find number of inactive users
        	ArrayList<String> usersWithMessages = new ArrayList<String>();
        	ArrayList<Integer> numberOfMessages = new ArrayList<Integer>();
        	String sql = "Select sender, COUNT(Messages.sender) FROM Messages GROUP BY sender HAVING 7 > COUNT(Messages.sender)";
    		ResultSet rs = SQLHelper.ExecuteSQL(sql);
    		try {
    			while (rs.next()) {
    				usersWithMessages.add(rs.getString(1));
    				numberOfMessages.add(rs.getInt(2));
    			}
    		}


    		catch (Exception e) {
    			System.out.println(e);
    		}
    		SQLHelper.Close();

    		ArrayList<String> usersWithPrivateMessages = new ArrayList<String>();
    		ArrayList<Integer> numberOfPrivateMessages = new ArrayList<Integer>();
    		sql = "Select sender, COUNT(Messages.sender) FROM Messages WHERE type = '0' GROUP BY sender";
    		rs = SQLHelper.ExecuteSQL(sql);
    		try {
    			while (rs.next()) {
    				usersWithPrivateMessages.add(rs.getString(1));
    				numberOfPrivateMessages.add(rs.getInt(2));
    			}
    		}
    		catch (Exception e) {
    			System.out.println(e);
    		}
    		SQLHelper.Close();

    		int numInactiveUsers = 0;
    		sql = "SELECT COUNT(U.email) FROM Users U WHERE U.email NOT IN (SELECT sender FROM Messages)";
    		rs = SQLHelper.ExecuteSQL(sql);
    		try {
    			while (rs.next()) {
    				numInactiveUsers = rs.getInt(1);
    			}
    		}
    		catch (Exception e) {
    			System.out.println(e);
    		}
    		SQLHelper.Close();

    		// Check for inactive useres who don't post at all
    		for (int i=0; i<usersWithPrivateMessages.size(); i++) {
    			for (int j=0; j<usersWithMessages.size(); j++) {
	    			if (usersWithPrivateMessages.get(i).equals(usersWithMessages.get(j))) {
	    				int messageAmount = numberOfMessages.get(j);
	    				int pMessageAmount = numberOfPrivateMessages.get(i);
	    				numberOfMessages.set(j, messageAmount - pMessageAmount/2);
	    			}
				}
    		}

    		//Check for inactive users who posted more 0 messages but less than 4
    		for (int i=0; i<usersWithMessages.size(); i++) {
    			if (numberOfMessages.get(i) < 4) {
    				numInactiveUsers++;
    			}
    		}

    		System.out.println("There are " + numInactiveUsers + " inactive users on BuzMo.");

    	}
    	else {
    		System.out.println("Invalid response");
    		
    		if (User.isManager() == 1) {
				HomeScreen.ManagerOptions();
			}
			else {
				HomeScreen.UserOptions();
			}
    	}
    }
}
