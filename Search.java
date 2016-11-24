import java.sql.*;
import java.util.ArrayList;

public class Search {


    public static void Search() {
	System.out.println("------------Search Menu-------------");
	//String prompts[] = {"Email", "Topic words", "Last posting (in days)", "Number of posts in last week"};
	String prompts[] = {"Email"};
	String answers[] = Menu.PromptUser(prompts);
	
	String sql = "SELECT name, email FROM Users WHERE email = '" + answers[0] + "'";
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	ArrayList<String> emails = new ArrayList<String>();
	ArrayList<String> names = new ArrayList<String>();
	
	try {
	    while (rs.next()) {

		// you cannot search for yourself
		if (rs.getString(2).trim().equals(User.getEmail()))
		    continue;
		
		names.add(rs.getString(1).trim());
		emails.add(rs.getString(2).trim());

	    }
	} catch (Exception e) {System.out.println(e);}



	SQLHelper.Close();

	System.out.println(names.size() + " user(s) found according to search parameters:");
	for (int i = 0; i < names.size(); i++) {
	    System.out.println(names.get(i) + " (" + emails.get(i) + ")");
	}

	if (names.size() == 0)
	    return;

	
	String items[] = {"Send friend request", "Back to homescreen"};
	int answer = Menu.DisplayMenu("Woud you like to send a friend request?", items);
	if (answer == 1) {
	    String temp[] = new String[names.size()];
	    answer = Menu.DisplayMenu("Which user would you like to add?", names.toArray(temp));
	    if (answer <= 0 || answer > names.size())
		return;

	    MyCircle.SendFriendRequest(User.getName(), User.getEmail(), emails.get(answer-1));
	    
	}
	
    }
    

}
