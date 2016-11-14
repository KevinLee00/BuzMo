


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
	    SQLHelper.Insert("Users", Menu.PromptUser( prompts ));
	} else {
	    System.out.println("Error\n");
	}
	
	
    }
}
