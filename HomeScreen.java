public class HomeScreen {

    public static void Run() {
		System.out.print("Welcome to BuzMo " + User.getName());
		if (User.getScreenName() != null) 
			{ System.out.println(" (" + User.getScreenName() + ")!"); } 
		else 
			{ System.out.println("!"); }
		UserOptions();
	}

	public static void UserOptions() {
		System.out.println("What would you like to do?");
		String prompts[] = {"Send message", "Check messages"};
		int answer = Menu.DisplayMenu(prompts);
		
		// Send messages
		if (answer == 1) {
			System.out.println("This hasn't been inplemented yet! Try something else.");
			UserOptions();
		}

		// Check messages
		else if (answer == 2 ) {
			System.out.println("This hasn't been implemented yet! Try something else.");
			UserOptions();
		}

		else {
			System.out.println("ERROR: Invald input. Please try again.");
			UserOptions();
		}
    }
}
