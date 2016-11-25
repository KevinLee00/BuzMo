public class HomeScreen {

    public static void Run() {
		System.out.print("Welcome to BuzMo " + User.getName());
		if (User.getScreenName() != null) 
			{ System.out.println(" (" + User.getScreenName() + ")"); } 
		else 
			{ System.out.println(""); }

		MyCircle.CheckForFriendRequests();
		UserOptions();
	}

	public static void UserOptions() {
	    String prompts[] = {"Send message", "Check inbox", "View sent messages", "Search for user", "Logout"};

		while (true) {
		
		    int answer = Menu.DisplayMenu("What would you like to do?", prompts);

		
		    // Send messages
		    if (answer == 1) {
			Messages.ComposeMessage();
		    }

		    // Check inbox
		    else if (answer == 2 ) {
			Messages.CheckInbox();
		    } else if (answer == 3) {
			Messages.ViewSentMessages();
		    } else if (answer == 4) {
			Search.Search();
		    } else if (answer == 5) {
			System.out.println("Sucessfully logged out. Goodbye!");
			System.exit(0);
		    }

		    else {
			System.out.println("ERROR: Invald input. Please try again.");
		    }

		}
    }
}
