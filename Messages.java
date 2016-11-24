import java.util.*;
import java.sql.*;

public class Messages {
    public static final int PRIVATE_MSG = 0;
    public static final int CIRCLE_MSG = 1;
    public static final int CHATGRP_MSG = 2;

    public static void ComposeMessage() {
	String[] prompts = {"message content"};
	String text[] = Menu.PromptUser(prompts);

	String[] items = {"Private", "MyCirlce", "Chatgroup"};
	int answer = Menu.DisplayMenu("What kind of message is this?", items);

	if (answer == 1) {
	    ArrayList<String> friends = MyCircle.GetFriends(User.getEmail());
	    String temp[] = new String[friends.size()];

	    for (int i = 0; i < temp.length; i++) {
		temp[i] = User.NameGivenEmail(friends.get(i)) + " (" + friends.get(i) + ")";
	    }
	    
	    
	    answer = Menu.DisplayMenu("Pick a friend to send your message to", temp);
	    SendPrivateMessage(text[0], friends.get(answer-1));
	}
	
    }

    public static void CheckMessages() {

	String sql = "SELECT * FROM Messages WHERE receiver = '" + User.getEmail() + "'";
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();
	ArrayList<String> times = new ArrayList<String>();
	ArrayList<String> senders = new ArrayList<String>();
	ArrayList<Integer> types = new ArrayList<Integer>();

	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	try {
	    while (rs.next()) {
		ids.add(rs.getString(1));
		messages.add(rs.getString(2).trim());
		times.add(rs.getString(3).trim());
		senders.add(rs.getString(4).trim());
		types.add(new Integer(rs.getInt(7)));
	    }
	} catch (Exception e) {System.out.println(e);}

	SQLHelper.Close();
	
	if (ids.size() == 0) {
	    System.out.println("No messages");
	    return;
	}

	System.out.println("You have " + ids.size() + " message(s)");

	String names[] = new String[ids.size()];
	for (int i = 0; i < ids.size(); i++) {
	    names[i] = User.NameGivenEmail(senders.get(i)) + " (" + senders.get(i) + ")";
	    if (types.get(i).equals(PRIVATE_MSG))
		names[i] += "[PRIVATE]";
	}


	int answer = Menu.DisplayMenu("Which message would you like to read?", names);
	answer--;
	System.out.println("------------Displaying Message------------\nMessage ID: " + ids.get(answer) + "\nTimestamp: " + times.get(answer) + "\n\n" + messages.get(answer) + "\n------------------------\n");

	if (types.get(answer).equals(PRIVATE_MSG)) {
	    String items[] = {"Yes", "No"};
	    int temp = Menu.DisplayMenu("Delete message?", items);
	    if (temp == 1) {
		sql = "DELETE FROM Messages WHERE id = " + ids.get(answer);
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();

		System.out.println("Message deleted");
	    }
	}
		
    }
    
    public static void SendPrivateMessage(String text, String receiver) {
        // message_id, text, timestamp, sender, receiver, owner, message_type
	
	//String sql = "CREATE TABLE Messages(id INTEGER NOT NULL, text CHAR(1400) NOT NULL, timestamp TIMESTAMP NOT NULL, sender CHAR(20), receiver CHAR(20), owner CHAR(20), type INTEGER NOT NULL, FOREIGN KEY(sender) REFERENCES Users(email), FOREIGN KEY(receiver) REFERENCES Users(email), FOREIGN KEY(owner) REFERENCES Users(email), PRIMARY KEY(id))";


	String sql = "SELECT Max(id) FROM Messages";
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	int id = 0; // the id will be the max of the current ids plus 1"
	try {
	    while (rs.next()) {
		id = rs.getInt(1) + 1;
	    }
	} catch (Exception e) {System.out.println(e);}
	
	SQLHelper.Close();


	Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());

	sql = "INSERT INTO Messages VALUES (" + id + ", '" + text + "', to_timestamp('" + time + "', 'YYYY-MM-DD HH24:MI:SS.FF'), '" + User.getEmail() + "', '" + receiver + "', '" + User.getEmail() + "', " + PRIVATE_MSG + ")";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	System.out.println("Message sent!");
	
    }
    
    public static void displayPrivateMessages() {
        /*ArrayList<String> friendsList = new ArrayList<String>();
        ArrayList<String> friendsEmailList = new ArrayList<String>();
        ResultSet rs = SQLHelper.ExecuteSQL("SELECT U.name, U.screen_name, U.email FROM Users U");
        while (rs.next) {
            friendsList.add(rs.getString(1));
            friendsEmailList.add(rs.getSTring(3));
        }
        int answer = Menu.DisplayMenu(friendsList.toArray());
        Messages.getPrivateMessagesFrom(friendsEmailList[answer-1]);*/

    }

    public static void getPrivateMessagesFrom(String senderEmail) {
        /*ResultSet rs = SQLHelper.ExecuteSQL("SELECT M.text, M.timestamp, M.sender FROM Messages M WHERE message_type = 0 AND recipient = " + User.getEmail() + " AND sender = " + senderEmail);
        while(rs.next()) {
            System.out.println( currentMessages.getString(1).trim() 
                                + "at " 
                                + currentMessages.getString(2).trim() 
                                + "from " 
                                + currentMessages.getString(3).trim()
                                );
        }
        SQLHelper.Close();*/
    }
}
