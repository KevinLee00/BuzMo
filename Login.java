


public class Login {

    public static void main(String args[]) {

	System.out.println("Welcome to BuzMo\n");
	String items[] = {"Login", "Create new account"};
	int answer = Menu.DisplayMenu(items);


	if (answer == 1) {
	    // login

	} else if (answer == 2) {
	    // create account
	    String prompts[] = {"Name", "Phone Number", "Email", "Password", "Screen Name"};
	    String[] answers = Menu.PromptUser( prompts );
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
