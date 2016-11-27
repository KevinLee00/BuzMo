import java.sql.*;


public class Login {

    public static void main(String args[]) {

	String items[] = {"Login", "Create new account"};
	int answer = Menu.DisplayMenu("Welcome to BuzMo", items);


	if (answer == 1) {
	    String prompts[] = {"Email", "Password"};
	    String answers[] = Menu.PromptUser(prompts);
	    ResultSet rs = SQLHelper.ExecuteSQL("SELECT * FROM Users");
	    int success = 0;
	    String name = null, phone_num = null, email = null, pword = null, screen_name = null;
	    int manager = 0;
	    try {
		while (rs.next()) {
		    name = rs.getString(1).trim();
		    phone_num = rs.getString(2).trim();
		    email = rs.getString(3).trim();
		    pword = rs.getString(4).trim();
		    screen_name = rs.getString(5) == null ? null : rs.getString(5).trim();
		    manager = rs.getInt(6);
		    if (email.equals(answers[0]) && pword.equals(answers[1])) {
			success = 1;
			
			break;
		    }
		}
	    } catch(Exception e) { System.out.println(e);}
	    

	    SQLHelper.Close();


	    if (success == 1) {
		System.out.println("Successfully logged in!");
		User.setInfo(name, phone_num, email, pword, screen_name, manager);
		// ChatGroups.ManageCurrentChatGroups();
		HomeScreen.Run();
	    } else {
		System.out.println("Invalid username/password combo");
	    }
	    
	} else if (answer == 2) {
	    // create account
	    String prompts[] = {"Name", "Phone Number", "Email", "Password", "Screen Name"};
	    String answers[] = Menu.PromptUser( prompts );
	    String[] temp = new String[6];
	    temp[5] = "0";
	    for (int i = 0; i < 5; i++) {
		temp[i] = answers[i];
	    }

	    String types[] = {"STRING", "STRING", "STRING", "STRING", "STRING", "INTEGER"};
	    
	    SQLHelper.Insert("Users", temp, types);
	} else {
	    System.out.println("Error\n");
	}
	
	
    }
}
