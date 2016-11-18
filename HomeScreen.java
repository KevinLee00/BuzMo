public class HomeScreen {

    public static void Run() {
	System.out.print("Welcome to BuzMo: " + User.getName());
	if (User.getScreenName() != null) {
	    System.out.println(" (" + User.getScreenName() + ")!");
	} else {
	    System.out.println("!");
	}
	System.out.println("What would you like to do?");
	
    }
}
