public class HomeScreen {

    public static void Run() {
		System.out.print("Welcome to BuzMo " + User.getName());
		if (User.getScreenName() != null) 
			{ System.out.println(" (" + User.getScreenName() + ")"); } 
		else 
			{ System.out.println(""); }

		MyCircle.CheckForFriendRequests();
		ChatGroups.CheckForChatGroupRequests();
		
		if (User.isManager() == 1) {
			ManagerOptions();
		}
		else {
			UserOptions();
		}
	}

	public static void UserOptions() {
	    String prompts[] = {"Send message", "Check inbox", "View sent messages", "Search for user", "Create a chat group", "View messages in a chat group", "Manage existing chat groups", "Search MyCircle Messages", "Add/delete topic words", "Logout"};

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
			Search.SearchUsers();
		    } else if (answer == 5) {
			ChatGroups.CreateChatGroup();
		    } else if (answer == 6) {
			ChatGroups.ViewChatGroupMessages();
		    } else if (answer == 7) {
			ChatGroups.ManageCurrentChatGroups();
		    } else if (answer == 8) {

			Search.SearchMessages();
		    } else if (answer == 9) {
			User.EditTopicWords();
		    } else if (answer == 10) {
			System.out.println("Sucessfully logged out. Goodbye!");
			System.exit(0);
		    } else if (answer == 1000) { //Update Time for the sake of the presentation project
		    User.updateDatabaseTime();
		    }

		    else {
			System.out.println("ERROR: Invald input. Please try again.");
		    }

		}
    }

    public static void ManagerOptions() {
    	String prompts[] = {"Send message", "Check inbox", "View sent messages", "Search for user", "Create a chat group", "View messages in a chat group", "Manage existing chat groups", "Search MyCircle Messages", "Add/delete topic words", "Get BuzMo analytics", "Appoint new manager", "Logout"};

		while (true) {
		
		    int answer = Menu.DisplayMenu("What would you like to do?", prompts);

		    // Send messages
		    if (answer == 1) {
			Messages.ComposeMessage();
		    } else if (answer == 2 ) {
			Messages.CheckInbox();
		    } else if (answer == 3) {
			Messages.ViewSentMessages();
		    } else if (answer == 4) {
			Search.SearchUsers();
		    } else if (answer == 5) {
			ChatGroups.CreateChatGroup();
		    } else if (answer == 6) {
			ChatGroups.ViewChatGroupMessages();
		    } else if (answer == 7) {
			ChatGroups.ManageCurrentChatGroups();
		    } else if (answer == 8) {
			Search.SearchMessages();
		    } else if (answer == 9) {
			User.EditTopicWords();
		    } else if (answer == 10) {
			User.runAnalytics();
		    } else if (answer == 11) {
			User.setNewManager();
		    } else if (answer == 12) {
			System.out.println("Sucessfully logged out. Goodbye!");
			System.exit(0);
		    } else if (answer == 1000) { //Update Time for the sake of the presentation project
			User.updateDatabaseTime();
		    }
		    else {
			System.out.println("ERROR: Invald input. Please try again.");
		    }
		}
	}
}
