import java.sql.*;


public class Login {

    public static void main(String args[]) {

	System.out.println("Welcome to BuzMo\n");
	String items[] = {"Login", "Create new account"};
	int answer = Menu.DisplayMenu(items);


	if (answer == 1) {
	    String prompts[] = {"Email", "Password"};
	    String answers[] = Menu.PromptUser(prompts);
	    ResultSet rs = SQLHelper.ExecuteSQL("SELECT email, pword FROM Users");
	    int success = 0;
	    try {
		while (rs.next()) {
		    String email = rs.getString(1).trim();
		    String pword = rs.getString(2).trim();
		    if (email.equals(answers[0]) && pword.equals(answers[1])) {
			success = 1;
			break;
		    }
		}
	    } catch(Exception e) { System.out.println(e);}
	    

	    SQLHelper.Close();


	    if (success == 1) {
		System.out.println("Successfully logged in!");
		User.setEmail(answers[0]);
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
