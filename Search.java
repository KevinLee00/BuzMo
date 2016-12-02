import java.sql.*;
import java.util.ArrayList;

public class Search {


    public static void SearchUsers() {
	System.out.println("------------Search Menu-------------");
	//String prompts[] = {"Email", "Topic words", "Last posting (in days)", "Number of posts in last week"};
	String prompts[] = {"Email", "Topic Words"};
	String answers[] = Menu.PromptUser(prompts);
	String twords[] = answers[1].split(" ");
	
	String sql = "SELECT name, email FROM Users WHERE email = '" + answers[0] + "'";

	for (int i = 0; i < twords.length; i++) {
	    sql += " OR email IN (SELECT email FROM TopicWordsU WHERE word = '" + twords[i] + "')";
	}
	
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
	    System.out.println(User.NameGivenEmail(emails.get(i)));
	}

	if (names.size() == 0)
	    return;

	
	String items[] = {"Send friend request", "Back to homescreen"};
	int answer = Menu.DisplayMenu("Woud you like to send a friend request?", items);
	if (answer == 1) {
	    String temp[] = new String[names.size()];
	    answer = Menu.DisplayMenu("Which user would you like to add?", names.toArray(temp));
	    if (answer <= 0 || answer > names.size()) {
		System.out.println("Add failed due to: Invalid input");
		return;
	}

	    MyCircle.SendFriendRequest(User.getName(), User.getEmail(), emails.get(answer-1));
	    
	}
	
    }

    public static void SearchMessages() {
	String prompts[] = {"Topic Words"};
	String words[] = Menu.PromptUser(prompts)[0].split(" ");
	String sql = "SELECT * FROM Messages WHERE type = 3 AND id IN (SELECT id FROM TopicWordsM WHERE ";
	for (int i = 0; i < words.length; i++) {
	    sql += "word = '" + words[i] + "'";
	    if (i != words.length - 1)
		sql += " OR ";
	    else
		sql += ")";
	}

	ArrayList<Messages.MSG> messages = new ArrayList<Messages.MSG>();
	
	ResultSet rs = SQLHelper.ExecuteSQL(sql);

	try {
	    while (rs.next()) {
		messages.add(new Messages.MSG(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), rs.getString(4).trim(), null, rs.getString(5).trim(), rs.getInt(6)));
	    }
	} catch (Exception e) {System.out.println(e);}

	SQLHelper.Close();

	if (messages.size() == 0) {
	    System.out.println("There are no public messages with these topic words");
	    return;
	}

	
	System.out.println(messages.size() + " message(s) found");

	String names[] = new String[messages.size()];
	for (int i = 0; i < messages.size(); i++) {
	    Messages.MSG m = messages.get(i);
	    	    
	    names[i] = "From: " + User.NameGivenEmail(m.sender) + " [Circle]";

	}


	int answer = Menu.DisplayMenu("Which message would you like to read?", names);
	Messages.MSG m = messages.get(answer-1);
	Messages.PrintMessage(m);

	String message_id = m.id;
	String owner = m.owner;
	sql = "UPDATE Messages SET views = views+1 WHERE id = " + m.id + " AND owner = '" + m.owner + "'";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	
	
    }
    
    

}
